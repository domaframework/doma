package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicSingleResultHandler<BASIC>
    extends ScalarSingleResultHandler<BASIC, Optional<BASIC>> {

  public OptionalBasicSingleResultHandler(Supplier<Wrapper<BASIC>> supplier, boolean primitive) {
    super(() -> new OptionalBasicScalar<>(supplier));
    assertNotNull(supplier);
  }
}
