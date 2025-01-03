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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents that the implementation can fetch data from database as a stream and map it to
 * something.
 *
 * @param <ELEMENT> the element type of the stream
 */
public interface StreamMappable<ELEMENT> extends Listable<ELEMENT> {

  /**
   * Open a stream.
   *
   * <p>You must close the stream after using it.
   *
   * @return the opened stream
   */
  Stream<ELEMENT> openStream();

  /**
   * Map a stream.
   *
   * @param streamMapper the mapper
   * @param <RESULT> the mapped result
   * @return the mapped result
   */
  <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper);

  /**
   * Collect a stream.
   *
   * @param collector the collector
   * @param <RESULT> the collected result
   * @return the collected result
   */
  default <RESULT> RESULT collect(Collector<ELEMENT, ?, RESULT> collector) {
    return mapStream(s -> s.collect(collector));
  }

  @SuppressWarnings("EmptyMethod")
  @Override
  StreamMappable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
