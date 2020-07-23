package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;
import org.seasar.doma.message.MessageResource;

/** Thrown to indicate that an unique constraint violation occurs. */
public class UniqueConstraintException extends JdbcException {

  private static final long serialVersionUID = 1L;

  protected final SqlKind kind;

  protected final String rawSql;

  protected final String formattedSql;

  protected final String sqlFilePath;

  public UniqueConstraintException(SqlLogType logType, Sql<?> sql, Throwable cause) {
    this(
        logType,
        sql.getKind(),
        sql.getRawSql(),
        sql.getFormattedSql(),
        sql.getSqlFilePath(),
        cause);
  }

  public UniqueConstraintException(
      SqlLogType logType,
      SqlKind kind,
      String rawSql,
      String formattedSql,
      String sqlFilePath,
      Throwable cause) {
    super(Message.DOMA2004, cause, sqlFilePath, choiceSql(logType, rawSql, formattedSql), cause);
    this.kind = kind;
    this.rawSql = rawSql;
    this.formattedSql = formattedSql;
    this.sqlFilePath = sqlFilePath;
  }

  protected UniqueConstraintException(
      MessageResource messageCode,
      SqlKind kind,
      String rawSql,
      String sqlFilePath,
      Throwable cause) {
    super(messageCode, cause, sqlFilePath, rawSql, cause);
    this.kind = kind;
    this.rawSql = rawSql;
    this.formattedSql = null;
    this.sqlFilePath = sqlFilePath;
  }

  /**
   * Returns the SQL kind.
   *
   * @return the SQL kind
   */
  public SqlKind getKind() {
    return kind;
  }

  /**
   * Returns the raw SQL string.
   *
   * @return the raw SQL string
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * Returns the formatted SQL string
   *
   * @return the formatted SQL or {@code null} if this exception is thrown in the batch process
   */
  public String getFormattedSql() {
    return formattedSql;
  }

  /**
   * Returns the SQL file path.
   *
   * @return the SQL file path or {@code null} if the SQL is auto generated
   */
  public String getSqlFilePath() {
    return sqlFilePath;
  }
}
