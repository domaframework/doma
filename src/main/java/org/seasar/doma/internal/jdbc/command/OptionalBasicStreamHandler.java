package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalar;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @param <RESULT>
 * @param <BASIC>
 */
public class OptionalBasicStreamHandler<BASIC, RESULT>
    extends ScalarStreamHandler<BASIC, Optional<BASIC>, RESULT> {

  public OptionalBasicStreamHandler(
      Supplier<Wrapper<BASIC>> supplier, Function<Stream<Optional<BASIC>>, RESULT> mapper) {
    super(() -> new OptionalBasicScalar<>(supplier), mapper);
  }
}
