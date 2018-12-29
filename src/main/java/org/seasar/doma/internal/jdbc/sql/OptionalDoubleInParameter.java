package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

public class OptionalDoubleInParameter extends ScalarInParameter<Double, OptionalDouble> {

  public OptionalDoubleInParameter(OptionalDouble value) {
    super(new OptionalDoubleScalar(), value);
  }
}
