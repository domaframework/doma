package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Integer} のラッパーです。
 *
 * @author taedium
 */
public class IntegerWrapper extends AbstractWrapper<Integer> implements NumberWrapper<Integer> {

  /** インスタンスを構築します。 */
  public IntegerWrapper() {
    super(Integer.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public IntegerWrapper(Integer value) {
    super(Integer.class, value);
  }

  @Override
  public void set(Number v) {
    set(v.intValue());
  }

  @Override
  public Integer getDefault() {
    return Integer.valueOf(0);
  }

  @Override
  public void increment() {
    Integer value = doGet();
    if (value != null) {
      doSet(value.intValue() + 1);
    }
  }

  @Override
  public void decrement() {
    Integer value = doGet();
    if (value != null) {
      doSet(value.intValue() - 1);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitIntegerWrapper(this, p, q);
  }
}
