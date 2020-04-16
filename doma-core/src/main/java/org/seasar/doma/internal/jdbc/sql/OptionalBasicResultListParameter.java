package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicResultListParameter<BASIC>
    extends ScalarResultListParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicResultListParameter(Supplier<Wrapper<BASIC>> supplier) {
    super(() -> new OptionalBasicScalar<>(supplier));
  }
}
