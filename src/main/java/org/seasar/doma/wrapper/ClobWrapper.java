package org.seasar.doma.wrapper;

import java.sql.Clob;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Clob} のラッパーです。
 *
 * @author taedium
 */
public class ClobWrapper extends AbstractWrapper<Clob> {

  /** インスタンスを構築します。 */
  public ClobWrapper() {
    super(Clob.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public ClobWrapper(Clob value) {
    super(Clob.class, value);
  }

  @Override
  protected Clob doGetCopy() {
    return null;
  }

  @Override
  protected boolean doHasEqualValue(Object otherValue) {
    return false;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitClobWrapper(this, p, q);
  }
}
