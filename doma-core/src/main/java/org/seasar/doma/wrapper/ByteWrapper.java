package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Byte} class. */
public class ByteWrapper extends AbstractWrapper<Byte> implements NumberWrapper<Byte> {

  public ByteWrapper() {
    super(Byte.class);
  }

  public ByteWrapper(Byte value) {
    super(Byte.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.byteValue());
  }

  @Override
  public void increment() {
    Byte value = doGet();
    if (value != null) {
      doSet((byte) (value + 1));
    }
  }

  @Override
  public void decrement() {
    Byte value = doGet();
    if (value != null) {
      doSet((byte) (value - 1));
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitByteWrapper(this, p, q);
  }
}
