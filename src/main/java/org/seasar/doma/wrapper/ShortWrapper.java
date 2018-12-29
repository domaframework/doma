package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Short} のラッパーです。
 *
 * @author taedium
 */
public class ShortWrapper extends AbstractWrapper<Short> implements NumberWrapper<Short> {

  /** インスタンスを構築します。 */
  public ShortWrapper() {
    super(Short.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public ShortWrapper(Short value) {
    super(Short.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.shortValue());
  }

  @Override
  public Short getDefault() {
    return Short.valueOf((short) 0);
  }

  @Override
  public void increment() {
    Short value = doGet();
    if (value != null) {
      doSet((short) (value.shortValue() + 1));
    }
  }

  @Override
  public void decrement() {
    Short value = doGet();
    if (value != null) {
      doSet((short) (value.shortValue() - 1));
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitShortWrapper(this, p, q);
  }
}
