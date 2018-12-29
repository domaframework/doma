package org.seasar.doma.wrapper;

import java.sql.Blob;
import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Blob} のラッパーです。
 *
 * @author taedium
 */
public class BlobWrapper extends AbstractWrapper<Blob> {

  /** インスタンスを構築します。 */
  public BlobWrapper() {
    super(Blob.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public BlobWrapper(Blob value) {
    super(Blob.class, value);
  }

  @Override
  protected Blob doGetCopy() {
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
    return visitor.visitBlobWrapper(this, p, q);
  }
}
