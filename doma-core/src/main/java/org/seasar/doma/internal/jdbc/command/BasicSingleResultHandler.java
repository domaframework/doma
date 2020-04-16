package org.seasar.doma.internal.jdbc.command;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicSingleResultHandler<BASIC> extends ScalarSingleResultHandler<BASIC, BASIC> {

  public BasicSingleResultHandler(Supplier<Wrapper<BASIC>> supplier) {
    super(() -> new BasicScalar<>(supplier));
  }
}
