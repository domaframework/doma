package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * バッチ処理で一意制約違反が発生した場合にスローされる例外です。
 *
 * <p>{@link #getFormattedSql()}は {@code null} を返します。
 *
 * <p>
 *
 * @author taedium
 */
public class BatchUniqueConstraintException extends UniqueConstraintException {

  private static final long serialVersionUID = 1L;

  /**
   * SQLと原因を指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param sql SQL
   * @param cause 原因
   */
  public BatchUniqueConstraintException(SqlLogType logType, Sql<?> sql, Throwable cause) {
    this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause);
  }

  /**
   * 未加工SQLと原因を指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param kind SQLの種別
   * @param rawSql 未加工SQL
   * @param sqlFilePath SQLファイルのパス
   * @param cause 原因
   */
  public BatchUniqueConstraintException(
      SqlLogType logType, SqlKind kind, String rawSql, String sqlFilePath, Throwable cause) {
    super(Message.DOMA2029, kind, choiceSql(logType, rawSql, rawSql), sqlFilePath, cause);
  }
}
