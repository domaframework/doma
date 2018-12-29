package org.seasar.doma.jdbc.tx;

import java.io.Serializable;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * トランザクションがすでに開始されている場合にスローされる例外です。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public class TransactionAlreadyBegunException extends JdbcException implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * インスタンスを構築します。
   *
   * @param transactionId トランザクションID
   */
  public TransactionAlreadyBegunException(String transactionId) {
    super(Message.DOMA2045, transactionId);
  }
}
