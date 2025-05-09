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
import org.seasar.doma.jdbc.query.BatchDeleteQuery;

/**
 * A command to execute a batch delete.
 *
 * <p>This command executes SQL DELETE statements in batch mode.
 */
public class BatchDeleteCommand extends BatchModifyCommand<BatchDeleteQuery> {

  /**
   * Creates a new instance.
   *
   * @param query the batch delete query
   */
  public BatchDeleteCommand(BatchDeleteQuery query) {
    super(query);
  }

  /**
   * Executes the batch delete.
   *
   * @param preparedStatement the prepared statement
   * @param sqls the SQL statements
   * @return the array of deleted rows count
   * @throws SQLException if a database access error occurs
   */
  @Override
  protected int[] executeInternal(PreparedStatement preparedStatement, List<PreparedSql> sqls)
      throws SQLException {
    return executeBatch(preparedStatement, sqls);
  }
}
