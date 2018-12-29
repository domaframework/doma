package org.seasar.doma.internal.jdbc.scalar;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.Wrapper;

/**
 * {@link Wrapper} に関する例外を表します。
 *
 * @author taedium
 */
public class ScalarException extends DomaException {

  private static final long serialVersionUID = 1L;

  /**
   * インスタンスを構築します。
   *
   * @param messageCode メッセージコード
   * @param args メッセージへの引数
   */
  public ScalarException(Message messageCode, Object... args) {
    super(messageCode, args);
  }

  /**
   * この例外の原因となった {@link Throwable} を指定してインスタンスを構築します。
   *
   * @param messageCode メッセージコード
   * @param cause 原因
   * @param args メッセージへの引数
   */
  public ScalarException(Message messageCode, Throwable cause, Object... args) {
    super(messageCode, cause, args);
  }
}
