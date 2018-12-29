package org.seasar.doma.wrapper;

import java.time.LocalTime;
import org.seasar.doma.DomaNullPointerException;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalTimeWrapper extends AbstractWrapper<LocalTime> {

  /** インスタンスを構築します。 */
  public LocalTimeWrapper() {
    super(LocalTime.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public LocalTimeWrapper(LocalTime value) {
    super(LocalTime.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalTimeWrapper(this, p, q);
  }
}
