package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Daoクラスのメソッドが見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.27.0
 */
public class DaoMethodNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** クラスの名前 */
  protected final String className;

  /** メソッドのシグネチャ */
  protected final String signature;

  /**
   * クラスの名前とメソッドのシグネチャを指定してインスタンスを構築します。
   *
   * @param cause 原因
   * @param className クラス名
   * @param signature メソッドのシグネチャ
   */
  public DaoMethodNotFoundException(Throwable cause, String className, String signature) {
    super(Message.DOMA2215, cause, className, signature, cause);
    this.className = className;
    this.signature = signature;
  }

  /**
   * クラスの名前を返します。
   *
   * @return クラスの名前
   */
  public String getClassName() {
    return className;
  }

  /**
   * メソッドのシグネチャを返します。
   *
   * @return メソッドのシグネチャ
   */
  public String getSignature() {
    return signature;
  }
}
