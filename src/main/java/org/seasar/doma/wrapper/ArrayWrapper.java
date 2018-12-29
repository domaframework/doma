package org.seasar.doma.wrapper;

import java.sql.Array;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Array} class. */
public class ArrayWrapper extends AbstractWrapper<Array> {

  public ArrayWrapper() {
    super(Array.class);
  }

  public ArrayWrapper(Array value) {
    super(Array.class, value);
  }

  @Override
  protected Array doGetCopy() {
    return null;
  }

  @Override
  protected boolean doHasEqualValue(Object otherValue) {
    return false;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitArrayWrapper(this, p, q);
  }
}
