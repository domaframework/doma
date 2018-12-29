package org.seasar.doma.wrapper;

import java.sql.Time;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Time} class. */
public class TimeWrapper extends AbstractWrapper<Time> {

  public TimeWrapper() {
    super(Time.class);
  }

  public TimeWrapper(Time value) {
    super(Time.class, value);
  }

  @Override
  protected Time doGetCopy() {
    Time original = get();
    if (original == null) {
      return null;
    }
    return new Time(original.getTime());
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitTimeWrapper(this, p, q);
  }
}
