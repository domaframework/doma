package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * {@link Config}に不適切な設定がある場合にスローされる例外です。
 *
 * @author taedium
 */
public class ConfigException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** {@link Config} の実装クラス名 */
  protected final String className;

  /** {@link Config} の実装クラスのメソッド名 */
  protected final String methodName;

  /**
   * インスタンスを構築します。
   *
   * @param className {@link Config} の実装クラス名
   * @param methodName {@link Config} の実装クラスのメソッド名
   */
  public ConfigException(String className, String methodName) {
    super(Message.DOMA2035, className, methodName);
    this.className = className;
    this.methodName = methodName;
  }

  /**
   * {@link Config} の実装クラス名を返します。
   *
   * @return {@link Config} の実装クラス名
   */
  public String getClassName() {
    return className;
  }

  /**
   * {@link Config} の実装クラスのメソッド名を返します。
   *
   * @return {@link Config} の実装クラスのメソッド名
   */
  public String getMethodName() {
    return methodName;
  }
}
