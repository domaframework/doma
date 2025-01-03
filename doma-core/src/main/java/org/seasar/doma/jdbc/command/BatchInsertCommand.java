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
import org.seasar.doma.jdbc.statistic.StatisticManager;

public class BatchInsertCommand extends BatchModifyCommand<BatchInsertQuery> {

  public BatchInsertCommand(BatchInsertQuery query) {
    super(query);
  }

  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
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

  @Override
  protected void postExecuteBatch(PreparedStatement preparedStatement, int position, int size) {
    query.generateIds(preparedStatement, position, size);
  }
}
