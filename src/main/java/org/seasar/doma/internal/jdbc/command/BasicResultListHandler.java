package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicResultListHandler<BASIC> extends ScalarResultListHandler<BASIC, BASIC> {

  public BasicResultListHandler(Supplier<Wrapper<BASIC>> supplier) {
    super(() -> new BasicScalar<>(supplier, false));
    assertNotNull(supplier);
  }
}
