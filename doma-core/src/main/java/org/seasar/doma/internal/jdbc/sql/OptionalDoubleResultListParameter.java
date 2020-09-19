package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

public class OptionalDoubleResultListParameter
    extends ScalarResultListParameter<Double, OptionalDouble> {

  public OptionalDoubleResultListParameter() {
    super(OptionalDoubleScalar::new);
  }
}
