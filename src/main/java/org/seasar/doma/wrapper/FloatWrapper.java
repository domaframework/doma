package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Float} のラッパーです。
 *
 * @author taedium
 */
public class FloatWrapper extends AbstractWrapper<Float> implements NumberWrapper<Float> {

  /** インスタンスを構築します。 */
  public FloatWrapper() {
    super(Float.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public FloatWrapper(Float value) {
    super(Float.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.floatValue());
  }

  @Override
  public Float getDefault() {
    return Float.valueOf(0f);
  }

  @Override
  public void increment() {
    Float value = doGet();
    if (value != null) {
      doSet(value.floatValue() + 1f);
    }
  }

  @Override
  public void decrement() {
    Float value = doGet();
    if (value != null) {
      doSet(value.floatValue() - 1f);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitFloatWrapper(this, p, q);
  }
}
