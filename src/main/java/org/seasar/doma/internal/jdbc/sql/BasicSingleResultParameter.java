package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicSingleResultParameter<BASIC> extends ScalarSingleResultParameter<BASIC, BASIC> {

  public BasicSingleResultParameter(Supplier<Wrapper<BASIC>> supplier, boolean primitive) {
    super(new BasicScalar<>(supplier, primitive));
    assertNotNull(supplier);
  }
}
