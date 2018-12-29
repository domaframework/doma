package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

/** @author taedium */
public class OptionalIntListParameter extends ScalarListParameter<Integer, OptionalInt> {

  public OptionalIntListParameter(List<OptionalInt> list, String name) {
    super(() -> new OptionalIntScalar(), list, name);
  }
}
