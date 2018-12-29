package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Double} のラッパーです。
 *
 * @author taedium
 */
public class DoubleWrapper extends AbstractWrapper<Double> implements NumberWrapper<Double> {

  /** インスタンスを構築します。 */
  public DoubleWrapper() {
    super(Double.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public DoubleWrapper(Double value) {
    super(Double.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.doubleValue());
  }

  @Override
  public Double getDefault() {
    return Double.valueOf(0d);
  }

  @Override
  public void increment() {
    Double value = doGet();
    if (value != null) {
      doSet(value.doubleValue() + 1d);
    }
  }

  @Override
  public void decrement() {
    Double value = doGet();
    if (value != null) {
      doSet(value.doubleValue() - 1d);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitDoubleWrapper(this, p, q);
  }
}
