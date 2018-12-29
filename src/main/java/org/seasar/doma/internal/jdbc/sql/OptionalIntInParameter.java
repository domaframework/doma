package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

/** @author taedium */
public class OptionalIntInParameter extends ScalarInParameter<Integer, OptionalInt> {

  public OptionalIntInParameter(OptionalInt value) {
    super(new OptionalIntScalar(), value);
  }
}
