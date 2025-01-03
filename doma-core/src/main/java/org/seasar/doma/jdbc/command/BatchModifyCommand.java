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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.BatchOptimisticLockException;
import org.seasar.doma.jdbc.BatchSqlExecutionException;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.BatchModifyQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

public abstract class BatchModifyCommand<QUERY extends BatchModifyQuery> implements Command<int[]> {

  protected final QUERY query;

  protected BatchModifyCommand(QUERY query) {
    assertNotNull(query);
    this.query = query;
  }

  @Override
  public QUERY getQuery() {
    return query;
  }

  @Override
  public int[] execute() {
    if (!query.isExecutable()) {
      JdbcLogger logger = query.getConfig().getJdbcLogger();
      logger.logSqlExecutionSkipping(
          query.getClassName(), query.getMethodName(), query.getSqlExecutionSkipCause());
      return new int[] {};
    }
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      PreparedSql sql = query.getSql();
      PreparedStatement preparedStatement = prepareStatement(connection, sql);
      try {
        setupOptions(preparedStatement);
        return executeInternal(preparedStatement, query.getSqls());
      } catch (SQLException e) {
        Dialect dialect = query.getConfig().getDialect();
        throw new BatchSqlExecutionException(
            query.getConfig().getExceptionSqlLogType(), sql, e, dialect.getRootCause(e));
      } finally {
        JdbcUtil.close(preparedStatement, query.getConfig().getJdbcLogger());
      }
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }

  protected PreparedStatement prepareStatement(Connection connection, PreparedSql sql) {
    if (query.isAutoGeneratedKeysSupported()) {
      Config config = query.getConfig();
      Dialect dialect = config.getDialect();
      switch (dialect.getAutoGeneratedKeysType()) {
        case FIRST_COLUMN:
          return JdbcUtil.prepareStatementForAutoGeneratedKeysOfFirstColumn(connection, sql);
        case DEFAULT:
          return JdbcUtil.prepareStatementForAutoGeneratedKeys(connection, sql);
      }
    }
    return JdbcUtil.prepareStatement(connection, sql);
  }

  protected abstract int[] executeInternal(
      PreparedStatement preparedStatement, List<PreparedSql> sqls) throws SQLException;

  protected void setupOptions(PreparedStatement preparedStatement) throws SQLException {
    if (query.getQueryTimeout() > 0) {
      preparedStatement.setQueryTimeout(query.getQueryTimeout());
    }
  }

  protected int[] executeBatch(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    int batchSize = query.getBatchSize() > 0 ? query.getBatchSize() : 1;
    int sqlSize = sqls.size();
    int[] updatedRows = new int[sqlSize];
    int i = 0;
    int pos = 0;
    for (PreparedSql sql : sqls) {
      log(sql);
      bindParameters(preparedStatement, sql);
      preparedStatement.addBatch();
      if (i == sqlSize - 1 || (batchSize > 0 && (i + 1) % batchSize == 0)) {
        int position = pos;
        int[] rows =
            statisticManager.executeSql(
                sql,
                () -> {
                  int[] r = executeBatch(preparedStatement, sql);
                  postExecuteBatch(preparedStatement, position, r.length);
                  return r;
                });
        validateRows(preparedStatement, sql, rows);
        System.arraycopy(rows, 0, updatedRows, pos, rows.length);
        pos = i + 1;
      }
      i++;
    }
    return updatedRows;
  }

  protected int[] executeBatch(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    try {
      return preparedStatement.executeBatch();
    } catch (SQLException e) {
      Dialect dialect = query.getConfig().getDialect();
      if (dialect.isUniqueConstraintViolated(e)) {
        throw new BatchUniqueConstraintException(
            query.getConfig().getExceptionSqlLogType(), sql, e);
      }
      throw e;
    }
  }

  /**
   * Invoked after the batch execution.
   *
   * @param preparedStatement the prepared statement
   * @param position the position of the first element in the batch
   * @param size the size of the executed batch
   */
  protected void postExecuteBatch(PreparedStatement preparedStatement, int position, int size) {}

  protected void log(PreparedSql sql) {
    JdbcLogger logger = query.getConfig().getJdbcLogger();
    logger.logSql(query.getClassName(), query.getMethodName(), sql);
  }

  protected void bindParameters(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(query);
    binder.bind(preparedStatement, sql.getParameters());
  }

  protected void validateRows(PreparedStatement preparedStatement, PreparedSql sql, int[] rows)
      throws SQLException {
    Dialect dialect = query.getConfig().getDialect();
    if (dialect.supportsBatchUpdateResults()) {
      if (!query.isOptimisticLockCheckRequired()) {
        return;
      }
      for (int row : rows) {
        if (row != 1) {
          throw new BatchOptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
        }
      }
    } else if (preparedStatement.getUpdateCount() == rows.length) {
      Arrays.fill(rows, 1);
    } else {
      if (!query.isOptimisticLockCheckRequired()) {
        return;
      }
      throw new BatchOptimisticLockException(query.getConfig().getExceptionSqlLogType(), sql);
    }
  }
}
