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

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

/**
 * The base interface for all query types.
 *
 * <p>This interface defines the common operations for all database queries in Doma.
 */
public interface Query {

  /**
   * Returns the SQL object that represents the query.
   *
   * @return the SQL object
   */
  Sql<?> getSql();

  /**
   * Returns the class name where the query is defined.
   *
   * @return the class name
   */
  String getClassName();

  /**
   * Returns the method name where the query is defined.
   *
   * @return the method name
   */
  String getMethodName();

  /**
   * Returns the method object where the query is defined.
   *
   * @return the method object
   */
  Method getMethod();

  /**
   * Returns the configuration for this query.
   *
   * @return the configuration
   */
  Config getConfig();

  /**
   * Returns the query timeout in seconds.
   *
   * @return the query timeout
   */
  int getQueryTimeout();

  /** Prepares this query for execution. This method must be called before the query is executed. */
  void prepare();

  /**
   * Completes this query after execution. This method must be called after the query is executed.
   */
  void complete();

  /**
   * Adds a comment to the SQL statement.
   *
   * @param sql the SQL statement
   * @return the SQL statement with the comment
   */
  String comment(String sql);
}
