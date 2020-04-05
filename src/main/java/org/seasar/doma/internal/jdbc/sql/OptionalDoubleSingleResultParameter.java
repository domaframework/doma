package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

public class OptionalDoubleSingleResultParameter
    extends ScalarSingleResultParameter<Double, OptionalDouble> {

  public OptionalDoubleSingleResultParameter() {
    super(new OptionalDoubleScalar());
  }
}
