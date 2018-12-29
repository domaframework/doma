package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;
import org.seasar.doma.jdbc.Reference;

/** @author taedium */
public class OptionalIntInOutParameter extends ScalarInOutParameter<Integer, OptionalInt> {

  public OptionalIntInOutParameter(Reference<OptionalInt> reference) {
    super(new OptionalIntScalar(), reference);
  }
}
