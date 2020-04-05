package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicOutParameter<BASIC> extends ScalarOutParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicOutParameter(
      Supplier<Wrapper<BASIC>> supplier, Reference<Optional<BASIC>> reference) {
    super(new OptionalBasicScalar<BASIC>(supplier), reference);
  }
}
