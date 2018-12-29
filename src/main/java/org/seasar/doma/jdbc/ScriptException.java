package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * スクリプトファイル内のSQLの実行中に例外が発生した場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.7.0
 */
public class ScriptException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** 未加工SQL */
  protected final String rawSql;

  /** スクリプトファイルのパス */
  protected final String scriptFilePath;

  /** 行番号 */
  protected final int lineNumber;

  /**
   * インスタンスを構築します。
   *
   * @param cause 原因
   * @param sql SQL
   * @param lineNumber 行番号
   */
  public ScriptException(Throwable cause, Sql<?> sql, int lineNumber) {
    this(cause, sql.getRawSql(), sql.getSqlFilePath(), lineNumber);
  }

  /**
   * インスタンスを構築します。
   *
   * @param cause 原因
   * @param rawSql 未加工SQL
   * @param scriptFilePath SQLファイルのパス
   * @param lineNumber 行番号
   */
  public ScriptException(Throwable cause, String rawSql, String scriptFilePath, int lineNumber) {
    super(Message.DOMA2077, cause, rawSql, scriptFilePath, lineNumber, cause);
    this.rawSql = rawSql;
    this.scriptFilePath = scriptFilePath;
    this.lineNumber = lineNumber;
  }

  /**
   * 未加工SQLを返します。
   *
   * @return 未加工SQL
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * スクリプトファイルのパスを返します。
   *
   * @return スクリプトファイルのパス
   */
  public String getScriptFilePath() {
    return scriptFilePath;
  }

  /**
   * 行番号を返します。
   *
   * @return 行番号
   */
  public int getLineNumber() {
    return lineNumber;
  }
}
