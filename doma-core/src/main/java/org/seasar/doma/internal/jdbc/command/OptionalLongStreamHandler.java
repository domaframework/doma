package org.seasar.doma.internal.jdbc.command;

import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongStreamHandler<RESULT>
    extends ScalarStreamHandler<Long, OptionalLong, RESULT> {

  public OptionalLongStreamHandler(Function<Stream<OptionalLong>, RESULT> mapper) {
    super(() -> new OptionalLongScalar(), mapper);
  }
}
