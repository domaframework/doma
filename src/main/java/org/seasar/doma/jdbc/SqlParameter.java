package org.seasar.doma.jdbc;

/**
 * SQLのパラメータを表します。
 *
 * @author taedium
 */
public interface SqlParameter {

  /**
   * パラメータの値を返します。
   *
   * @return パラメータの値を返します。
   */
  Object getValue();

  <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p) throws TH;
}
