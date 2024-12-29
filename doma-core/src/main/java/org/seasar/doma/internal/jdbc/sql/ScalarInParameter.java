package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarInParameter<BASIC, CONTAINER> implements InParameter<BASIC> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  public ScalarInParameter(Scalar<BASIC, CONTAINER> holder) {
    assertNotNull(holder);
    this.scalar = holder;
  }

  public ScalarInParameter(Scalar<BASIC, CONTAINER> holder, CONTAINER value) {
    assertNotNull(holder);
    this.scalar = holder;
    holder.set(value);
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
    return visitor.visitInParameter(this, p);
  }
}
