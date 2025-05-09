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
import org.seasar.doma.jdbc.query.InsertQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command that executes INSERT SQL statements.
 *
 * <p>This class extends {@link ModifyCommand} to provide specific functionality for executing
 * INSERT operations in a database. It handles the execution of prepared statements for inserting
 * data and tracks execution statistics.
 *
 * <p>The command returns the number of rows affected by the INSERT operation. It also handles the
 * generation of auto-generated keys (such as identity columns) and assigns them back to the entity
 * being inserted.
 */
public class InsertCommand extends ModifyCommand<InsertQuery> {

  /**
   * Constructs a new InsertCommand with the specified query.
   *
   * @param query the insert query to execute, which contains the SQL statement and configuration
   */
  public InsertCommand(InsertQuery query) {
    super(query);
  }

  /**
   * Executes the INSERT statement and returns the number of affected rows.
   *
   * <p>This method uses the statistic manager to track execution statistics for the SQL operation
   * if statistics collection is enabled. It also handles the generation of auto-generated keys
   * after the insert operation is completed.
   *
   * @param preparedStatement the prepared statement to execute
   * @return the number of rows affected by the INSERT operation
   * @throws SQLException if a database access error occurs during execution
   */
  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    return statisticManager.executeSql(
        query.getSql(),
        () -> {
          int rows = executeUpdate(preparedStatement);
          query.generateId(preparedStatement);
          return rows;
        });
  }
}
