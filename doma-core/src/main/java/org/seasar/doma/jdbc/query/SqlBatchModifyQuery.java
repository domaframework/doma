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

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

/**
 * An abstract base class for SQL-based batch modification queries.
 *
 * <p>This class provides common functionality for executing batch operations using SQL statements.
 * It handles SQL node processing, parameter binding, and batch execution for INSERT, UPDATE, and
 * DELETE operations.
 *
 * @author bakenezumi
 */
public abstract class SqlBatchModifyQuery extends AbstractQuery implements BatchModifyQuery {

  /** The SQL kind (INSERT, UPDATE, or DELETE). */
  protected final SqlKind kind;

  /** The SQL node representing the SQL statement. */
  protected SqlNode sqlNode;

  /** The parameters for the SQL statement, mapped by name. */
  protected final Map<String, List<Value>> parameters = new LinkedHashMap<>();

  /** The prepared SQL statements for the batch. */
  protected List<PreparedSql> sqls;

  /** Whether optimistic lock checking is required for this query. */
  protected boolean optimisticLockCheckRequired;

  /** The batch size for this query. */
  protected int batchSize = -1;

  /** The SQL log type for this query. */
  protected SqlLogType sqlLogType;

  /** The size of the parameter lists. */
  protected int parameterSize = -1;

  /**
   * Constructs a new instance with the specified SQL kind.
   *
   * @param kind the SQL kind
   * @throws NullPointerException if kind is null
   */
  protected SqlBatchModifyQuery(SqlKind kind) {
    assertNotNull(kind);
    this.kind = kind;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statements for the batch operation by setting options
   * and building SQL statements.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlNode);
    prepareOptions();
    prepareSql();
    assertNotNull(sqls);
  }

  /**
   * Prepares the query options.
   *
   * <p>This method sets the query timeout and batch size from the configuration if they're not
   * already set.
   */
  protected void prepareOptions() {
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
    if (batchSize <= 0) {
      batchSize = config.getBatchSize();
    }
  }

  /**
   * Prepares the SQL statements for the batch.
   *
   * <p>This method creates a SQL statement for each set of parameter values in the batch.
   */
  protected void prepareSql() {
    sqls = new ArrayList<>();
    IntStream.rangeClosed(0, parameterSize - 1)
        .forEach(
            i -> {
              @SuppressWarnings("serial")
              Map<String, Value> map =
                  new LinkedHashMap<String, Value>() {
                    {
                      parameters.forEach((key, value) -> put(key, value.get(i)));
                    }
                  };
              ExpressionEvaluator evaluator =
                  new ExpressionEvaluator(
                      map, config.getDialect().getExpressionFunctions(), config.getClassHelper());
              NodePreparedSqlBuilder sqlBuilder =
                  new NodePreparedSqlBuilder(config, kind, null, evaluator, sqlLogType);
              PreparedSql sql = sqlBuilder.build(sqlNode, this::comment);
              sqls.add(sql);
            });
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation does nothing because no post-processing is required.
   */
  @Override
  public void complete() {}

  /**
   * Sets the SQL node for this query.
   *
   * @param sqlNode the SQL node
   */
  public void setSqlNode(SqlNode sqlNode) {
    this.sqlNode = sqlNode;
  }

  /**
   * Adds a parameter to this query.
   *
   * <p>This method adds a named parameter with a list of values for batch processing.
   *
   * @param name the parameter name
   * @param type the parameter type
   * @param values the parameter values
   * @throws NullPointerException if any of the arguments are null
   * @throws IllegalArgumentException if the size of the values list doesn't match other parameters
   */
  public void addParameter(String name, Class<?> type, List<?> values) {
    assertNotNull(name, type);
    assertNotNull(values);
    List<Value> valueList = new ArrayList<>();
    for (Object value : values) {
      valueList.add(new Value(type, value));
    }
    if (parameterSize == -1) {
      parameterSize = valueList.size();
    } else {
      assertEquals(parameterSize, valueList.size());
    }
    parameters.put(name, valueList);
  }

  /** Clears all parameters from this query. */
  public void clearParameters() {
    parameters.clear();
  }

  /**
   * Sets the batch size for this query.
   *
   * @param batchSize the batch size
   */
  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  /**
   * Sets the SQL log type for this query.
   *
   * @param sqlLogType the SQL log type
   */
  public void setSqlLogType(SqlLogType sqlLogType) {
    this.sqlLogType = sqlLogType;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the first SQL statement in the batch.
   */
  @Override
  public PreparedSql getSql() {
    return sqls.get(0);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns all SQL statements in the batch.
   */
  @Override
  public List<PreparedSql> getSqls() {
    return sqls;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns whether optimistic lock checking is required for this query.
   */
  @Override
  public boolean isOptimisticLockCheckRequired() {
    return optimisticLockCheckRequired;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation always returns true because SQL batch queries are always executable.
   */
  @Override
  public boolean isExecutable() {
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation always returns null because SQL batch queries are always executable.
   */
  @Override
  public SqlExecutionSkipCause getSqlExecutionSkipCause() {
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation always returns false because SQL batch queries do not support
   * auto-generated keys.
   */
  @Override
  public boolean isAutoGeneratedKeysSupported() {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the batch size for this query.
   */
  @Override
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the SQL log type for this query.
   */
  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation returns the string representation of the SQL statements.
   */
  @Override
  public String toString() {
    return sqls != null ? sqls.toString() : null;
  }
}
