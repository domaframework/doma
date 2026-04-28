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

import org.seasar.doma.jdbc.PreparedSql;

/**
 * A {@link BatchInsertQuery} whose prepared SQL statements are built lazily, one entity at a time,
 * to keep memory usage bounded for very large entity lists.
 *
 * <p>An implementation of this interface signals to {@link
 * org.seasar.doma.jdbc.command.BatchInsertCommand} that the prepared SQLs for the batch must not be
 * materialized all at once. Instead, the command requests one SQL at a time through {@link
 * #buildSql(int)}, binds and adds it to the JDBC batch, and lets the {@link PreparedSql} become
 * eligible for garbage collection before requesting the next.
 */
public interface ChunkedBatchInsertQuery extends BatchInsertQuery {

  /**
   * Returns the total number of entities to be inserted.
   *
   * @return the total number of entities
   */
  int getEntityCount();

  /**
   * Builds the prepared SQL statement for the entity at the given index.
   *
   * <p>Implementations are expected to retain at most one {@link PreparedSql} at a time so that
   * peak memory does not grow with the entity count. The returned object is owned by the caller;
   * subsequent invocations are not required to retain it.
   *
   * @param index the zero-based entity index
   * @return the prepared SQL statement for the entity at {@code index}
   */
  PreparedSql buildSql(int index);
}
