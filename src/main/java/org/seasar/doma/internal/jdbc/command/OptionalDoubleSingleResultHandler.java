package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalDouble;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

/** @author nakamura-to */
public class OptionalDoubleSingleResultHandler
    extends ScalarSingleResultHandler<Double, OptionalDouble> {

  public OptionalDoubleSingleResultHandler() {
    super(() -> new OptionalDoubleScalar());
  }
}
