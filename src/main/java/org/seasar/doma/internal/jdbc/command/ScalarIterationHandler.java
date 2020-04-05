package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.query.SelectQuery;

public class ScalarIterationHandler<BASIC, CONTAINER, RESULT>
    extends AbstractIterationHandler<CONTAINER, RESULT> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  public ScalarIterationHandler(
      Supplier<Scalar<BASIC, CONTAINER>> supplier,
      IterationCallback<CONTAINER, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  protected ScalarProvider<BASIC, CONTAINER> createObjectProvider(SelectQuery query) {
    return new ScalarProvider<>(supplier, query);
  }
}
