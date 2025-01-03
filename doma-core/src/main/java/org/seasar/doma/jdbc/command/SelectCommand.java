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

public class SelectCommand<RESULT> implements Command<RESULT> {

  protected final SelectQuery query;

  protected final PreparedSql sql;

  protected final ResultSetHandler<RESULT> resultSetHandler;

  public SelectCommand(SelectQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    assertNotNull(query, resultSetHandler);
    this.query = query;
    this.sql = query.getSql();
    this.resultSetHandler = resultSetHandler;
  }

  @Override
  public SelectQuery getQuery() {
    return query;
  }

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

  protected void log() {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

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

  protected void bindParameters(PreparedStatement preparedStatement) throws SQLException {
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

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
