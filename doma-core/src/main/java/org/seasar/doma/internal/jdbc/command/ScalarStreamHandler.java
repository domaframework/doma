package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.query.SelectQuery;

public class ScalarStreamHandler<BASIC, CONTAINER, RESULT>
    extends AbstractStreamHandler<CONTAINER, RESULT> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  public ScalarStreamHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier, Function<Stream<CONTAINER>, RESULT> mapper) {
    super(mapper);
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  protected ScalarProvider<BASIC, CONTAINER> createObjectProvider(SelectQuery query) {
    return new ScalarProvider<>(supplier, query);
  }
}
