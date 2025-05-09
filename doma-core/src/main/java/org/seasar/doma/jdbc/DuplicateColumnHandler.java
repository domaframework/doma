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
package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.query.Query;

/**
 * A handler for columns that are duplicated in a result set.
 *
 * <p>This interface defines how to handle situations where the same column name appears multiple
 * times in a result set. Implementations can choose to ignore duplicates, throw exceptions, or
 * handle them in custom ways.
 *
 * <p>The default implementation does nothing, effectively ignoring duplicate columns.
 *
 * @see ThrowingDuplicateColumnHandler
 * @see DuplicateColumnException
 */
public interface DuplicateColumnHandler {

  /**
   * Handles a duplicate column found in a result set.
   *
   * @param query the query being executed
   * @param duplicateColumnName the name of the duplicate column
   * @throws DuplicateColumnException if the implementation chooses to reject duplicate columns
   */
  default void handle(Query query, String duplicateColumnName) {}
}
