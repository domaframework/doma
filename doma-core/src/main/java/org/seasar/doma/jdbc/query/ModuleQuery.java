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

import org.seasar.doma.jdbc.CallableSql;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A query that calls a database module such as a stored procedure or function.
 *
 * <p>This interface defines operations specific to database module calls.
 */
public interface ModuleQuery extends Query {

  /**
   * Returns the callable SQL for this module query.
   *
   * @return the callable SQL
   */
  @Override
  CallableSql getSql();

  /**
   * Returns the qualified name of the database module.
   *
   * @return the qualified name
   */
  String getQualifiedName();

  /**
   * Returns the SQL log type for this module query.
   *
   * @return the SQL log type
   */
  SqlLogType getSqlLogType();
}
