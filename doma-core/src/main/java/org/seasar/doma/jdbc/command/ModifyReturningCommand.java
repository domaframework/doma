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

public abstract class ModifyReturningCommand<QUERY extends ModifyQuery, RESULT>
    implements Command<RESULT> {

  protected final QUERY query;

  protected final PreparedSql sql;

  protected final ResultSetHandler<RESULT> resultSetHandler;

  protected final Supplier<RESULT> emptyResultSupplier;

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

  @Override
  public QUERY getQuery() {
    return query;
  }

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

  protected PreparedStatement prepareStatement(Connection connection) {
    return JdbcUtil.prepareStatement(connection, sql);
  }

  protected abstract RESULT executeInternal(PreparedStatement preparedStatement)
      throws SQLException;

  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

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

  class ReturningQuery implements SelectQuery {

    @Override
    public PreparedSql getSql() {
      return sql;
    }

    @Override
    public SelectOptions getOptions() {
      return SelectOptions.get();
    }

    @Override
    public boolean isResultEnsured() {
      return true;
    }

    @Override
    public boolean isResultMappingEnsured() {
      return false;
    }

    @Override
    public FetchType getFetchType() {
      return FetchType.EAGER;
    }

    @Override
    public int getFetchSize() {
      return query.getConfig().getFetchSize();
    }

    @Override
    public int getMaxRows() {
      return query.getConfig().getMaxRows();
    }

    @Override
    public SqlLogType getSqlLogType() {
      return query.getSqlLogType();
    }

    @Override
    public boolean isResultStream() {
      return false;
    }

    @Override
    public String getClassName() {
      return query.getClassName();
    }

    @Override
    public String getMethodName() {
      return query.getMethodName();
    }

    @Override
    public Method getMethod() {
      return query.getMethod();
    }

    @Override
    public Config getConfig() {
      return query.getConfig();
    }

    @Override
    public int getQueryTimeout() {
      return query.getQueryTimeout();
    }

    @Override
    public void prepare() {}

    @Override
    public void complete() {}

    @Override
    public String comment(String sql) {
      return query.comment(sql);
    }
  }
}
