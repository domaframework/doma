package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicInOutParameter<BASIC>
    extends ScalarInOutParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicInOutParameter(
      Supplier<Wrapper<BASIC>> supplier, Reference<Optional<BASIC>> reference) {
    super(new OptionalBasicScalar<>(supplier), reference);
  }
}
