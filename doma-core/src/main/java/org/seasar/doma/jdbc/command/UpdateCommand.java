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
import org.seasar.doma.jdbc.query.UpdateQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command that executes UPDATE SQL statements.
 *
 * <p>This class extends {@link ModifyCommand} to provide specific functionality for executing
 * UPDATE operations in a database. It handles the execution of prepared statements for updating
 * data and tracks execution statistics.
 *
 * <p>The command returns the number of rows affected by the UPDATE operation. It also increments
 * the version number for entities that use optimistic locking.
 */
public class UpdateCommand extends ModifyCommand<UpdateQuery> {

  /**
   * Constructs a new UpdateCommand with the specified query.
   *
   * @param query the update query to execute, which contains the SQL statement and configuration
   */
  public UpdateCommand(UpdateQuery query) {
    super(query);
  }

  /**
   * Executes the UPDATE statement and returns the number of affected rows.
   *
   * <p>This method uses the statistic manager to track execution statistics for the SQL operation
   * if statistics collection is enabled. It also increments the version number for entities that
   * use optimistic locking after the update operation is completed.
   *
   * @param preparedStatement the prepared statement to execute
   * @return the number of rows affected by the UPDATE operation
   * @throws SQLException if a database access error occurs during execution
   */
  @Override
  protected int executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    int rows = statisticManager.executeSql(query.getSql(), () -> executeUpdate(preparedStatement));
    query.incrementVersion();
    return rows;
  }
}
