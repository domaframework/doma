package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalDouble;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

public class OptionalDoubleCollectorHandler<RESULT>
    extends ScalarCollectorHandler<Double, OptionalDouble, RESULT> {

  public OptionalDoubleCollectorHandler(Collector<OptionalDouble, ?, RESULT> collector) {
    super(OptionalDoubleScalar::new, collector);
  }
}
