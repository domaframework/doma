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

import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate an error at a location in SQL written in the two-way SQL syntax.
 *
 * <p>Such SQL comes from SQL files, {@code @Sql} annotations, and the query builders, which turn
 * their parameters into bind variable directives.
 *
 * <p>The message arguments start with the SQL, the line number, and the column number, followed by
 * the arguments specific to the error.
 *
 * @see SqlParseException
 * @see SqlBuildException
 */
public abstract class SqlLocationException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** the SQL that has the error */
  protected final String sql;

  /** the line number of the error */
  protected final int lineNumber;

  /** the column number of the error */
  protected final int columnNumber;

  /**
   * Creates an instance.
   *
   * @param messageCode the message code
   * @param cause the cause, or {@code null}
   * @param sql the SQL that has the error
   * @param lineNumber the line number of the error
   * @param columnNumber the column number of the error
   * @param args the message arguments that follow the column number
   */
  protected SqlLocationException(
      MessageResource messageCode,
      Throwable cause,
      String sql,
      int lineNumber,
      int columnNumber,
      Object... args) {
    super(messageCode, cause, prependLocation(sql, lineNumber, columnNumber, args));
    this.sql = sql;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
  }

  private static Object[] prependLocation(
      String sql, int lineNumber, int columnNumber, Object[] args) {
    Object[] result = new Object[args.length + 3];
    result[0] = sql;
    result[1] = lineNumber;
    result[2] = columnNumber;
    System.arraycopy(args, 0, result, 3, args.length);
    return result;
  }

  /**
   * Returns the SQL that has the error.
   *
   * @return the SQL
   */
  public String getSql() {
    return sql;
  }

  /**
   * Returns the line number of the error. The first line is 1.
   *
   * @return the line number
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Returns the column number of the error in its line.
   *
   * @return the column number
   */
  public int getColumnNumber() {
    return columnNumber;
  }
}
