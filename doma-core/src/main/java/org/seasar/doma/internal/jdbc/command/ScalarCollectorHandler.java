package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.Scalar;

public class ScalarCollectorHandler<BASIC, CONTAINER, RESULT>
    extends AbstractCollectorHandler<CONTAINER, RESULT> {

  public ScalarCollectorHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier, Collector<CONTAINER, ?, RESULT> collector) {
    super(new ScalarStreamHandler<>(supplier, s -> s.collect(collector)));
  }
}
