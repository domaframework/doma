package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalInt;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

/** @author nakamura-to */
public class OptionalIntResultListHandler extends ScalarResultListHandler<Integer, OptionalInt> {

  public OptionalIntResultListHandler() {
    super(() -> new OptionalIntScalar());
  }
}
