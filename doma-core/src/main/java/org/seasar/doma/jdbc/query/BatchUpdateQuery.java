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
 * An interface for batch UPDATE queries.
 *
 * <p>This interface represents a query that performs batch UPDATE operations. It extends {@link
 * BatchModifyQuery} to inherit common batch data modification functionality while specializing for
 * UPDATE operations.
 *
 * <p>Implementations of this interface handle the execution of multiple UPDATE statements as a
 * single batch operation for improved performance, and provide methods for handling optimistic
 * concurrency control through version numbers.
 */
public interface BatchUpdateQuery extends BatchModifyQuery {

  /**
   * Increments the version numbers for all entities in the batch.
   *
   * <p>This method is called after executing the batch to update the version numbers in the entity
   * objects, ensuring they match the values in the database after the update. This is important for
   * optimistic concurrency control.
   */
  void incrementVersions();
}
