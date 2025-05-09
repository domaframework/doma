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

import org.seasar.doma.jdbc.query.Query;

/**
 * A core interface for executing database operations in the Doma framework.
 *
 * <p>This interface represents a command pattern implementation for database access. Each command
 * encapsulates a specific database operation (such as select, insert, update, delete) along with
 * its execution logic. Commands are responsible for executing queries and processing their results.
 *
 * <p>The Command interface is designed to be extensible, with various implementations for different
 * types of database operations. Each implementation handles the specific details of executing its
 * associated query type and processing the results.
 *
 * @param <RESULT> the type of result that this command produces when executed
 */
public interface Command<RESULT> {

  /**
   * Returns the query associated with this command.
   *
   * <p>The query contains the SQL statement and parameters that will be executed by this command.
   *
   * @return the query object that this command will execute
   */
  Query getQuery();

  /**
   * Executes the command and returns the result.
   *
   * <p>This method is responsible for executing the database operation encapsulated by this
   * command. It handles the execution of the SQL statement, processing of results, and any
   * necessary resource management.
   *
   * @return the result of executing the command, the type depends on the specific command
   *     implementation
   */
  RESULT execute();
}
