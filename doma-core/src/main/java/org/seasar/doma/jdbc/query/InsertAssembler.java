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
package org.seasar.doma.jdbc.query;

/**
 * An interface for assembling INSERT queries.
 *
 * <p>This interface is responsible for generating the SQL for INSERT operations. Implementations of
 * this interface handle the construction of INSERT statements, including the column list, values
 * clause, and any other necessary SQL components.
 */
public interface InsertAssembler {
  /**
   * Assembles the INSERT query.
   *
   * <p>This method generates the SQL for the INSERT operation, including the table name, column
   * list, values clause, and any other required SQL components.
   */
  void assemble();
}
