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
package org.seasar.doma.jdbc.criteria.context;

import java.sql.Statement;
import java.util.Objects;
import org.seasar.doma.jdbc.SqlLogType;

/** Represents the settings for a criteria query. */
public class Settings {
  private String comment;
  private SqlLogType sqlLogType = SqlLogType.FORMATTED;
  private int queryTimeout = 0;

  /**
   * Returns the comment for the SQL statement.
   *
   * @return the comment. The default value is {@literal null}.
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment for the SQL statement.
   *
   * <p>The implementation of {@link org.seasar.doma.jdbc.Commenter} can use this value.
   *
   * @param comment the comment for the SQL statement
   */
  public void setComment(String comment) {
    Objects.requireNonNull(comment);
    this.comment = comment;
  }

  /**
   * Returns the SQL log type.
   *
   * @return the SQL log type. The default value is {@link SqlLogType#FORMATTED }
   */
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  /**
   * Sets the SQL log type.
   *
   * <p>Specify {@link SqlLogType#RAW } or {@link SqlLogType#NONE } for the sensitive SQL statement.
   *
   * @param sqlLogType the SQL log type
   */
  public void setSqlLogType(SqlLogType sqlLogType) {
    Objects.requireNonNull(sqlLogType);
    this.sqlLogType = sqlLogType;
  }

  /**
   * Returns the query timeout limit in seconds.
   *
   * @return the query timeout limit in seconds. The default value is {@literal 0}.
   */
  public int getQueryTimeout() {
    return queryTimeout;
  }

  /**
   * Sets the query timeout limit in seconds.
   *
   * @param queryTimeout the query timeout limit in seconds. If the value is greater than or equal
   *     to 1, it is passed to {@link Statement#setQueryTimeout(int)}.
   * @see Statement#setQueryTimeout(int)
   */
  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }
}
