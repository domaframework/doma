package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * バッチ処理に失敗した場合にスローされる例外です。
 *
 * <p>{@link #getFormattedSql()}は {@code null} を返します。
 *
 * <p>
 *
 * @author taedium
 */
public class BatchSqlExecutionException extends SqlExecutionException {

  private static final long serialVersionUID = 1L;

  /**
   * SQL、スローされた原因、根本原因を指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param sql SQL
   * @param cause スローされた原因
   * @param rootCause 根本原因
   */
  public BatchSqlExecutionException(
      SqlLogType logType, Sql<?> sql, Throwable cause, Throwable rootCause) {
    this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause, rootCause);
  }

  /**
   * 未加工SQL、スローされた原因、根本原因を指定してインスタンスを構築します。
   *
   * @param logType ログタイプ
   * @param kind SQLの種別
   * @param rawSql 未加工SQL
   * @param sqlFilePath SQLファイルのパス
   * @param cause スローされた原因
   * @param rootCause 根本原因
   */
  public BatchSqlExecutionException(
      SqlLogType logType,
      SqlKind kind,
      String rawSql,
      String sqlFilePath,
      Throwable cause,
      Throwable rootCause) {
    super(
        Message.DOMA2030,
        kind,
        choiceSql(logType, rawSql, rawSql),
        null,
        sqlFilePath,
        cause,
        rootCause);
  }
}
