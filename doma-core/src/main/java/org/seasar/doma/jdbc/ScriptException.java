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

/** Thrown to indicate that the one of SQL statements in a script file throws an exception. */
public class ScriptException extends JdbcException {

  private static final long serialVersionUID = 1L;

  protected final String rawSql;

  protected final String scriptFilePath;

  protected final int lineNumber;

  public ScriptException(Throwable cause, Sql<?> sql, int lineNumber) {
    this(cause, sql.getRawSql(), sql.getSqlFilePath(), lineNumber);
  }

  public ScriptException(Throwable cause, String rawSql, String scriptFilePath, int lineNumber) {
    super(Message.DOMA2077, cause, rawSql, scriptFilePath, lineNumber, cause);
    this.rawSql = rawSql;
    this.scriptFilePath = scriptFilePath;
    this.lineNumber = lineNumber;
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
   * Returns the script file path.
   *
   * @return the script file path
   */
  public String getScriptFilePath() {
    return scriptFilePath;
  }

  /**
   * Returns the line number of the script file.
   *
   * @return the line number
   */
  public int getLineNumber() {
    return lineNumber;
  }
}
