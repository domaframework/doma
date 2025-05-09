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
package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.BiFunction;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;

/**
 * A command to process SQL with a custom handler.
 *
 * <p>This command allows custom processing of SQL statements using a provided handler function.
 *
 * @param <RESULT> the result type
 */
public class SqlProcessorCommand<RESULT> implements Command<RESULT> {

  /** the query */
  protected final SqlProcessorQuery query;

  /** the handler function that processes the SQL */
  protected final BiFunction<Config, PreparedSql, RESULT> handler;

  /**
   * Creates a new instance.
   *
   * @param query the SQL processor query
   * @param handler the handler function that processes the SQL
   */
  public SqlProcessorCommand(
      SqlProcessorQuery query, BiFunction<Config, PreparedSql, RESULT> handler) {
    assertNotNull(query, handler);
    this.query = query;
    this.handler = handler;
  }

  /**
   * Returns the query.
   *
   * @return the query
   */
  @Override
  public SqlProcessorQuery getQuery() {
    return query;
  }

  /**
   * Executes the SQL processing.
   *
   * @return the result of the SQL processing
   */
  @Override
  public RESULT execute() {
    return handler.apply(query.getConfig(), query.getSql());
  }
}
