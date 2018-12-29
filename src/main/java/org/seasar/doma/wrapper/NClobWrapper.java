package org.seasar.doma.wrapper;

import java.sql.NClob;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@link NClob} のラッパーです。
 *
 * @author taedium
 */
public class NClobWrapper extends AbstractWrapper<NClob> {

  /** インスタンスを構築します。 */
  public NClobWrapper() {
    super(NClob.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public NClobWrapper(NClob value) {
    super(NClob.class, value);
  }

  @Override
  protected NClob doGetCopy() {
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
    return visitor.visitNClobWrapper(this, p, q);
  }
}
