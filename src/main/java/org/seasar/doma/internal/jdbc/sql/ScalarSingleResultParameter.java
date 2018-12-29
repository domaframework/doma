package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.SingleResultParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class ScalarSingleResultParameter<BASIC, CONTAINER>
    implements SingleResultParameter<BASIC, CONTAINER> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  public ScalarSingleResultParameter(Scalar<BASIC, CONTAINER> scalar) {
    assertNotNull(scalar);
    this.scalar = scalar;
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
  public Optional<Class<?>> getDomainClass() {
    return scalar.getDomainClass();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitSingleResultParameter(this, p);
  }
}
