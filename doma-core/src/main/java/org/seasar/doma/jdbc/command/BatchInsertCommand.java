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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.query.BatchInsertQuery;
import org.seasar.doma.jdbc.query.ChunkedBatchInsertQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command to execute a batch insert.
 *
 * <p>This command executes SQL INSERT statements in batch mode and generates IDs for entities if
 * necessary.
 */
public class BatchInsertCommand extends BatchModifyCommand<BatchInsertQuery> {

  /**
   * Creates a new instance.
   *
   * @param query the batch insert query
   */
  public BatchInsertCommand(BatchInsertQuery query) {
    super(query);
  }

  /**
   * Executes the batch insert and generates IDs if necessary.
   *
   * <p>If batch operation is supported, it uses the batch execution mode. Otherwise, it executes
   * each SQL statement individually.
   *
   * @param preparedStatement the prepared statement
   * @param sqls the SQL statements
   * @return the array of inserted rows count
   * @throws SQLException if a database access error occurs
   */
  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    if (query instanceof ChunkedBatchInsertQuery chunked) {
      return executeChunked(preparedStatement, chunked);
    }
    if (query.isBatchSupported()) {
      return executeBatch(preparedStatement, sqls);
    }
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    int sqlSize = sqls.size();
    int[] updatedRows = new int[sqlSize];
    int i = 0;
    for (PreparedSql sql : sqls) {
      log(sql);
      bindParameters(preparedStatement, sql);
      int index = i;
      updatedRows[i] =
          statisticManager.executeSql(
              sql,
              () -> {
                int rows = executeUpdate(preparedStatement, sql);
                query.generateId(preparedStatement, index);
                return rows;
              });
      i++;
    }
    return updatedRows;
  }

  /**
   * Executes the insert in chunks of {@code batchSize} entities, asking the query to build the
   * prepared SQL for one entity at a time so that at most one {@link PreparedSql} is alive between
   * the time the command binds it and the time the next one is built.
   *
   * @param preparedStatement the prepared statement
   * @param chunked the chunked batch insert query
   * @return the array of inserted rows count
   * @throws SQLException if a database access error occurs
   */
  protected int[] executeChunked(
      PreparedStatement preparedStatement, ChunkedBatchInsertQuery chunked) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    int batchSize = query.getBatchSize() > 0 ? query.getBatchSize() : 1;
    int totalSize = chunked.getEntityCount();
    int[] updatedRows = new int[totalSize];
    boolean batchSupported = query.isBatchSupported();
    for (int from = 0; from < totalSize; from += batchSize) {
      int to = Math.min(from + batchSize, totalSize);
      if (batchSupported) {
        PreparedSql lastSql = null;
        for (int i = from; i < to; i++) {
          PreparedSql sql = chunked.buildSql(i);
          log(sql);
          bindParameters(preparedStatement, sql);
          preparedStatement.addBatch();
          lastSql = sql;
        }
        int chunkPosition = from;
        PreparedSql sqlForExecute = lastSql;
        int[] rows =
            statisticManager.executeSql(
                sqlForExecute,
                () -> {
                  int[] r = executeBatch(preparedStatement, sqlForExecute);
                  postExecuteBatch(preparedStatement, chunkPosition, r.length);
                  return r;
                });
        validateRows(preparedStatement, sqlForExecute, rows);
        System.arraycopy(rows, 0, updatedRows, from, rows.length);
      } else {
        for (int i = from; i < to; i++) {
          PreparedSql sql = chunked.buildSql(i);
          log(sql);
          bindParameters(preparedStatement, sql);
          int index = i;
          updatedRows[i] =
              statisticManager.executeSql(
                  sql,
                  () -> {
                    int rows = executeUpdate(preparedStatement, sql);
                    query.generateId(preparedStatement, index);
                    return rows;
                  });
        }
      }
    }
    return updatedRows;
  }

  /**
   * Executes a single update operation.
   *
   * @param preparedStatement the prepared statement
   * @param sql the SQL statement
   * @return the number of affected rows
   * @throws SQLException if a database access error occurs
   * @throws BatchUniqueConstraintException if a unique constraint violation occurs
   */
  protected int executeUpdate(PreparedStatement preparedStatement, PreparedSql sql)
      throws SQLException {
    try {
      return preparedStatement.executeUpdate();
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
   * Generates IDs for the batch of entities after execution.
   *
   * @param preparedStatement the prepared statement
   * @param position the position of the first element in the batch
   * @param size the size of the executed batch
   */
  @Override
  protected void postExecuteBatch(PreparedStatement preparedStatement, int position, int size) {
    query.generateIds(preparedStatement, position, size);
  }
}
