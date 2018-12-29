package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @param <BASIC>
 */
public class OptionalBasicResultListHandler<BASIC>
    extends ScalarResultListHandler<BASIC, Optional<BASIC>> {

  public OptionalBasicResultListHandler(Supplier<Wrapper<BASIC>> supplier) {
    super(() -> new OptionalBasicScalar<>(supplier));
    assertNotNull(supplier);
  }
}
