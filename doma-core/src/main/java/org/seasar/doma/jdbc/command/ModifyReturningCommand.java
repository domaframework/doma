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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlExecutionException;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.ModifyQuery;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * An abstract command that executes a SQL statement with a RETURNING clause and retrieves a result
 * set. This is the base class for commands that execute INSERT, UPDATE, or DELETE statements with
 * RETURNING clauses.
 *
 * @param <QUERY> the type of query
 * @param <RESULT> the type of result
 */
public abstract class ModifyReturningCommand<QUERY extends ModifyQuery, RESULT>
    implements Command<RESULT> {

  /** The query to execute. */
  protected final QUERY query;

  /** The SQL to execute. */
  protected final PreparedSql sql;

  /** The handler for the result set. */
  protected final ResultSetHandler<RESULT> resultSetHandler;

  /** The supplier of empty result. */
  protected final Supplier<RESULT> emptyResultSupplier;

  /**
   * Creates an instance.
   *
   * @param query the query to execute
   * @param resultSetHandler the handler for the result set
   * @param emptyResultSupplier the supplier of empty result
   */
  protected ModifyReturningCommand(
      QUERY query,
      ResultSetHandler<RESULT> resultSetHandler,
      Supplier<RESULT> emptyResultSupplier) {
    assertNotNull(query, resultSetHandler, emptyResultSupplier);
    this.query = query;
    this.sql = query.getSql();
    this.resultSetHandler = resultSetHandler;
    this.emptyResultSupplier = emptyResultSupplier;
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
   * Executes the SQL statement and retrieves the result.
   *
   * @return the result
   * @throws SqlExecutionException if a database access error occurs
   */
  @Override
  public RESULT execute() {
    if (!query.isExecutable()) {
      JdbcLogger logger = query.getConfig().getJdbcLogger();
      logger.logSqlExecutionSkipping(
          query.getClassName(), query.getMethodName(), query.getSqlExecutionSkipCause());
      return emptyResultSupplier.get();
    }
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      PreparedStatement preparedStatement = prepareStatement(connection);
      try {
        log();
        setupOptions(preparedStatement);
        bindParameters(preparedStatement);
        return executeInternal(preparedStatement);
      } catch (SQLException e) {
        Dialect dialect = query.getConfig().getDialect();
        throw new SqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger());
      }
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }

  /**
   * Prepares a statement.
   *
   * @param connection the connection
   * @return the prepared statement
   */
  protected PreparedStatement prepareStatement(Connection connection) {
    return JdbcUtil.prepareStatement(connection, sql);
  }

  /**
   * Executes the SQL statement and handles the result set.
   *
   * @param preparedStatement the prepared statement
   * @return the result
   * @throws SQLException if a database access error occurs
   */
  protected abstract RESULT executeInternal(PreparedStatement preparedStatement)
      throws SQLException;

  /** Logs the SQL statement. */
  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  /**
   * Sets up options for the prepared statement.
   *
   * @param preparedStatement the prepared statement
   * @throws SQLException if a database access error occurs
   */
  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  /**
   * Binds parameters to the prepared statement.
   *
   * @param preparedStatement the prepared statement
   * @throws SQLException if a database access error occurs
   */
  protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

  /**
   * Executes the query and processes the result set.
   *
   * @param preparedStatement the prepared statement
   * @return the result
   * @throws SQLException if a database access error occurs
   */
  protected RESULT executeQuery(PreparedStatement preparedStatement) throws SQLException {
    Supplier<RESULT> supplier;
    ResultSet resultSet = preparedStatement.executeQuery();
    try {
      supplier = handleResultSet(resultSet);
    } finally {
      JdbcUtil.close(resultSet, query.getConfig().getJdbcLogger());
    }
    return supplier.get();
  }

  /**
   * Handles the result set.
   *
   * @param resultSet the result set
   * @return the supplier of result
   * @throws SQLException if a database access error occurs
   * @throws OptimisticLockException if no rows are affected and optimistic lock checking is enabled
   */
  protected Supplier<RESULT> handleResultSet(ResultSet resultSet) throws SQLException {
    return resultSetHandler.handle(
        resultSet,
        new ReturningQuery(),
        (index, next) -> {
          if (index == -1 && !next && query.isOptimisticLockCheckRequired()) {
            throw new OptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
          }
        });
  }

  /** An implementation of {@link SelectQuery} for handling result sets from RETURNING clauses. */
  class ReturningQuery implements SelectQuery {

    /**
     * Returns the SQL to execute.
     *
     * @return the SQL
     */
    @Override
    public PreparedSql getSql() {
      return sql;
    }

    /**
     * Returns the options for the select statement.
     *
     * @return the options
     */
    @Override
    public SelectOptions getOptions() {
      return SelectOptions.get();
    }

    /**
     * Indicates whether the result is ensured.
     *
     * @return {@code true} if the result is ensured
     */
    @Override
    public boolean isResultEnsured() {
      return true;
    }

    /**
     * Indicates whether the result mapping is ensured.
     *
     * @return {@code false} as the result mapping is not ensured
     */
    @Override
    public boolean isResultMappingEnsured() {
      return false;
    }

    /**
     * Returns the fetch type.
     *
     * @return the fetch type
     */
    @Override
    public FetchType getFetchType() {
      return FetchType.EAGER;
    }

    /**
     * Returns the fetch size.
     *
     * @return the fetch size
     */
    @Override
    public int getFetchSize() {
      return query.getConfig().getFetchSize();
    }

    /**
     * Returns the maximum number of rows.
     *
     * @return the maximum number of rows
     */
    @Override
    public int getMaxRows() {
      return query.getConfig().getMaxRows();
    }

    /**
     * Returns the SQL log type.
     *
     * @return the SQL log type
     */
    @Override
    public SqlLogType getSqlLogType() {
      return query.getSqlLogType();
    }

    /**
     * Indicates whether the result is a stream.
     *
     * @return {@code false} as the result is not a stream
     */
    @Override
    public boolean isResultStream() {
      return false;
    }

    /**
     * Returns the class name.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
      return query.getClassName();
    }

    /**
     * Returns the method name.
     *
     * @return the method name
     */
    @Override
    public String getMethodName() {
      return query.getMethodName();
    }

    /**
     * Returns the method.
     *
     * @return the method
     */
    @Override
    public Method getMethod() {
      return query.getMethod();
    }

    /**
     * Returns the configuration.
     *
     * @return the configuration
     */
    @Override
    public Config getConfig() {
      return query.getConfig();
    }

    /**
     * Returns the query timeout.
     *
     * @return the query timeout
     */
    @Override
    public int getQueryTimeout() {
      return query.getQueryTimeout();
    }

    /** Prepares the query. This method does nothing. */
    @Override
    public void prepare() {}

    /** Completes the query. This method does nothing. */
    @Override
    public void complete() {}

    /**
     * Adds a comment to the SQL.
     *
     * @param sql the SQL
     * @return the SQL with a comment
     */
    @Override
    public String comment(String sql) {
      return query.comment(sql);
    }
  }
}
