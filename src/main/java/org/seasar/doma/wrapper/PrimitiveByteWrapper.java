package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code byte} class. */
public class PrimitiveByteWrapper extends ByteWrapper {

  private static final byte defaultValue = 0;

  public PrimitiveByteWrapper() {
    this(defaultValue);
  }

  public PrimitiveByteWrapper(byte value) {
    super(value);
  }

  @Override
  protected void doSet(Byte value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.byteValue());
  }

  @Override
  public Byte getDefault() {
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
    return visitor.visitPrimitiveByteWrapper(this, p, q);
  }
}
