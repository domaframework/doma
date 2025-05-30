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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A query that creates a database resource.
 *
 * <p>This interface is used to create JDBC resources such as BLOB, CLOB, Array, etc.
 *
 * @param <RESULT> the type of the resource to be created
 */
public interface CreateQuery<RESULT> extends Query {

  /**
   * Creates a database resource using the given connection.
   *
   * @param connection the JDBC connection
   * @return the created resource
   * @throws SQLException if a database access error occurs
   */
  RESULT create(Connection connection) throws SQLException;
}
