package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalInt;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

public class OptionalIntCollectorHandler<RESULT>
    extends ScalarCollectorHandler<Integer, OptionalInt, RESULT> {

  public OptionalIntCollectorHandler(Collector<OptionalInt, ?, RESULT> mapper) {
    super(() -> new OptionalIntScalar(), mapper);
  }
}
