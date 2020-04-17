package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicListParameter<BASIC> extends ScalarListParameter<BASIC, BASIC> {

  public BasicListParameter(Supplier<Wrapper<BASIC>> supplier, List<BASIC> list, String name) {
    super(() -> new BasicScalar<>(supplier), list, name);
  }
}
