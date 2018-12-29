package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

/** @author taedium */
public class OptionalDoubleListParameter extends ScalarListParameter<Double, OptionalDouble> {

  public OptionalDoubleListParameter(List<OptionalDouble> list, String name) {
    super(() -> new OptionalDoubleScalar(), list, name);
  }
}
