package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.SingleResultParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class ScalarSingleResultParameter<BASIC, CONTAINER>
    implements SingleResultParameter<BASIC, CONTAINER> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  public ScalarSingleResultParameter(Supplier<Scalar<BASIC, CONTAINER>> supplier) {
    assertNotNull(supplier);
    this.scalar = supplier.get();
  }

  @Override
  public Wrapper<BASIC> getWrapper() {
    return scalar.getWrapper();
  }

  @Override
  public Object getValue() {
    return scalar.get();
  }

  @Override
  public CONTAINER getResult() {
    return scalar.get();
  }

  @Override
  public Optional<Class<?>> getHolderClass() {
    return scalar.getHolderClass();
  }

  @Override
  public <P, TH extends Throwable> void accept(SqlParameterVisitor<P, TH> visitor, P p) throws TH {
    visitor.visitSingleResultParameter(this, p);
  }
}
