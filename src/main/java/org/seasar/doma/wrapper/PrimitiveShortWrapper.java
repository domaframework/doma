package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code short} class. */
public class PrimitiveShortWrapper extends ShortWrapper {

  private static final short defaultValue = 0;

  public PrimitiveShortWrapper() {
    this(defaultValue);
  }

  public PrimitiveShortWrapper(short value) {
    super(value);
  }

  @Override
  protected void doSet(Short value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.shortValue());
  }

  @Override
  public Short getDefault() {
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
    return visitor.visitPrimitiveShortWrapper(this, p, q);
  }
}
