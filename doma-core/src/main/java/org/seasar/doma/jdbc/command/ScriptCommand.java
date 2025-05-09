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
package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.ScriptReader;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.AbstractSql;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.ScriptException;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.query.ScriptQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command to execute SQL scripts.
 *
 * <p>This command reads and executes SQL statements from a script file.
 */
public class ScriptCommand implements Command<Void> {

  /** the query */
  protected final ScriptQuery query;

  /** the saved script exception that occurred during execution */
  protected ScriptException savedScriptException;

  /**
   * Creates a new instance.
   *
   * @param query the script query
   * @throws NullPointerException if the query is null
   */
  public ScriptCommand(ScriptQuery query) {
    assertNotNull(query);
    this.query = query;
  }

  /**
   * Returns the query.
   *
   * @return the query
   */
  @Override
  public ScriptQuery getQuery() {
    return query;
  }

  /**
   * Executes the script.
   *
   * @return null
   * @throws ScriptException if a script execution error occurs
   */
  @Override
  public Void execute() {
    Config config = query.getConfig();
    StatisticManager statisticManager = config.getStatisticManager();
    Connection connection = JdbcUtil.getConnection(config.getDataSource());
    try {
      ScriptReader reader = new ScriptReader(query);
      try {
        for (String script = reader.readSql(); script != null; script = reader.readSql()) {
          ScriptSql sql =
              new ScriptSql(
                  script, query.getScriptFilePath(), query.getSqlLogType(), query::comment);
          Statement statement = JdbcUtil.createStatement(connection);
          try {
            log(sql);
            setupOptions(statement);
            final String s = script;
            statisticManager.executeSql(sql, () -> statement.execute(s));
          } catch (Exception e) {
            if (query.getHaltOnError()) {
              throw new ScriptException(e, sql, reader.getLineNumber());
            }
            if (savedScriptException == null) {
              savedScriptException = new ScriptException(e, sql, reader.getLineNumber());
            }
          } finally {
            JdbcUtil.close(statement, config.getJdbcLogger());
          }
        }
      } finally {
        reader.close();
      }
    } finally {
      JdbcUtil.close(connection, config.getJdbcLogger());
    }
    throwSavedScriptExceptionIfExists();
    return null;
  }

  /**
   * Logs the SQL statement.
   *
   * @param sql the SQL statement
   */
  protected void log(ScriptSql sql) {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  /**
   * Sets up options for the statement.
   *
   * @param statement the statement
   * @throws SQLException if a database access error occurs
   */
  protected void setupOptions(Statement statement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      statement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  /**
   * Throws the saved script exception if it exists.
   *
   * @throws ScriptException if a script execution error occurred
   */
  protected void throwSavedScriptExceptionIfExists() {
    if (savedScriptException != null) {
      throw savedScriptException;
    }
  }

  /** A SQL statement in a script. */
  protected static class ScriptSql extends AbstractSql<SqlParameter> {

    /**
     * Creates a new instance.
     *
     * @param rawSql the raw SQL statement
     * @param sqlFilePath the SQL file path
     * @param sqlLogType the SQL log type
     * @param converter the converter function
     */
    public ScriptSql(
        String rawSql,
        String sqlFilePath,
        SqlLogType sqlLogType,
        Function<String, String> converter) {
      super(
          SqlKind.SCRIPT,
          rawSql,
          rawSql,
          sqlFilePath,
          Collections.emptyList(),
          sqlLogType,
          converter);
    }
  }
}
