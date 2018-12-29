package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <BASIC>
 */
public class OptionalBasicCollectorHandler<BASIC, RESULT>
    extends ScalarCollectorHandler<BASIC, Optional<BASIC>, RESULT> {

  public OptionalBasicCollectorHandler(
      Supplier<Wrapper<BASIC>> supplier, Collector<Optional<BASIC>, ?, RESULT> collector) {
    super(() -> new OptionalBasicScalar<>(supplier), collector);
  }
}
