package org.seasar.doma.wrapper;

import java.time.LocalTime;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link LocalTime} class. */
public class LocalTimeWrapper extends AbstractWrapper<LocalTime> {

  public LocalTimeWrapper() {
    super(LocalTime.class);
  }

  public LocalTimeWrapper(LocalTime value) {
    super(LocalTime.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalTimeWrapper(this, p, q);
  }
}
