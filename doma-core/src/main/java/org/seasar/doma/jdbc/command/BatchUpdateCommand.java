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
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.BatchUpdateQuery;

/**
 * A command to execute a batch update.
 *
 * <p>This command executes SQL UPDATE statements in batch mode and increments version numbers if
 * entities have optimistic lock control.
 */
public class BatchUpdateCommand extends BatchModifyCommand<BatchUpdateQuery> {

  /**
   * Creates a new instance.
   *
   * @param query the batch update query
   */
  public BatchUpdateCommand(BatchUpdateQuery query) {
    super(query);
  }

  /**
   * Executes the batch update and increments version numbers.
   *
   * @param preparedStatement the prepared statement
   * @param sqls the SQL statements
   * @return the array of updated rows count
   * @throws SQLException if a database access error occurs
   */
  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    int[] rows = executeBatch(preparedStatement, sqls);
    query.incrementVersions();
    return rows;
  }
}
