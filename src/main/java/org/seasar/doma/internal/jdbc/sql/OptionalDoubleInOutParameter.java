package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;
import org.seasar.doma.jdbc.Reference;

/** @author taedium */
public class OptionalDoubleInOutParameter extends ScalarInOutParameter<Double, OptionalDouble> {

  public OptionalDoubleInOutParameter(Reference<OptionalDouble> reference) {
    super(new OptionalDoubleScalar(), reference);
  }
}
