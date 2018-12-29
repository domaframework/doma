package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <BASIC>
 */
public class BasicCollectorHandler<BASIC, RESULT>
    extends ScalarCollectorHandler<BASIC, BASIC, RESULT> {

  public BasicCollectorHandler(
      Supplier<Wrapper<BASIC>> supplier, Collector<BASIC, ?, RESULT> collector) {
    super(() -> new BasicScalar<>(supplier, false), collector);
  }
}
