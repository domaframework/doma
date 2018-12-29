package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

/** @author taedium */
public class OptionalIntResultListParameter
    extends ScalarResultListParameter<Integer, OptionalInt> {

  public OptionalIntResultListParameter() {
    super(() -> new OptionalIntScalar());
  }
}
