package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;
import org.seasar.doma.jdbc.Reference;

/** @author taedium */
public class OptionalIntOutParameter extends ScalarOutParameter<Integer, OptionalInt> {

  public OptionalIntOutParameter(Reference<OptionalInt> reference) {
    super(new OptionalIntScalar(), reference);
  }
}
