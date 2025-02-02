/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc;

import java.io.Serial;
import org.seasar.doma.message.Message;
import org.seasar.doma.message.MessageResource;

/** Thrown to indicate that there is the column that is unknown to an entity. */
public class UnknownColumnException extends JdbcException {

  @Serial private static final long serialVersionUID = 1L;

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
    this(
        Message.DOMA2002,
        new Object[] {
          columnName,
          expectedPropertyName,
          entityClassName,
          sqlFilePath,
          choiceSql(logType, rawSql, formattedSql)
        },
        columnName,
        expectedPropertyName,
        entityClassName,
        kind,
        rawSql,
        formattedSql,
        sqlFilePath);
  }

  protected UnknownColumnException(
      MessageResource messageCode,
      Object[] args,
      String columnName,
      String expectedPropertyName,
      String entityClassName,
      SqlKind kind,
      String rawSql,
      String formattedSql,
      String sqlFilePath) {
    super(messageCode, args);
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
