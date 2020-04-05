package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Object} class. */
public class ObjectWrapper extends AbstractWrapper<Object> {

  public ObjectWrapper() {
    super(Object.class);
  }

  public ObjectWrapper(Object value) {
    super(Object.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitObjectWrapper(this, p, q);
  }
}
