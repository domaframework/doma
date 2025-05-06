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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An abstract base class for SQL file-based data modification queries.
 *
 * <p>This class provides common functionality for SQL file-based INSERT, UPDATE, and DELETE
 * operations. It handles SQL file loading, parameter binding, and SQL statement preparation.
 */
public abstract class SqlFileModifyQuery extends AbstractQuery implements ModifyQuery {

  /** Empty string array used as default for property name arrays. */
  protected static final String[] EMPTY_STRINGS = new String[] {};

  /** The SQL kind (INSERT, UPDATE, or DELETE). */
  protected final SqlKind kind;

  /** The path to the SQL file. */
  protected String sqlFilePath;

  /** The parameters to be bound to the SQL statement. */
  protected final Map<String, Value> parameters = new LinkedHashMap<>();

  /** The prepared SQL statement. */
  protected PreparedSql sql;

  /** Whether optimistic lock checking is required for this query. */
  protected boolean optimisticLockCheckRequired;

  /** The SQL log type for this query. */
  protected SqlLogType sqlLogType;

  /** The property names to be included in the operation. */
  protected String[] includedPropertyNames = EMPTY_STRINGS;

  /** The property names to be excluded from the operation. */
  protected String[] excludedPropertyNames = EMPTY_STRINGS;

  /** Whether this query is executable. */
  protected boolean executable;

  /** The cause for skipping SQL execution, if applicable. */
  protected SqlExecutionSkipCause sqlExecutionSkipCause = SqlExecutionSkipCause.STATE_UNCHANGED;

  /**
   * Constructs a new instance with the specified SQL kind.
   *
   * @param kind the SQL kind
   * @throws NullPointerException if the kind is null
   */
  protected SqlFileModifyQuery(SqlKind kind) {
    assertNotNull(kind);
    this.kind = kind;
  }

  /**
   * Prepares the query options.
   *
   * <p>This method sets the query timeout from the configuration if it's not already set.
   */
  protected void prepareOptions() {
    if (queryTimeout <= 0) {
      queryTimeout = config.getQueryTimeout();
    }
  }

  /**
   * Prepares the SQL statement.
   *
   * <p>This method loads the SQL file, evaluates expressions, and builds the prepared SQL statement.
   */
  protected void prepareSql() {
    SqlFile sqlFile =
        config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config,
            kind,
            sqlFile.getPath(),
            evaluator,
            sqlLogType,
            this::expandColumns,
            this::populateValues);
    sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
  }

  /**
   * Expands columns in the SQL statement.
   *
   * <p>This method is called when an expand node is encountered in the SQL file.
   *
   * @param node the expand node
   * @return the expanded column list
   * @throws UnsupportedOperationException if this operation is not supported by the implementing
   *     class
   */
  protected List<String> expandColumns(ExpandNode node) {
    throw new UnsupportedOperationException();
  }

  /**
   * Populates values in the SQL context.
   *
   * <p>This method is called when a populate node is encountered in the SQL file.
   *
   * @param node the populate node
   * @param context the SQL context
   * @throws UnsupportedOperationException if this operation is not supported by the implementing
   *     class
   */
  protected void populateValues(PopulateNode node, SqlContext context) {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the path to the SQL file.
   *
   * @param sqlFilePath the SQL file path
   */
  public void setSqlFilePath(String sqlFilePath) {
    this.sqlFilePath = sqlFilePath;
  }

  /**
   * Adds a parameter to this query.
   *
   * @param name the parameter name
   * @param type the parameter type
   * @param value the parameter value
   * @throws NullPointerException if the name or type is null
   */
  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    addParameterInternal(name, type, value);
  }

  /**
   * Adds a parameter to this query without null checks.
   *
   * <p>This method is used internally when null checks have already been performed.
   *
   * @param name the parameter name
   * @param type the parameter type
   * @param value the parameter value
   */
  public void addParameterInternal(String name, Class<?> type, Object value) {
    parameters.put(name, new Value(type, value));
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
   * Sets the property names to be included in the operation.
   *
   * @param includedPropertyNames the property names to be included
   */
  public void setIncludedPropertyNames(String... includedPropertyNames) {
    this.includedPropertyNames = includedPropertyNames;
  }

  /**
   * Sets the property names to be excluded from the operation.
   *
   * @param excludedPropertyNames the property names to be excluded
   */
  public void setExcludedPropertyNames(String... excludedPropertyNames) {
    this.excludedPropertyNames = excludedPropertyNames;
  }

  /** {@inheritDoc} */
  @Override
  public PreparedSql getSql() {
    return sql;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOptimisticLockCheckRequired() {
    return optimisticLockCheckRequired;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isExecutable() {
    return executable;
  }

  /** {@inheritDoc} */
  @Override
  public SqlExecutionSkipCause getSqlExecutionSkipCause() {
    return sqlExecutionSkipCause;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAutoGeneratedKeysSupported() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  /**
   * Sets the entity and entity type for this query.
   *
   * @param <E> the entity type
   * @param name the parameter name for the entity
   * @param entity the entity
   * @param entityType the entity type
   */
  public abstract <E> void setEntityAndEntityType(String name, E entity, EntityType<E> entityType);

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return sql != null ? sql.toString() : null;
  }
}
