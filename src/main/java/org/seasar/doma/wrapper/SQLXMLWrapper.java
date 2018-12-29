package org.seasar.doma.wrapper;

import java.sql.SQLXML;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@link SQLXML} のラッパーです。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public class SQLXMLWrapper extends AbstractWrapper<SQLXML> {

  /** インスタンスを構築します。 */
  public SQLXMLWrapper() {
    super(SQLXML.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public SQLXMLWrapper(SQLXML value) {
    super(SQLXML.class, value);
  }

  @Override
  protected SQLXML doGetCopy() {
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
    return visitor.visitSQLXMLWrapper(this, p, q);
  }
}
