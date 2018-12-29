package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

public class OptionalIntSingleResultParameter
    extends ScalarSingleResultParameter<Integer, OptionalInt> {

  public OptionalIntSingleResultParameter() {
    super(new OptionalIntScalar());
  }
}
