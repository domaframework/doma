package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Long} class. */
public class LongWrapper extends AbstractWrapper<Long> implements NumberWrapper<Long> {

  public LongWrapper() {
    super(Long.class);
  }

  public LongWrapper(Long value) {
    super(Long.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.longValue());
  }

  @Override
  public Long getDefault() {
    return 0L;
  }

  @Override
  public void increment() {
    var value = doGet();
    if (value != null) {
      doSet(value + 1L);
    }
  }

  @Override
  public void decrement() {
    var value = doGet();
    if (value != null) {
      doSet(value - 1L);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLongWrapper(this, p, q);
  }
}
