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
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A query that processes SQL files. This class is responsible for loading SQL files and building
 * prepared SQL statements with parameter binding.
 */
public class SqlProcessorQuery extends AbstractQuery {

  /** The parameters to be bound to the SQL. */
  protected final Map<String, Value> parameters = new HashMap<>();

  /** The path to the SQL file. */
  protected String sqlFilePath;

  /** The SQL file. */
  protected SqlFile sqlFile;

  /** The prepared SQL statement. */
  protected PreparedSql sql;

  /**
   * Sets the path to the SQL file.
   *
   * @param sqlFilePath the SQL file path
   */
  public void setSqlFilePath(String sqlFilePath) {
    this.sqlFilePath = sqlFilePath;
  }

  /**
   * Adds a parameter to be bound to the SQL.
   *
   * @param name the parameter name
   * @param type the parameter type
   * @param value the parameter value
   */
  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    parameters.put(name, new Value(type, value));
  }

  /** Prepares this query for execution. This method must be called before {@link #getSql()}. */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlFilePath);
    prepareSql();
  }

  /** Prepares the SQL statement by loading the SQL file and building the prepared SQL. */
  protected void prepareSql() {
    sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SQL_PROCESSOR, sqlFilePath, evaluator, SqlLogType.FORMATTED);
    sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
  }

  /**
   * Returns the prepared SQL statement.
   *
   * @return the prepared SQL statement
   */
  @Override
  public PreparedSql getSql() {
    return sql;
  }

  /** Completes this query. This method is called after the query execution. */
  @Override
  public void complete() {}
}
