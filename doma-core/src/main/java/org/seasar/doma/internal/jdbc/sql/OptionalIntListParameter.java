package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

public class OptionalIntListParameter extends ScalarListParameter<Integer, OptionalInt> {

  public OptionalIntListParameter(List<OptionalInt> list, String name) {
    super(OptionalIntScalar::new, list, name);
  }
}
