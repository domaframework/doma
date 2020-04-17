package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code float} class. */
public class PrimitiveFloatWrapper extends FloatWrapper {

  private static final float defaultValue = 0f;

  public PrimitiveFloatWrapper() {
    this(defaultValue);
  }

  public PrimitiveFloatWrapper(float value) {
    super(value);
  }

  @Override
  protected void doSet(Float value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.floatValue());
  }

  @Override
  public Float getDefault() {
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
    return visitor.visitPrimitiveFloatWrapper(this, p, q);
  }
}
