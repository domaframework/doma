package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * SQLファイルが見つからない場合にスローされる例外です。
 *
 * @author taedium
 */
public class SqlFileNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** 見つからないSQLファイルのパス */
  protected final String path;

  /**
   * SQLファイルのパスを指定してインスタンスを構築します。
   *
   * @param path 見つからないSQLファイルのパス
   */
  public SqlFileNotFoundException(String path) {
    super(Message.DOMA2011, path);
    this.path = path;
  }

  /**
   * 見つからないSQLファイルのパスを返します。
   *
   * @return 見つからないSQLファイルのパス
   */
  public String getPath() {
    return path;
  }
}
