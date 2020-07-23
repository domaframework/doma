package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code double} class. */
public class PrimitiveDoubleWrapper extends DoubleWrapper {

  private static final double defaultValue = 0d;

  public PrimitiveDoubleWrapper() {
    this(defaultValue);
  }

  public PrimitiveDoubleWrapper(double value) {
    super(value);
  }

  @Override
  protected void doSet(Double value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.doubleValue());
  }

  @Override
  public Double getDefault() {
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
    return visitor.visitPrimitiveDoubleWrapper(this, p, q);
  }
}
