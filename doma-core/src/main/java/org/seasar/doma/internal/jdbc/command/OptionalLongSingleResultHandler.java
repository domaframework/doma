package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongSingleResultHandler extends ScalarSingleResultHandler<Long, OptionalLong> {

  public OptionalLongSingleResultHandler() {
    super(OptionalLongScalar::new);
  }
}
