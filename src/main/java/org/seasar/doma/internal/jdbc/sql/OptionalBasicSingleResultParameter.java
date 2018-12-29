package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class OptionalBasicSingleResultParameter<BASIC>
    extends ScalarSingleResultParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicSingleResultParameter(Supplier<Wrapper<BASIC>> supplier) {
    super(new OptionalBasicScalar<>(supplier));
    assertNotNull(supplier);
  }
}
