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

import java.util.Optional;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

public interface Singular<ELEMENT> extends Statement<ELEMENT> {

  /**
   * Returns the element of data as an optional.
   *
   * @return the element of data
   */
  default Optional<ELEMENT> fetchOptional() {
    ELEMENT element = execute();
    return Optional.ofNullable(element);
  }

  /**
   * Returns the element of data.
   *
   * @return the element of data
   */
  default ELEMENT fetchOne() {
    return execute();
  }

  @Override
  Singular<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
