package org.seasar.doma.wrapper;

import java.time.LocalDate;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link LocalDate} class. */
public class LocalDateWrapper extends AbstractWrapper<LocalDate> {

  public LocalDateWrapper() {
    super(LocalDate.class);
  }

  public LocalDateWrapper(LocalDate value) {
    super(LocalDate.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalDateWrapper(this, p, q);
  }
}
