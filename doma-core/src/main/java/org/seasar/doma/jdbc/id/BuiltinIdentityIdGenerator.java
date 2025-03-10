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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.GenerationType;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;

/** The built-in implementation for {@link IdentityIdGenerator}. */
public class BuiltinIdentityIdGenerator extends AbstractIdGenerator implements IdentityIdGenerator {

  @Override
  public boolean supportsBatch(IdGenerationConfig config) {
    return config.getDialect().supportsBatchExecutionReturningGeneratedValues();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean includesIdentityColumn(IdGenerationConfig config) {
    return config.getDialect().includesIdentityColumn();
  }

  @Override
  public boolean includesIdentityColumn(IdGenerationConfig config, Object idValue) {
    return config.getDialect().includesIdentityColumn(idValue);
  }

  @Override
  public boolean supportsAutoGeneratedKeys(IdGenerationConfig config) {
    return config.getDialect().supportsAutoGeneratedKeys();
  }

  @Override
  public Long generatePreInsert(IdGenerationConfig config) {
    return null;
  }

  @Override
  public Long generatePostInsert(IdGenerationConfig config, Statement statement) {
    if (config.getDialect().supportsAutoGeneratedKeys()) {
      return getGeneratedValue(config, statement);
    }
    return getGeneratedValue(config);
  }

  /**
   * Retrieves the generated value by using {@link Statement#getGeneratedKeys()}.
   *
   * @param config the configuration
   * @param statement the SQL INSERT statement
   * @return the generated value
   * @throws JdbcException if the generation is failed
   */
  protected long getGeneratedValue(IdGenerationConfig config, Statement statement) {
    try {
      final ResultSet resultSet = statement.getGeneratedKeys();
      return getGeneratedValue(config, resultSet);
    } catch (final SQLException e) {
      throw new JdbcException(Message.DOMA2018, e, config.getEntityType().getName(), e);
    }
  }

  /**
   * Retrieves the generated value by using a specific SQL.
   *
   * @param config the configuration
   * @return the generated value
   * @throws JdbcException if the generation is failed
   */
  protected long getGeneratedValue(IdGenerationConfig config) {
    Naming naming = config.getNaming();
    EntityType<?> entityType = config.getEntityType();
    String catalogName = entityType.getCatalogName();
    String schemaName = entityType.getSchemaName();
    String tableName = entityType.getTableName(naming::apply);
    String idColumnName = entityType.getGeneratedIdPropertyType().getColumnName(naming::apply);
    Sql<?> sql =
        config
            .getDialect()
            .getIdentitySelectSql(
                catalogName,
                schemaName,
                tableName,
                idColumnName,
                entityType.isQuoteRequired(),
                entityType.getGeneratedIdPropertyType().isQuoteRequired());
    return getGeneratedValue(config, sql);
  }

  @Override
  public List<Long> generateValuesPostInsert(IdGenerationConfig config, Statement statement) {
    JdbcLogger logger = config.getJdbcLogger();
    List<Long> values = new ArrayList<>();
    ResultSet resultSet = null;
    try {
      resultSet = statement.getGeneratedKeys();
      while (resultSet.next()) {
        long value = resultSet.getLong(1);
        values.add(value);
      }
    } catch (final SQLException e) {
      throw new JdbcException(Message.DOMA2018, e, config.getEntityType().getName(), e);
    } finally {
      JdbcUtil.close(resultSet, logger);
    }
    return values;
  }

  @Override
  public GenerationType getGenerationType() {
    return GenerationType.IDENTITY;
  }
}
