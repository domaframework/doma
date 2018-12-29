package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Long} のラッパーです。
 *
 * @author taedium
 */
public class LongWrapper extends AbstractWrapper<Long> implements NumberWrapper<Long> {

  /** インスタンスを構築します。 */
  public LongWrapper() {
    super(Long.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public LongWrapper(Long value) {
    super(Long.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.longValue());
  }

  @Override
  public Long getDefault() {
    return Long.valueOf(0L);
  }

  @Override
  public void increment() {
    Long value = doGet();
    if (value != null) {
      doSet(value.longValue() + 1L);
    }
  }

  @Override
  public void decrement() {
    Long value = doGet();
    if (value != null) {
      doSet(value.longValue() - 1L);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLongWrapper(this, p, q);
  }
}
