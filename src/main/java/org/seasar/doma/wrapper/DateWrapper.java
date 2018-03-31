package org.seasar.doma.wrapper;

import java.sql.Date;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Date} class. */
public class DateWrapper extends AbstractWrapper<Date> {

  public DateWrapper() {
    super(Date.class);
  }

  public DateWrapper(Date value) {
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
    return visitor.visitDateWrapper(this, p, q);
  }
}
