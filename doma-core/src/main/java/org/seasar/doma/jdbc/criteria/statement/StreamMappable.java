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

  @Override
  StreamMappable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
