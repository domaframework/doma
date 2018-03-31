package org.seasar.doma.wrapper;

import java.sql.Timestamp;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Timestamp} class. */
public class TimestampWrapper extends AbstractWrapper<Timestamp> {

  public TimestampWrapper() {
    super(Timestamp.class);
  }

  public TimestampWrapper(Timestamp value) {
    super(Timestamp.class, value);
  }

  @Override
  protected Timestamp doGetCopy() {
    var original = get();
    if (original == null) {
      return null;
    }
    var copy = new Timestamp(original.getTime());
    copy.setNanos(original.getNanos());
    return copy;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitTimestampWrapper(this, p, q);
  }
}
