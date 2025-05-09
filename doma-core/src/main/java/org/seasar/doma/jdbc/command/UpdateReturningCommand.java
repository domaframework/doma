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
import java.util.function.Supplier;
import org.seasar.doma.jdbc.query.UpdateQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

/**
 * A command that executes an SQL UPDATE statement and retrieves a result set generated from a
 * RETURNING clause.
 *
 * <p>This command extends {@link ModifyReturningCommand} to provide specific functionality for
 * executing UPDATE operations that return data via a RETURNING clause. This is useful for
 * retrieving auto-generated values or other column values after an update operation.
 *
 * <p>The RETURNING clause is a SQL feature supported by some database systems (like PostgreSQL,
 * Oracle) that allows retrieving data from modified rows as part of the same statement that
 * performs the modification, without requiring a separate SELECT statement.
 *
 * @param <RESULT> the type of result returned from the RETURNING clause
 */
public class UpdateReturningCommand<RESULT> extends ModifyReturningCommand<UpdateQuery, RESULT> {

  /**
   * Creates an instance of the UpdateReturningCommand.
   *
   * @param query the update query to execute, containing the SQL statement with a RETURNING clause
   * @param resultSetHandler the handler for processing the result set returned by the RETURNING
   *     clause
   * @param emptyResultSupplier the supplier that provides an empty result when no rows are returned
   *     or when the RETURNING clause is not supported by the database
   */
  public UpdateReturningCommand(
      UpdateQuery query,
      ResultSetHandler<RESULT> resultSetHandler,
      Supplier<RESULT> emptyResultSupplier) {
    super(query, resultSetHandler, emptyResultSupplier);
  }

  /**
   * Executes the SQL UPDATE statement with a RETURNING clause and processes the returned result
   * set.
   *
   * <p>This method uses the {@link StatisticManager} to track execution statistics and delegates to
   * the {@code executeQuery} method to execute the prepared statement. Unlike regular UPDATE
   * commands that return the number of affected rows, this method returns data from the RETURNING
   * clause.
   *
   * @param preparedStatement the prepared statement containing the SQL UPDATE with RETURNING clause
   * @return the result processed from the result set returned by the RETURNING clause
   * @throws SQLException if a database access error occurs during execution
   */
  @Override
  protected RESULT executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    return statisticManager.executeSql(query.getSql(), () -> executeQuery(preparedStatement));
  }
}
