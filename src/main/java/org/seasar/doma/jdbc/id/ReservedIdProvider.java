package org.seasar.doma.jdbc.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.message.Message;

/** An identity provider that reserves identity values in advance. */
public class ReservedIdProvider implements IdProvider {

  protected final Config config;

  protected final EntityDesc<?> entityDesc;

  protected final int reservationSize;

  protected final boolean available;

  protected long[] identities;

  protected int index = 0;

  public ReservedIdProvider(Config config, EntityDesc<?> entityDesc, int reservationSize) {
    assertNotNull(config, entityDesc);
    this.config = config;
    this.entityDesc = entityDesc;
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
    long[] identities = new long[reservationSize];
    Sql<?> sql = createSql();
    JdbcLogger logger = config.getJdbcLogger();
    Connection connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      PreparedStatement preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        logger.logSql(getClass().getName(), "getIdentities", sql);
        setupOptions(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
          for (int i = 0; i < reservationSize && resultSet.next(); i++) {
            identities[i] = resultSet.getLong(1);
          }
        } catch (final SQLException e) {
          throw new JdbcException(Message.DOMA2083, e, entityDesc.getName(), e);
        } finally {
          JdbcUtil.close(resultSet, logger);
        }
      } catch (SQLException e) {
        throw new JdbcException(Message.DOMA2083, e, entityDesc.getName(), e);
      } finally {
        JdbcUtil.close(preparedStatement, logger);
      }
    } finally {
      JdbcUtil.close(connection, logger);
    }
    return identities;
  }

  protected Sql<?> createSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    String catalogName = entityDesc.getCatalogName();
    String schemaName = entityDesc.getSchemaName();
    String tableName = entityDesc.getTableName(naming::apply);
    String idColumnName = entityDesc.getGeneratedIdPropertyDesc().getColumnName(naming::apply);
    boolean isQuoteRequired = entityDesc.isQuoteRequired();
    boolean isIdColumnQuoteRequired = entityDesc.getGeneratedIdPropertyDesc().isQuoteRequired();
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
