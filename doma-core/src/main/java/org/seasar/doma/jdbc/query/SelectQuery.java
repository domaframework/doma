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
package org.seasar.doma.jdbc.query;

import org.seasar.doma.FetchType;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A query for selecting data from a database.
 *
 * <p>This interface defines operations specific to SELECT statements.
 */
public interface SelectQuery extends Query {

  /**
   * Returns the prepared SQL for this select query.
   *
   * @return the prepared SQL
   */
  @Override
  PreparedSql getSql();

  /**
   * Returns the options for this select query.
   *
   * @return the select options
   */
  SelectOptions getOptions();

  /**
   * Returns whether the query is expected to return at least one result.
   *
   * @return {@code true} if the query is expected to return at least one result
   */
  boolean isResultEnsured();

  /**
   * Returns whether the result mapping is ensured.
   *
   * @return {@code true} if the result mapping is ensured
   */
  boolean isResultMappingEnsured();

  /**
   * Returns the fetch type for this query.
   *
   * @return the fetch type
   */
  FetchType getFetchType();

  /**
   * Returns the fetch size for this query.
   *
   * @return the fetch size
   */
  int getFetchSize();

  /**
   * Returns the maximum number of rows to be returned.
   *
   * @return the maximum number of rows
   */
  int getMaxRows();

  /**
   * Returns the SQL log type for this query.
   *
   * @return the SQL log type
   */
  SqlLogType getSqlLogType();

  /**
   * Returns whether the result should be processed as a stream.
   *
   * @return {@code true} if the result should be processed as a stream
   */
  boolean isResultStream();
}
