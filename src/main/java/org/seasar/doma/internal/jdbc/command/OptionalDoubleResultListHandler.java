package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

public class OptionalDoubleResultListHandler
    extends ScalarResultListHandler<Double, OptionalDouble> {

  public OptionalDoubleResultListHandler() {
    super(() -> new OptionalDoubleScalar());
  }
}
