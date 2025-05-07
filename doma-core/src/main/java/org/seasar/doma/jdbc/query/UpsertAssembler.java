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
 * An interface for assembling upsert queries.
 *
 * <p>Upsert is a database operation that inserts a row if it doesn't exist, or updates it if it
 * does. This interface is implemented for each database dialect to handle dialect-specific upsert
 * syntax.
 *
 * <p>Implementations of this interface are responsible for generating the SQL for upsert
 * operations, including handling the specific syntax required by different database systems.
 */
public interface UpsertAssembler {
  /**
   * Assembles the upsert query.
   *
   * <p>This method generates the SQL for the upsert operation according to the specific database
   * dialect's syntax requirements.
   */
  void assemble();
}
