package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code int} class. */
public class PrimitiveIntWrapper extends IntegerWrapper {

  private static final int defaultValue = 0;

  public PrimitiveIntWrapper() {
    this(defaultValue);
  }

  public PrimitiveIntWrapper(int value) {
    super(value);
  }

  @Override
  protected void doSet(Integer value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.intValue());
  }

  @Override
  public Integer getDefault() {
    return defaultValue;
  }

  @Override
  public boolean isPrimitiveWrapper() {
    return true;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitPrimitiveIntWrapper(this, p, q);
  }
}
