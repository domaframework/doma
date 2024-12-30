package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.OutParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarInOutParameter<BASIC, CONTAINER>
    implements InParameter<BASIC>, OutParameter<BASIC> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  protected final Reference<CONTAINER> reference;

  public ScalarInOutParameter(Scalar<BASIC, CONTAINER> holder, Reference<CONTAINER> reference) {
    assertNotNull(holder, reference);
    this.scalar = holder;
    this.reference = reference;
    holder.set(reference.get());
  }

  @Override
  public CONTAINER getValue() {
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
  public Optional<JdbcType<Object>> getJdbcType() {
    return scalar.getJdbcType();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitInOutParameter(this, p);
  }
}
