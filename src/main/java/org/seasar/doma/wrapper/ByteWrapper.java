package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Byte} のラッパーです。
 *
 * @author taedium
 */
public class ByteWrapper extends AbstractWrapper<Byte> implements NumberWrapper<Byte> {

  /** インスタンスを構築します。 */
  public ByteWrapper() {
    super(Byte.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public ByteWrapper(Byte value) {
    super(Byte.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.byteValue());
  }

  @Override
  public Byte getDefault() {
    return Byte.valueOf((byte) 0);
  }

  @Override
  public void increment() {
    Byte value = doGet();
    if (value != null) {
      doSet((byte) (value.byteValue() + 1));
    }
  }

  @Override
  public void decrement() {
    Byte value = doGet();
    if (value != null) {
      doSet((byte) (value.byteValue() - 1));
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
