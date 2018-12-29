package org.seasar.doma.jdbc.id;

/**
 * 識別子プロバイダです。
 *
 * @author nakamura-to
 * @since 2.5.0
 */
public interface IdProvider {

  /**
   * 識別子を返せるならば {@literal true}
   *
   * @return 識別子を返せるかどうか
   */
  boolean isAvailable();

  /**
   * 識別子を返します。
   *
   * @return 識別子
   * @throws IllegalStateException 不正な内部状態により識別子を返せない場合
   * @throws UnsupportedOperationException このメソッドの呼び出しをサポートしていない場合
   */
  long get();
}
