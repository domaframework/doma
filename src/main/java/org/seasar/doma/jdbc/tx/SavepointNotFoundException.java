package org.seasar.doma.jdbc.tx;

import java.io.Serializable;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * セーブポイントが見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.1.0
 */
public class SavepointNotFoundException extends JdbcException implements Serializable {

  private static final long serialVersionUID = 1L;

  /** セーブポイントの名前 */
  protected final String savepointName;

  /**
   * インスタンスを構築します。
   *
   * @param savepointName セーブポイントの名前
   */
  public SavepointNotFoundException(String savepointName) {
    super(Message.DOMA2054, savepointName);
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
