package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * バッチ処理時に楽観的排他制御に失敗した場合にスローされる例外です。
 *
 * <p>{@link #getFormattedSql()}は {@code null} を返します。
 *
 * @author taedium
 */
public class BatchOptimisticLockException extends OptimisticLockException {

  private static final long serialVersionUID = 1L;

  /**
   * 楽観的排他制御に失敗したSQLを指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param sql SQL
   */
  public BatchOptimisticLockException(SqlLogType logType, Sql<?> sql) {
    this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath());
  }

  /**
   * 楽観的排他制御に失敗した未加工SQLを指定してインスタンスを構築します。 * @param kind SQLの種別 * @param rawSql 未加工SQL
   *
   * @param logType ログタイプ
   * @param kind SQLの種別
   * @param rawSql 未加工SQL
   * @param sqlFilePath SQLファイルのパス
   */
  public BatchOptimisticLockException(
      SqlLogType logType, SqlKind kind, String rawSql, String sqlFilePath) {
    super(Message.DOMA2028, kind, choiceSql(logType, rawSql, rawSql), sqlFilePath);
  }
}
