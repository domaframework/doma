/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.statistic.StatisticManager;
import org.seasar.doma.message.Message;

/** An identity provider that reserves identity values in advance. */
@Deprecated
public class ReservedIdProvider implements IdProvider {

  protected final Config config;

  protected final EntityType<?> entityType;

  protected final int reservationSize;

  protected final boolean available;

  protected long[] identities;

  protected int index = 0;

  public ReservedIdProvider(Config config, EntityType<?> entityType, int reservationSize) {
    assertNotNull(config, entityType);
    this.config = config;
    this.entityType = entityType;
    this.reservationSize = reservationSize;
    this.available = config.getDialect().supportsIdentityReservation();
  }

  @Override
  public boolean isAvailable() {
    return available;
  }

  @Override
  public long get() {
    if (!available) {
      throw new UnsupportedOperationException();
    }
    if (identities == null) {
      identities = getIdentities();
    }
    if (identities.length <= index) {
      throw new IllegalStateException(
          String.format("identities.length=%d, index=%d", identities.length, index));
    }
    return identities[index++];
  }

  protected long[] getIdentities() {
    Sql<?> sql = createSql();
    JdbcLogger logger = config.getJdbcLogger();
    StatisticManager statisticManager = config.getStatisticManager();
    Connection connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      PreparedStatement preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "getIdentities", sql);
        setupOptions(preparedStatement);
        return statisticManager.executeSql(
            sql,
            () -> {
              long[] identities = new long[reservationSize];
              ResultSet resultSet = preparedStatement.executeQuery();
              try {
                for (int i = 0; i < reservationSize && resultSet.next(); i++) {
                  identities[i] = resultSet.getLong(1);
                }
              } catch (final SQLException e) {
                throw new JdbcException(Message.DOMA2083, e, entityType.getName(), e);
              } finally {
                JdbcUtil.close(resultSet, logger);
              }
              return identities;
            });
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2083, e, entityType.getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
  }

  protected Sql<?> createSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    String catalogName = entityType.getCatalogName();
    String schemaName = entityType.getSchemaName();
    String tableName = entityType.getTableName(naming::apply);
    String idColumnName = entityType.getGeneratedIdPropertyType().getColumnName(naming::apply);
    boolean isQuoteRequired = entityType.isQuoteRequired();
    boolean isIdColumnQuoteRequired = entityType.getGeneratedIdPropertyType().isQuoteRequired();
    return dialect.getIdentityReservationSql(
        catalogName,
        schemaName,
        tableName,
        idColumnName,
        isQuoteRequired,
        isIdColumnQuoteRequired,
        reservationSize);
  }

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (config.getFetchSize() > 0) {
      preparedStatement.setFetchSize(config.getFetchSize());
    }
    if (config.getMaxRows() > 0) {
      preparedStatement.setMaxRows(config.getMaxRows());
    }
    if (config.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(config.getQueryTimeout());
    }
  }
}
