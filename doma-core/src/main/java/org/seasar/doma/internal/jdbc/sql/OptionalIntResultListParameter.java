package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

public class OptionalIntResultListParameter
    extends ScalarResultListParameter<Integer, OptionalInt> {

  public OptionalIntResultListParameter() {
    super(OptionalIntScalar::new);
  }
}
