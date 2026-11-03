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
package org.seasar.doma.template;

import org.seasar.doma.jdbc.JdbcException;

/**
 * Thrown by {@link SqlTemplate#validate()} to indicate that a SQL template has a syntax error.
 *
 * <p>The message and the message code are the same as those of the error raised while parsing the
 * template. The error is available as the cause.
 */
public class SqlTemplateSyntaxException extends JdbcException {

  private static final long serialVersionUID = 1L;

  /** the SQL template */
  private final String sql;

  /** the line number of the error */
  private final int lineNumber;

  /** the column number of the error */
  private final int columnNumber;

  SqlTemplateSyntaxException(JdbcException cause, String sql, int lineNumber, int columnNumber) {
    super(cause.getMessageResource(), cause, (Object[]) cause.getArgs());
    this.sql = sql;
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
  }

  /**
   * Returns the SQL template.
   *
   * @return the SQL template
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
