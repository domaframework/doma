package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * JDBCに関するサポートされていないメソッドが呼び出された場合にスローされる例外です。
 *
 * @author taedium
 */
public class JdbcUnsupportedOperationException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** サポートされていないメソッドを持つクラスの名前 */
  protected final String className;

  /** サポートされていないメソッドの名前 */
  protected final String methodName;

  /**
   * インスタンスを構築します。
   *
   * @param className サポートされていないメソッドを持つクラスの名前
   * @param methodName サポートされていないメソッドの名前
   */
  public JdbcUnsupportedOperationException(String className, String methodName) {
    super(Message.DOMA2034, className, methodName);
    this.className = className;
    this.methodName = methodName;
  }

  /**
   * サポートされていないメソッドを持つクラスの名前を返します。
   *
   * @return サポートされていないメソッドを持つクラスの名前
   */
  public String getClassName() {
    return className;
  }

  /**
   * サポートされていないメソッドの名前を返します。
   *
   * @return サポートされていないメソッドの名前
   */
  public String getMethodName() {
    return methodName;
  }
}
