package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.OutParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarOutParameter<BASIC, CONTAINER> implements OutParameter<BASIC> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  protected final Reference<CONTAINER> reference;

  public ScalarOutParameter(
      Supplier<Scalar<BASIC, CONTAINER>> supplier, Reference<CONTAINER> reference) {
    assertNotNull(supplier, reference);
    this.scalar = supplier.get();
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
  public Optional<Class<?>> getHolderClass() {
    return scalar.getHolderClass();
  }

  @Override
  public <P, TH extends Throwable> void accept(SqlParameterVisitor<P, TH> visitor, P p) throws TH {
    visitor.visitOutParameter(this, p);
  }
}
