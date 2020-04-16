package org.seasar.doma.internal.jdbc.command;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.Wrapper;

public class BasicStreamHandler<BASIC, RESULT> extends ScalarStreamHandler<BASIC, BASIC, RESULT> {

  public BasicStreamHandler(
      Supplier<Wrapper<BASIC>> supplier, Function<Stream<BASIC>, RESULT> mapper) {
    super(() -> new BasicScalar<>(supplier), mapper);
  }
}
