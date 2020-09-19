package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.command.ScalarProvider;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.query.Query;

public class ScalarResultListParameter<BASIC, CONTAINER>
    extends AbstractResultListParameter<CONTAINER> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  public ScalarResultListParameter(Supplier<Scalar<BASIC, CONTAINER>> supplier) {
    super(new ArrayList<>());
    assertNotNull(supplier);
    this.supplier = supplier;
  }

  @Override
  public ObjectProvider<CONTAINER> createObjectProvider(Query query) {
    return new ScalarProvider<>(supplier, query);
  }
}
