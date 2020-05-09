package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

public interface StreamMappable<ELEMENT> extends Listable<ELEMENT> {

  <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper);

  default <RESULT> RESULT collect(Collector<ELEMENT, ?, RESULT> collector) {
    return mapStream(s -> s.collect(collector));
  }

  @Override
  StreamMappable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
