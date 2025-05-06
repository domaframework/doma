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

import java.sql.Statement;

/**
 * An interface for INSERT queries.
 *
 * <p>This interface represents a query that performs INSERT operations. It extends {@link
 * ModifyQuery} to inherit common data modification functionality while specializing for INSERT
 * operations.
 *
 * <p>Implementations of this interface handle the execution of INSERT statements, including the
 * construction of the column list, values clause, and generation of auto-generated keys or sequence
 * values.
 */
public interface InsertQuery extends ModifyQuery {

  /**
   * Generates an ID for the inserted row.
   *
   * <p>This method is called after executing the INSERT statement to retrieve and set
   * auto-generated keys or sequence values for the entity. It's typically used for primary key
   * generation strategies like identity columns or sequences.
   *
   * @param statement the statement used for the INSERT operation
   */
  void generateId(Statement statement);
}
