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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents that the implementation can fetch data from database and list them.
 *
 * @param <ELEMENT> the listable element type
 */
public interface Listable<ELEMENT> extends Statement<List<ELEMENT>> {

  /**
   * Returns data as a list.
   *
   * @return data
   */
  default List<ELEMENT> fetch() {
    return execute();
  }

  /**
   * Returns data as a stream.
   *
   * @return data
   */
  default Stream<ELEMENT> stream() {
    return execute().stream();
  }

  /**
   * Returns the first element of data as an optional.
   *
   * @return the first element of data
   */
  default Optional<ELEMENT> fetchOptional() {
    Iterator<ELEMENT> iterator = stream().iterator();
    if (iterator.hasNext()) {
      return Optional.ofNullable(iterator.next());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the first element of data.
   *
   * @return the first element of data
   */
  default ELEMENT fetchOne() {
    return fetchOptional().orElse(null);
  }

  @Override
  Listable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
