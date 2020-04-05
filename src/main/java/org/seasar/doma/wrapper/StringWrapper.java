package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link String} class. */
public class StringWrapper extends AbstractWrapper<String> {

  public StringWrapper() {
    super(String.class);
  }

  public StringWrapper(String value) {
    super(String.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitStringWrapper(this, p, q);
  }
}
