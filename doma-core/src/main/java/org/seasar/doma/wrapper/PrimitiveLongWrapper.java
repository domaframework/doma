package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code long} class. */
public class PrimitiveLongWrapper extends LongWrapper {

  private static final long defaultValue = 0L;

  public PrimitiveLongWrapper() {
    this(defaultValue);
  }

  public PrimitiveLongWrapper(long value) {
    super(value);
  }

  @Override
  protected void doSet(Long value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.longValue());
  }

  @Override
  public Long getDefault() {
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
    return visitor.visitPrimitiveLongWrapper(this, p, q);
  }
}
