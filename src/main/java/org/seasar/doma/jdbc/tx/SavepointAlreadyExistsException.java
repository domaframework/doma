package org.seasar.doma.jdbc.tx;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * セーブポイントがすでに存在する場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.1.0
 */
public class SavepointAlreadyExistsException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** セーブポイントの名前 */
  protected final String savepointName;

  /**
   * インスタンスを構築します。
   *
   * @param savepointName セーブポイントの名前
   */
  public SavepointAlreadyExistsException(String savepointName) {
    super(Message.DOMA2059, savepointName);
    this.savepointName = savepointName;
  }

  /**
   * セーブポイントの名前を返します。
   *
   * @return セーブポイントの名前
   */
  public String getSavepointName() {
    return savepointName;
  }
}
