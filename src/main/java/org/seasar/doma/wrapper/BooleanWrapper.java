package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Boolean} class. */
public class BooleanWrapper extends AbstractWrapper<Boolean> {

  public BooleanWrapper() {
    super(Boolean.class);
  }

  public BooleanWrapper(Boolean value) {
    super(Boolean.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitBooleanWrapper(this, p, q);
  }
}
