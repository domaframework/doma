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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.command.CallableSqlParameterBinder;
import org.seasar.doma.internal.jdbc.command.CallableSqlParameterFetcher;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.ModuleQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * An abstract command that executes database modules such as stored procedures and functions.
 *
 * <p>This class provides common functionality for executing database modules using JDBC's {@link
 * CallableStatement}. It handles connection management, parameter binding, and execution of SQL
 * statements.
 *
 * @param <QUERY> the query type
 * @param <RESULT> the result type
 */
public abstract class ModuleCommand<QUERY extends ModuleQuery, RESULT> implements Command<RESULT> {

  /** The query to execute. */
  protected final QUERY query;

  /** The SQL to be executed. */
  protected final CallableSql sql;

  /**
   * Creates a new instance.
   *
   * @param query the query to execute
   * @throws NullPointerException if the query is null
   */
  protected ModuleCommand(QUERY query) {
    assertNotNull(query);
    this.query = query;
    this.sql = query.getSql();
  }

  /**
   * Returns the query.
   *
   * @return the query
   */
  @Override
  public QUERY getQuery() {
    return query;
  }

  /**
   * Executes this command.
   *
   * <p>This method handles the lifecycle of JDBC resources, including obtaining a connection,
   * creating a statement, binding parameters, executing the SQL, and closing resources.
   *
   * @return the result of the execution
   * @throws SqlExecutionException if a database access error occurs
   */
  @Override
  public RESULT execute() {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      CallableStatement callableStatement = JdbcUtil.prepareCall(connection, sql);
      try {
        log();
        setupOptions(callableStatement);
        bindParameters(callableStatement);
        return statisticManager.executeSql(sql, () -> executeInternal(callableStatement));
      } catch (SQLException e) {
        Dialect dialect = query.getConfig().getDialect();
        throw new SqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        JdbcUtil.close(callableStatement, query.getConfig().getJdbcLogger());
      }
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }

  /**
   * Executes the internal operation with the given callable statement.
   *
   * <p>This method is called by {@link #execute()} after setting up the statement and binding
   * parameters. Subclasses must implement this method to define the specific execution behavior.
   *
   * @param callableStatement the statement to execute
   * @return the result of the execution
   * @throws SQLException if a database access error occurs
   */
  protected abstract RESULT executeInternal(CallableStatement callableStatement)
      throws SQLException;

  /**
   * Sets up options for the callable statement.
   *
   * <p>This implementation sets the query timeout if specified.
   *
   * @param preparedStatement the statement to set options on
   * @throws SQLException if a database access error occurs
   */
  protected void setupOptions(CallableStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  /**
   * Binds parameters to the callable statement.
   *
   * @param callableStatement the statement to bind parameters to
   * @throws SQLException if a database access error occurs
   */
  protected void bindParameters(CallableStatement callableStatement) throws SQLException {
    CallableSqlParameterBinder binder = new CallableSqlParameterBinder(query);
    binder.bind(callableStatement, sql.getParameters());
  }

  /**
   * Fetches output parameters from the callable statement after execution.
   *
   * @param callableStatement the statement to fetch parameters from
   * @throws SQLException if a database access error occurs
   */
  protected void fetchParameters(CallableStatement callableStatement) throws SQLException {
    CallableSqlParameterFetcher fetcher = new CallableSqlParameterFetcher(query);
    fetcher.fetch(callableStatement, sql.getParameters());
  }

  /** Logs the SQL statement before execution. */
  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }
}
