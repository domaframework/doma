package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalLong;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongCollectorHandler<RESULT>
    extends ScalarCollectorHandler<Long, OptionalLong, RESULT> {

  public OptionalLongCollectorHandler(Collector<OptionalLong, ?, RESULT> collector) {
    super(OptionalLongScalar::new, collector);
  }
}
