package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicListParameter<BASIC> extends ScalarListParameter<BASIC, Optional<BASIC>> {

  public OptionalBasicListParameter(
      Supplier<Wrapper<BASIC>> supplier, List<Optional<BASIC>> list, String name) {
    super(() -> new OptionalBasicScalar<>(supplier), list, name);
  }
}
