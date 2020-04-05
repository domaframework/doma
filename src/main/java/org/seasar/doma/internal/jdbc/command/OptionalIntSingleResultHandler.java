package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

public class OptionalIntSingleResultHandler
    extends ScalarSingleResultHandler<Integer, OptionalInt> {

  public OptionalIntSingleResultHandler() {
    super(() -> new OptionalIntScalar());
  }
}
