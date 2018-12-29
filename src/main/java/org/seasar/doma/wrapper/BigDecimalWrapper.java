package org.seasar.doma.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@link BigDecimal} のラッパーです。
 *
 * @author taedium
 */
public class BigDecimalWrapper extends AbstractWrapper<BigDecimal>
    implements NumberWrapper<BigDecimal> {

  /** インスタンスを構築します。 */
  public BigDecimalWrapper() {
    super(BigDecimal.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public BigDecimalWrapper(BigDecimal value) {
    super(BigDecimal.class, value);
  }

  @Override
  public void set(Number v) {
    if (v instanceof BigDecimal) {
      super.set((BigDecimal) v);
    } else if (v instanceof BigInteger) {
      super.set(new BigDecimal((BigInteger) v));
    } else {
      super.set(new BigDecimal(v.doubleValue()));
    }
  }

  @Override
  public void increment() {
    BigDecimal value = doGet();
    if (value != null) {
      doSet(value.add(BigDecimal.ONE));
    }
  }

  @Override
  public void decrement() {
    BigDecimal value = doGet();
    if (value != null) {
      doSet(value.subtract(BigDecimal.ONE));
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitBigDecimalWrapper(this, p, q);
  }
}
