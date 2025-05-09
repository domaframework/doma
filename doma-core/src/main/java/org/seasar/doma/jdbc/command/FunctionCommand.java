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

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.FunctionQuery;

/**
 * A command that executes a database function.
 *
 * <p>This command returns the result of the function execution. The result is obtained from the
 * query object after executing the function and fetching output parameters.
 *
 * @param <RESULT> the result type
 */
public class FunctionCommand<RESULT> extends ModuleCommand<FunctionQuery<RESULT>, RESULT> {

  /**
   * Creates a new instance.
   *
   * @param query the query to execute
   */
  public FunctionCommand(FunctionQuery<RESULT> query) {
    super(query);
  }

  /**
   * Executes the database function.
   *
   * @param callableStatement the statement to execute
   * @return the result of the function execution
   * @throws SQLException if a database access error occurs
   */
  @Override
  protected RESULT executeInternal(CallableStatement callableStatement) throws SQLException {
    callableStatement.execute();
    fetchParameters(callableStatement);
    return query.getResult();
  }
}
