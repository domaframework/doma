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
import org.seasar.doma.jdbc.query.ProcedureQuery;

/**
 * A command that executes a stored procedure.
 *
 * <p>This command doesn't return any result as procedures typically perform operations without
 * returning values directly. Output parameters can be accessed through the query object.
 */
public class ProcedureCommand extends ModuleCommand<ProcedureQuery, Void> {

  /**
   * Creates a new instance.
   *
   * @param query the query to execute
   */
  public ProcedureCommand(ProcedureQuery query) {
    super(query);
  }

  /**
   * Executes the stored procedure.
   *
   * @param callableStatement the statement to execute
   * @return always null as procedures don't return values directly
   * @throws SQLException if a database access error occurs
   */
  @Override
  protected Void executeInternal(CallableStatement callableStatement) throws SQLException {
    callableStatement.execute();
    fetchParameters(callableStatement);
    return null;
  }
}
