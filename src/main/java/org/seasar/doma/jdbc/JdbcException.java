package org.seasar.doma.jdbc;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * JDBCに関する例外です。
 *
 * @author taedium
 */
public class JdbcException extends DomaException {

  private static final long serialVersionUID = 1L;

  /**
   * インスタンスを構築します。
   *
   * @param messageCode メッセージコード
   * @param args メッセージへの引数
   */
  public JdbcException(MessageResource messageCode, Object... args) {
    super(messageCode, args);
  }

  /**
   * この例外の原因となった {@link Throwable} を指定してインスタンスを構築します。
   *
   * @param messageCode メッセージコード
   * @param cause 原因
   * @param args メッセージへの引数
   */
  public JdbcException(MessageResource messageCode, Throwable cause, Object... args) {
    super(messageCode, cause, args);
  }

  /**
   * ログタイプに応じてログ用SQLを選択します。
   *
   * @param logType ログタイプ
   * @param rawSql 未加工SQL
   * @param formattedSql フォーマット済みSQL
   * @return ログ用SQL
   * @since 1.22.0
   */
  protected static String choiceSql(SqlLogType logType, String rawSql, String formattedSql) {
    switch (logType) {
      case RAW:
        return rawSql;
      case FORMATTED:
        return formattedSql;
      case NONE:
        return "";
      default:
        throw new AssertionError("unreachable");
    }
  }
}
