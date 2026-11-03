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

/** Thrown to indicate a syntax error found while parsing a SQL template. */
public class SqlParseException extends SqlTemplateException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an instance.
   *
   * @param messageCode the message code
   * @param sql the SQL template
   * @param lineNumber the line number of the error
   * @param columnNumber the column number of the error
   * @param args the message arguments that follow the column number
   */
  public SqlParseException(
      MessageResource messageCode, String sql, int lineNumber, int columnNumber, Object... args) {
    super(messageCode, null, sql, lineNumber, columnNumber, args);
  }
}
