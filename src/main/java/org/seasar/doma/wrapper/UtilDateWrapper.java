package org.seasar.doma.wrapper;

import java.util.Date;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Date} class. */
public class UtilDateWrapper extends AbstractWrapper<Date> {

  public UtilDateWrapper() {
    super(Date.class);
  }

  public UtilDateWrapper(Date value) {
    super(Date.class, value);
  }

  @Override
  protected Date doGetCopy() {
    var original = get();
    if (original == null) {
      return null;
    }
    return new Date(original.getTime());
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitUtilDateWrapper(this, p, q);
  }
}
