package org.seasar.doma.wrapper;

import java.time.LocalDate;
import org.seasar.doma.DomaNullPointerException;

/**
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalDateWrapper extends AbstractWrapper<LocalDate> {

  /** インスタンスを構築します。 */
  public LocalDateWrapper() {
    super(LocalDate.class);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param value 値
   */
  public LocalDateWrapper(LocalDate value) {
    super(LocalDate.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalDateWrapper(this, p, q);
  }
}
