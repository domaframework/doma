package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;
import org.seasar.doma.jdbc.Reference;

public class OptionalLongInOutParameter extends ScalarInOutParameter<Long, OptionalLong> {

  public OptionalLongInOutParameter(Reference<OptionalLong> reference) {
    super(new OptionalLongScalar(), reference);
  }
}
