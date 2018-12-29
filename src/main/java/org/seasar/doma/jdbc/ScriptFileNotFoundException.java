package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * スクリプトファイルが見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.7.0
 */
public class ScriptFileNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** 見つからないスクリプトファイルのパス */
  protected final String path;

  /**
   * スクリプトファイルのパスを指定してインスタンスを構築します。
   *
   * @param path 見つからないスクリプトファイルのパス
   */
  public ScriptFileNotFoundException(String path) {
    super(Message.DOMA2012, path);
    this.path = path;
  }

  /**
   * 見つからないスクリプトファイルのパスを返します。
   *
   * @return 見つからないスクリプトファイルのパス
   */
  public String getPath() {
    return path;
  }
}
