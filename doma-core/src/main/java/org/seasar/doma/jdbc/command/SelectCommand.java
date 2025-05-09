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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.NoResultException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command that executes SELECT SQL queries and processes the result sets.
 *
 * <p>This class handles the execution of database queries that retrieve data, including connection
 * management, statement preparation, parameter binding, and result set processing. It uses a {@link
 * ResultSetHandler} to convert the JDBC result set into the desired return type.
 *
 * <p>The command supports both eager and lazy fetching of results, with proper resource management
 * for each approach. When using lazy fetching with streams, resources are automatically closed when
 * the stream is closed.
 *
 * @param <RESULT> the type of result returned by this command
 */
public class SelectCommand<RESULT> implements Command<RESULT> {

  /**
   * The query object that contains the SELECT SQL statement and related configuration. This member
   * is initialized in the constructor and remains unchanged throughout the command's lifecycle.
   */
  protected final SelectQuery query;

  /**
   * The prepared SQL statement that will be executed. This is extracted from the query object and
   * contains both the SQL string and its parameters.
   */
  protected final PreparedSql sql;

  /**
   * The handler responsible for processing the result set returned by the query execution. It
   * converts the JDBC ResultSet into the desired return type specified by the RESULT generic
   * parameter.
   */
  protected final ResultSetHandler<RESULT> resultSetHandler;

  /**
   * Constructs a new SelectCommand with the specified query and result set handler.
   *
   * @param query the query to execute, which contains the SQL statement and configuration
   * @param resultSetHandler the handler that processes the result set and converts it to the
   *     desired type
   */
  public SelectCommand(SelectQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    assertNotNull(query, resultSetHandler);
    this.query = query;
    this.sql = query.getSql();
    this.resultSetHandler = resultSetHandler;
  }

  /**
   * Returns the query object associated with this command.
   *
   * @return the select query object
   */
  @Override
  public SelectQuery getQuery() {
    return query;
  }

  /**
   * Executes the SELECT SQL query and processes the result set.
   *
   * <p>This method handles the entire lifecycle of a database query:
   *
   * <ol>
   *   <li>Obtaining a database connection
   *   <li>Preparing the SQL statement
   *   <li>Setting up statement options (fetch size, max rows, timeout)
   *   <li>Binding parameters to the statement
   *   <li>Executing the query
   *   <li>Processing the result set
   *   <li>Properly closing all resources
   * </ol>
   *
   * <p>For lazy fetching with streams, resources are automatically closed when the stream is
   * closed. Statistics are recorded if enabled in the configuration.
   *
   * @return the result of the query execution, as processed by the result set handler
   * @throws SqlExecutionException if a database access error occurs
   */
  @Override
  public RESULT execute() {
    Supplier<RESULT> supplier = null;
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      PreparedStatement preparedStatement = JdbcUtil.prepareStatement(connection, sql);
      try {
        log();
        setupOptions(preparedStatement);
        bindParameters(preparedStatement);
        if (statisticManager.isEnabled()) {
          long startTimeNanos = System.nanoTime();
          supplier =
              defer(
                  executeQuery(preparedStatement),
                  () -> {
                    // If a ResultSet is wrapped in a Stream, the execution time is considered to
                    // last until the Stream is closed.
                    long endTimeNanos = System.nanoTime();
                    statisticManager.recordSqlExecution(sql, startTimeNanos, endTimeNanos);
                  });
        } else {
          supplier = executeQuery(preparedStatement);
        }
      } catch (SQLException e) {
        Dialect dialect = query.getConfig().getDialect();
        throw new SqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        supplier =
            defer(
                supplier,
                () -> JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger()));
      }
    } finally {
      supplier =
          defer(supplier, () -> JdbcUtil.close(connection, query.getConfig().getJdbcLogger()));
    }
    return supplier.get();
  }

  /**
   * Logs the SQL statement that will be executed.
   *
   * <p>This method uses the JDBC logger configured in the query to log the SQL statement along with
   * the class name and method name that initiated the query.
   */
  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  /**
   * Sets up the options for the prepared statement based on the query configuration.
   *
   * <p>This method configures the following options if they are specified in the query:
   *
   * <ul>
   *   <li>Fetch size - controls how many rows are fetched from the database at once
   *   <li>Max rows - limits the maximum number of rows that can be returned
   *   <li>Query timeout - sets the maximum time in seconds that a query can execute
   * </ul>
   *
   * @param preparedStatement the prepared statement to configure
   * @throws SQLException if a database access error occurs
   */
  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getFetchSize() > 0) {
      preparedStatement.setFetchSize(query.getFetchSize());
    }
    if (query.getMaxRows() > 0) {
      preparedStatement.setMaxRows(query.getMaxRows());
    }
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  /**
   * Binds the SQL parameters to the prepared statement.
   *
   * <p>This method uses a {@link PreparedSqlParameterBinder} to set the parameter values in the
   * prepared statement according to their types and positions.
   *
   * @param preparedStatement the prepared statement to bind parameters to
   * @throws SQLException if a database access error occurs or if a parameter value is incompatible
   *     with the designated SQL type
   */
  protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

  /**
   * Executes the SQL query and processes the result set.
   *
   * <p>This method executes the prepared statement and obtains a result set. It then delegates the
   * processing of the result set to the {@link #handleResultSet(ResultSet)} method. The result set
   * is properly closed when the returned supplier is consumed, or immediately if eager fetching is
   * used.
   *
   * @param preparedStatement the prepared statement to execute
   * @return a supplier that provides the processed result
   * @throws SQLException if a database access error occurs during query execution
   */
  protected Supplier<RESULT> executeQuery(PreparedStatement preparedStatement) throws SQLException {
    Supplier<RESULT> supplier = null;
    ResultSet resultSet = preparedStatement.executeQuery();
    try {
      supplier = handleResultSet(resultSet);
    } finally {
      supplier =
          defer(supplier, () -> JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger()));
    }
    return supplier;
  }

  /**
   * Processes the result set using the configured result set handler.
   *
   * <p>This method delegates the processing of the result set to the result set handler that was
   * provided in the constructor. It also checks if the result set is empty when a result is
   * required, and throws a {@link NoResultException} in that case.
   *
   * @param resultSet the result set to process
   * @return a supplier that provides the processed result
   * @throws SQLException if a database access error occurs during result set processing
   * @throws NoResultException if no result is found and a result is required by the query
   */
  protected Supplier<RESULT> handleResultSet(ResultSet resultSet) throws SQLException {
    return resultSetHandler.handle(
        resultSet,
        query,
        (index, next) -> {
          if (index == -1 && !next && query.isResultEnsured()) {
            Sql<?> sql = query.getSql();
            throw new NoResultException(query.getConfig().getExceptionSqlLogType(), sql);
          }
        });
  }

  /**
   * @deprecated Use {@link #defer(Supplier, Runnable)} instead.
   */
  @Deprecated(forRemoval = true)
  protected void close(Supplier<RESULT> supplier, Runnable closeHandler) {
    if (supplier != null && query.isResultStream() && query.getFetchType() == FetchType.LAZY) {
      RESULT result = supplier.get();
      if (result instanceof Stream) {
        Stream<?> stream = (Stream<?>) result;
        //noinspection ResultOfMethodCallIgnored
        stream.onClose(closeHandler);
      } else {
        closeHandler.run();
      }
    } else {
      closeHandler.run();
    }
  }

  /**
   * Defers the execution of a runnable until the result is consumed.
   *
   * <p>This method is used for resource management. When lazy fetching is enabled and the result is
   * a Stream, the runnable (typically a resource cleanup operation) is registered as an onClose
   * handler for the stream, ensuring that resources are properly closed when the stream is closed.
   * For non-stream results or eager fetching, the runnable is executed immediately.
   *
   * @param supplier the supplier of the result
   * @param runnable the operation to defer or execute immediately
   * @return the original supplier or a new supplier that wraps the result with the deferred
   *     operation
   */
  @SuppressWarnings("unchecked")
  protected Supplier<RESULT> defer(Supplier<RESULT> supplier, Runnable runnable) {
    if (supplier != null && query.isResultStream() && query.getFetchType() == FetchType.LAZY) {
      RESULT value = supplier.get();
      if (value instanceof Stream<?> stream) {
        Stream<?> newStream = stream.onClose(runnable);
        return () -> (RESULT) newStream;
      } else {
        runnable.run();
      }
    } else {
      runnable.run();
    }
    return supplier;
  }
}
