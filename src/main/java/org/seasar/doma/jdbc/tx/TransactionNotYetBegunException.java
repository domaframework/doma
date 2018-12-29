package org.seasar.doma.jdbc.tx;

import java.io.Serializable;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * トランザクションがまだ開始されていない場合にスローされる例外です。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public class TransactionNotYetBegunException extends JdbcException implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * インスタンスを構築します。
   *
   * @param message メッセージ
   * @param args メッセージの引数
   */
  public TransactionNotYetBegunException(Message message, Object... args) {
    super(message, args);
  }
}
