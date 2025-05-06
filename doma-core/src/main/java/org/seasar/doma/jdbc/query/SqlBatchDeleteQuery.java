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

import org.seasar.doma.jdbc.SqlKind;

/**
 * A query that performs batch delete operations using SQL statements.
 *
 * <p>This class extends {@link SqlBatchModifyQuery} to provide functionality for executing batch
 * DELETE statements. It initializes the SQL kind to {@link SqlKind#DELETE} to indicate that this
 * query performs DELETE operations.
 *
 * @author bakenezumi
 */
public class SqlBatchDeleteQuery extends SqlBatchModifyQuery implements BatchDeleteQuery {

  /**
   * Constructs a new instance.
   *
   * <p>This constructor initializes the query with {@link SqlKind#DELETE} to indicate that it
   * performs DELETE operations.
   */
  public SqlBatchDeleteQuery() {
    super(SqlKind.DELETE);
  }
}
