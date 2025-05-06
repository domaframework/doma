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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.FetchType;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * A query that counts the number of rows that would be returned by a SELECT statement.
 *
 * <p>This class extends {@link AbstractSelectQuery} to provide functionality for transforming a
 * SELECT query into a COUNT query. It uses the database dialect to transform the SQL node for
 * counting.
 */
public class CountQuery extends AbstractSelectQuery {

  /** The SQL node representing the original SELECT query. */
  protected SqlNode sqlNode;

  /** {@inheritDoc} */
  @Override
  public boolean isResultEnsured() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isResultMappingEnsured() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public FetchType getFetchType() {
    return FetchType.LAZY;
  }

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlNode);
  }

  /** {@inheritDoc} */
  @Override
  protected void prepareSql() {
    SqlNode transformedSqlNode = config.getDialect().transformSelectSqlNodeForGettingCount(sqlNode);
    ExpressionEvaluator evaluator = createExpressionEvaluator();
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config,
            SqlKind.SELECT,
            null,
            evaluator,
            sqlLogType,
            this::expandColumns,
            this::populateValues,
            this::expandAggregateColumns);
    sql = sqlBuilder.build(transformedSqlNode, this::comment);
  }

  /** {@inheritDoc} */
  @Override
  public void complete() {
    // do nothing
  }

  /**
   * Sets the SQL node representing the original SELECT query.
   *
   * @param sqlNode the SQL node
   */
  public void setSqlNode(SqlNode sqlNode) {
    this.sqlNode = sqlNode;
  }
}
