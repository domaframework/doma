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

/** Represents the settings for a SELECT criteria query. */
public class SelectSettings extends Settings {
  private boolean allowEmptyWhere = true;
  private int fetchSize = 0;
  private int maxRows = 0;

  /**
   * Returns whether the empty WHERE clause is allowed or not.
   *
   * @return whether the empty WHERE clause is allowed or not. The default value is {@literal true}.
   */
  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean getAllowEmptyWhere() {
    return allowEmptyWhere;
  }

  /**
   * Sets whether the empty WHERE clause is allowed or not.
   *
   * <p>If the value is {@literal false} and the WHERE clause is empty, {@link
   * org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException} will be thrown.
   *
   * @param allowEmptyWhere whether the empty WHERE clause is allowed or not
   */
  public void setAllowEmptyWhere(boolean allowEmptyWhere) {
    this.allowEmptyWhere = allowEmptyWhere;
  }

  /**
   * Returns the fetch size.
   *
   * @return the fetch size. The default value is {@literal 0}.
   */
  public int getFetchSize() {
    return fetchSize;
  }

  /**
   * Sets the fetch size.
   *
   * <p>If the value is greater than or equal to 1, it is passed to {@link
   * Statement#setFetchSize(int)}.
   *
   * @param fetchSize the fetch size
   * @see Statement#setFetchSize(int)
   */
  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  /**
   * Returns the maximum number of rows for a {@code ResultSet} object.
   *
   * @return the maximum number of rows. The default value is {@literal 0}
   */
  public int getMaxRows() {
    return maxRows;
  }

  /**
   * Sets the maximum number of rows for a {@code ResultSet} object.
   *
   * <p>If the value is greater than or equal to 1, it is passed to {@link
   * Statement#setMaxRows(int)}.
   *
   * @param maxRows the maximum number of rows
   * @see Statement#setMaxRows(int)
   */
  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }
}
