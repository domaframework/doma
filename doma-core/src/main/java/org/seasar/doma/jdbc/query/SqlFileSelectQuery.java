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
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/**
 * A query that selects data from a database using an external SQL file.
 *
 * <p>This class extends {@link AbstractSelectQuery} to provide functionality for executing SELECT
 * statements defined in external SQL files. It handles SQL file loading, transformation, and
 * execution.
 */
public class SqlFileSelectQuery extends AbstractSelectQuery {

  /** The path to the SQL file. */
  protected String sqlFilePath;

  /** The SQL file containing the SELECT statement. */
  protected SqlFile sqlFile;

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlFilePath);
  }

  /**
   * Prepares the SQL for this query.
   *
   * <p>This method loads the SQL file, transforms the SQL node using the dialect, and builds the
   * prepared SQL statement.
   */
  protected void prepareSql() {
    sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
    SqlNode transformedSqlNode =
        config.getDialect().transformSelectSqlNode(sqlFile.getSqlNode(), options);
    ExpressionEvaluator evaluator = createExpressionEvaluator();
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config,
            SqlKind.SELECT,
            sqlFilePath,
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
    if (SelectOptionsAccessor.isCount(options)) {
      executeCount(sqlFile.getSqlNode());
    }
  }

  /**
   * Sets the path to the SQL file.
   *
   * @param sqlFilePath the SQL file path
   */
  public void setSqlFilePath(String sqlFilePath) {
    this.sqlFilePath = sqlFilePath;
  }
}
