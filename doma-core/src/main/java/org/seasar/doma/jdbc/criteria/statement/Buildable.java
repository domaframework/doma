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
package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents that the implementation can build an SQL.
 *
 * @param <BUILDABLE> the subtype
 */
public interface Buildable<BUILDABLE extends Buildable<BUILDABLE>> {

  /**
   * Returns the built SQL.
   *
   * @return the built sql
   */
  Sql<?> asSql();

  /**
   * Peeks the built SQL.
   *
   * @param consumer the SQL handler
   * @return this instance
   */
  @SuppressWarnings("unchecked")
  default BUILDABLE peek(Consumer<Sql<?>> consumer) {
    Objects.requireNonNull(consumer);
    consumer.accept(asSql());
    return (BUILDABLE) this;
  }
}
