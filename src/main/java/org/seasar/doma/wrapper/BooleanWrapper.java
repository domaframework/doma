package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Boolean} のラッパーです。
 *
 * @author taedium
 */
public class BooleanWrapper extends AbstractWrapper<Boolean> {

  /** インスタンスを構築します。 */
  public BooleanWrapper() {
    super(Boolean.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public BooleanWrapper(Boolean value) {
    super(Boolean.class, value);
  }

  @Override
  public Boolean getDefault() {
    return Boolean.FALSE;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitBooleanWrapper(this, p, q);
  }
}
