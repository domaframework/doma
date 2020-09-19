package org.seasar.doma.internal.jdbc.sql;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicInParameter<BASIC> extends ScalarInParameter<BASIC, BASIC> {

  public BasicInParameter(Supplier<Wrapper<BASIC>> supplier) {
    super(new BasicScalar<>(supplier));
  }

  public BasicInParameter(Supplier<Wrapper<BASIC>> supplier, BASIC value) {
    super(new BasicScalar<>(supplier), value);
  }
}
