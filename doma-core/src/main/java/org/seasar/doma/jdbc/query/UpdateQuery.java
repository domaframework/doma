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
 * An interface for UPDATE queries.
 *
 * <p>This interface represents a query that performs UPDATE operations. It extends {@link
 * ModifyQuery} to inherit common data modification functionality while specializing for UPDATE
 * operations.
 *
 * <p>Implementations of this interface handle the execution of UPDATE statements, including the
 * construction of the SET clause, WHERE clause, and handling of optimistic concurrency control
 * through version numbers.
 */
public interface UpdateQuery extends ModifyQuery {

  /**
   * Increments the version number for the entity being updated.
   *
   * <p>This method is called after executing the UPDATE statement to update the version number in
   * the entity object, ensuring it matches the value in the database after the update. This is
   * important for optimistic concurrency control.
   */
  void incrementVersion();
}
