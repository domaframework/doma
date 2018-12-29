package org.seasar.doma.internal.jdbc.sql;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicResultListParameter<BASIC> extends ScalarResultListParameter<BASIC, BASIC> {

  public BasicResultListParameter(Supplier<Wrapper<BASIC>> supplier) {
    super(() -> new BasicScalar<>(supplier, false));
  }
}
