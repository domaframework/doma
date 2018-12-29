package org.seasar.doma.wrapper;

import java.time.LocalDateTime;
import org.seasar.doma.DomaNullPointerException;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalDateTimeWrapper extends AbstractWrapper<LocalDateTime> {

  /** インスタンスを構築します。 */
  public LocalDateTimeWrapper() {
    super(LocalDateTime.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public LocalDateTimeWrapper(LocalDateTime value) {
    super(LocalDateTime.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalDateTimeWrapper(this, p, q);
  }
}
