package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a batch processing is failed.
 *
 * <p>{@link #getFormattedSql()} returns {@code null}.
 */
public class BatchSqlExecutionException extends SqlExecutionException {

  private static final long serialVersionUID = 1L;

  public BatchSqlExecutionException(
      SqlLogType logType, Sql<?> sql, Throwable cause, Throwable rootCause) {
    this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause, rootCause);
  }

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
