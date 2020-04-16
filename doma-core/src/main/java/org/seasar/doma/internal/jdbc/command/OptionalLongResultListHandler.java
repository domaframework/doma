package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongResultListHandler extends ScalarResultListHandler<Long, OptionalLong> {

  public OptionalLongResultListHandler() {
    super(() -> new OptionalLongScalar());
  }
}
