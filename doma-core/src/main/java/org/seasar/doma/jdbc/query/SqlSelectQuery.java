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

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * A query that executes a SQL SELECT statement.
 *
 * <p>This class extends {@link AbstractSelectQuery} to provide functionality for executing SELECT
 * statements. It handles SQL node transformation, expression evaluation, and SQL statement
 * preparation.
 */
public class SqlSelectQuery extends AbstractSelectQuery {

  /** The SQL node representing the query. */
  protected SqlNode sqlNode;

  /**
   * {@inheritDoc}
   *
   * <p>This implementation validates that the SQL node is not null.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlNode);
  }

  /**
   * Prepares the SQL statement.
   *
   * <p>This method transforms the SQL node using the dialect, evaluates expressions, and builds the
   * prepared SQL statement.
   */
  protected void prepareSql() {
    SqlNode transformedSqlNode = config.getDialect().transformSelectSqlNode(sqlNode, options);
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

  /**
   * {@inheritDoc}
   *
   * <p>This implementation executes a count query if the select options indicate a count operation.
   */
  @Override
  public void complete() {
    if (SelectOptionsAccessor.isCount(options)) {
      executeCount(sqlNode);
    }
  }

  /**
   * Sets the SQL node for this query.
   *
   * @param sqlNode the SQL node
   */
  public void setSqlNode(SqlNode sqlNode) {
    this.sqlNode = sqlNode;
  }
}
