package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongResultListParameter extends ScalarResultListParameter<Long, OptionalLong> {

  public OptionalLongResultListParameter() {
    super(() -> new OptionalLongScalar());
  }
}
