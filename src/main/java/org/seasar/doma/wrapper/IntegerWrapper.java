package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Integer} class. */
public class IntegerWrapper extends AbstractWrapper<Integer> implements NumberWrapper<Integer> {

  public IntegerWrapper() {
    super(Integer.class);
  }

  public IntegerWrapper(Integer value) {
    super(Integer.class, value);
  }

  @Override
  public void set(Number v) {
    set(v.intValue());
  }

  @Override
  public Integer getDefault() {
    return 0;
  }

  @Override
  public void increment() {
    Integer value = doGet();
    if (value != null) {
      doSet(value + 1);
    }
  }

  @Override
  public void decrement() {
    Integer value = doGet();
    if (value != null) {
      doSet(value - 1);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitIntegerWrapper(this, p, q);
  }
}
