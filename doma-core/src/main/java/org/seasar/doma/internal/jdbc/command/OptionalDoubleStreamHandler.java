package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.OptionalDoubleScalar;

/**
 * @author nakamura-to
 * @param <RESULT>
 */
public class OptionalDoubleStreamHandler<RESULT>
    extends ScalarStreamHandler<Double, OptionalDouble, RESULT> {

  public OptionalDoubleStreamHandler(Function<Stream<OptionalDouble>, RESULT> mapper) {
    super(() -> new OptionalDoubleScalar(), mapper);
  }
}
