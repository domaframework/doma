package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link String} のラッパーです。
 *
 * @author taedium
 */
public class StringWrapper extends AbstractWrapper<String> {

  /** インスタンスを構築します。 */
  public StringWrapper() {
    super(String.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public StringWrapper(String value) {
    super(String.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitStringWrapper(this, p, q);
  }
}
