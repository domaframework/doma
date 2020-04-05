package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongInParameter extends ScalarInParameter<Long, OptionalLong> {

  public OptionalLongInParameter(OptionalLong value) {
    super(new OptionalLongScalar(), value);
  }
}
