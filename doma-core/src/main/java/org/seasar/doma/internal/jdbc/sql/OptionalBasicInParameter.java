package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicInParameter<BASIC> extends ScalarInParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicInParameter(Supplier<Wrapper<BASIC>> supplier, Optional<BASIC> value) {
    super(new OptionalBasicScalar<>(supplier), value);
  }
}
