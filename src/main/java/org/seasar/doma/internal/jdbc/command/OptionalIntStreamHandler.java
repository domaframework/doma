package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.OptionalIntScalar;

/**
 * @author nakamura-to
 * @param <RESULT>
 */
public class OptionalIntStreamHandler<RESULT>
    extends ScalarStreamHandler<Integer, OptionalInt, RESULT> {

  public OptionalIntStreamHandler(Function<Stream<OptionalInt>, RESULT> mapper) {
    super(() -> new OptionalIntScalar(), mapper);
  }
}
