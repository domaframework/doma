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

public interface BatchInsertQuery extends BatchModifyQuery {

  boolean isBatchSupported();

  void generateId(Statement statement, int index);

  /**
   * Generates IDs for the batch.
   *
   * @param statement the statement
   * @param position the position of the first element in the batch
   * @param size the size of the executed batch
   */
  default void generateIds(Statement statement, int position, int size) {}
}
