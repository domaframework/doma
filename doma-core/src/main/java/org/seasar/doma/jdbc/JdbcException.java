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

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate a JDBC related error during database operations.
 *
 * <p>This is the base exception class for all JDBC-related exceptions in Doma. It provides common
 * functionality for handling SQL-related errors and formatting SQL statements for error messages.
 *
 * <p>Specific subclasses provide more detailed information about particular types of JDBC errors,
 * such as SQL syntax errors, constraint violations, or mapping problems.
 *
 * @see SqlExecutionException
 * @see UniqueConstraintException
 * @see OptimisticLockException
 */
public class JdbcException extends DomaException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new JdbcException with the specified message code and arguments.
   *
   * @param messageCode the message code that identifies the error message
   * @param args the arguments to format the error message
   */
  public JdbcException(MessageResource messageCode, Object... args) {
    super(messageCode, args);
  }

  /**
   * Constructs a new JdbcException with the specified message code, cause, and arguments.
   *
   * @param messageCode the message code that identifies the error message
   * @param cause the cause of this exception
   * @param args the arguments to format the error message
   */
  public JdbcException(MessageResource messageCode, Throwable cause, Object... args) {
    super(messageCode, cause, args);
  }

  /**
   * Chooses the SQL string for logging depending on the log type
   *
   * @param logType the log type
   * @param rawSql the raw SQL string
   * @param formattedSql the formatted SQL string
   * @return the SQL string
   */
  protected static String choiceSql(SqlLogType logType, String rawSql, String formattedSql) {
    switch (logType) {
      case RAW:
        return rawSql;
      case FORMATTED:
        return formattedSql;
      case NONE:
        return "";
      default:
        throw new AssertionError("unreachable");
    }
  }
}
