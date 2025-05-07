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
 * An interface for batch INSERT queries.
 *
 * <p>This interface represents a query that performs batch INSERT operations. It extends {@link
 * BatchModifyQuery} to inherit common batch data modification functionality while specializing for
 * INSERT operations.
 *
 * <p>Implementations of this interface handle the execution of multiple INSERT statements as a
 * single batch operation for improved performance, and provide methods for generating IDs for
 * inserted rows.
 */
public interface BatchInsertQuery extends BatchModifyQuery {

  /**
   * Determines if batch operations are supported for this query.
   *
   * <p>Some database operations or configurations may not support batch processing. This method
   * allows implementations to indicate whether batch operations can be used.
   *
   * @return true if batch operations are supported, false otherwise
   */
  boolean isBatchSupported();

  /**
   * Generates an ID for a single inserted row in the batch.
   *
   * <p>This method is called after executing the batch to generate IDs for rows that use
   * auto-generated keys or sequences.
   *
   * @param statement the statement used for the batch operation
   * @param index the index of the element in the batch
   */
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
