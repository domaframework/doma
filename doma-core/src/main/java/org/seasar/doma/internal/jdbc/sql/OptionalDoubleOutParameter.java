package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;
import org.seasar.doma.jdbc.Reference;

public class OptionalDoubleOutParameter extends ScalarOutParameter<Double, OptionalDouble> {

  public OptionalDoubleOutParameter(Reference<OptionalDouble> reference) {
    super(new OptionalDoubleScalar(), reference);
  }
}
