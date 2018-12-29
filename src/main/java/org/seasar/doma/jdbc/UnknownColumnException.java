package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/** Thrown to indicate that there is the column that is unknown to an entity. */
public class UnknownColumnException extends JdbcException {

  private static final long serialVersionUID = 1L;

  protected final String columnName;

  protected final String expectedPropertyName;

  protected final String entityClassName;

  protected final SqlKind kind;

  protected final String rawSql;

  protected final String formattedSql;

  protected final String sqlFilePath;

  public UnknownColumnException(
      SqlLogType logType,
      String columnName,
      String expectedPropertyName,
      String entityClassName,
      SqlKind kind,
      String rawSql,
      String formattedSql,
      String sqlFilePath) {
    super(
        Message.DOMA2002,
        columnName,
        expectedPropertyName,
        entityClassName,
        sqlFilePath,
        choiceSql(logType, rawSql, formattedSql));
    this.columnName = columnName;
    this.expectedPropertyName = expectedPropertyName;
    this.entityClassName = entityClassName;
    this.kind = kind;
    this.rawSql = rawSql;
    this.formattedSql = formattedSql;
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
   * Returns the entity class name.
   *
   * @return the entity class name
   */
  public String getEntityClassName() {
    return entityClassName;
  }

  /**
   * Returns the unknown column name.
   *
   * @return the unknown column name
   */
  public String getColumnName() {
    return columnName;
  }

  /**
   * Returns the expected property name that is mapped to the unknown column name.
   *
   * @return the expected property name
   */
  public String getExpectedPropertyName() {
    return expectedPropertyName;
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
