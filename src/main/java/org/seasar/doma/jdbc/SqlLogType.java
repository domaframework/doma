package org.seasar.doma.jdbc;

import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * SQLのログの出力形式です。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public enum SqlLogType {

  /**
   * 未加工SQL。
   *
   * <p>SQL中のバインドパラメータは {@code ?} で表されます。
   */
  RAW,

  /**
   * フォーマット済みSQL。
   *
   * <p>SQL中のバインドパラメータはフォーマットされて表されます。 フォーマットには {@link Dialect#getSqlLogFormattingVisitor()}
   * が返すオブジェクトが使用されます。
   */
  FORMATTED,

  /** 無出力。 */
  NONE
}
