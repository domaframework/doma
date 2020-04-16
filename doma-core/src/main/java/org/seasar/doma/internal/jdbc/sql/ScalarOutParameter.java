package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.OutParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarOutParameter<BASIC, CONTAINER> implements OutParameter<BASIC> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  protected final Reference<CONTAINER> reference;

  public ScalarOutParameter(Scalar<BASIC, CONTAINER> holder, Reference<CONTAINER> reference) {
    assertNotNull(holder, reference);
    this.scalar = holder;
    this.reference = reference;
  }

  @Override
  public Object getValue() {
    return scalar.get();
  }

  @Override
  public Wrapper<BASIC> getWrapper() {
    return scalar.getWrapper();
  }

  @Override
  public void updateReference() {
    reference.set(scalar.get());
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return scalar.getDomainClass();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitOutParameter(this, p);
  }
}
