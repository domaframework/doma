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

import org.seasar.doma.message.Message;

/** Thrown to indicate that a column is duplicated in a result set. */
public class DuplicateColumnException extends JdbcException {

  private static final long serialVersionUID = 1L;

  protected final String columnName;

  protected final String rawSql;

  protected final String formattedSql;

  protected final String sqlFilePath;

  public DuplicateColumnException(
      SqlLogType logType,
      String columnName,
      String rawSql,
      String formattedSql,
      String sqlFilePath) {
    super(Message.DOMA2237, columnName, sqlFilePath, choiceSql(logType, rawSql, formattedSql));
    this.columnName = columnName;
    this.rawSql = rawSql;
    this.formattedSql = formattedSql;
    this.sqlFilePath = sqlFilePath;
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
