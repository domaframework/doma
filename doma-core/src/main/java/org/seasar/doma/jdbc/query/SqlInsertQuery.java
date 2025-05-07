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

import java.sql.Statement;
import org.seasar.doma.jdbc.SqlKind;

/**
 * A query that executes a SQL INSERT statement.
 *
 * <p>This class extends {@link SqlModifyQuery} to provide functionality for executing INSERT
 * statements. It sets the SQL kind to {@link SqlKind#INSERT} to indicate that this query performs
 * an INSERT operation.
 */
public class SqlInsertQuery extends SqlModifyQuery implements InsertQuery {

  /**
   * Constructs a new instance.
   *
   * <p>This constructor initializes the query with {@link SqlKind#INSERT} to indicate that it
   * performs an INSERT operation.
   */
  public SqlInsertQuery() {
    super(SqlKind.INSERT);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation does nothing because this query does not support auto-generated keys.
   */
  @Override
  public void generateId(Statement statement) {}
}
