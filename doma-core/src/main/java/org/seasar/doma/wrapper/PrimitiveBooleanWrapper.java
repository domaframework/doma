package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code boolean} class. */
public class PrimitiveBooleanWrapper extends BooleanWrapper {

  private static final boolean defaultValue = false;

  public PrimitiveBooleanWrapper() {
    this(defaultValue);
  }

  public PrimitiveBooleanWrapper(boolean value) {
    super(value);
  }

  @Override
  public Boolean getDefault() {
    return false;
  }

  @Override
  protected void doSet(Boolean value) {
    this.value = value == null ? defaultValue : value;
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
    return visitor.visitPrimitiveBooleanWrapper(this, p, q);
  }
}
