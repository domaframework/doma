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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An abstract base class for SQL file-based batch modification queries.
 *
 * <p>This class provides common functionality for executing batch operations using SQL files. It
 * handles SQL file loading, parameter binding, and batch execution for INSERT, UPDATE, and DELETE
 * operations.
 *
 * @param <ELEMENT> the type of elements in the batch
 */
public abstract class SqlFileBatchModifyQuery<ELEMENT> extends AbstractQuery
    implements BatchModifyQuery {

  /** Empty string array used as default for property name arrays. */
  protected static final String[] EMPTY_STRINGS = new String[] {};

  /** The class of elements in the batch. */
  protected final Class<ELEMENT> elementClass;

  /** The SQL kind (INSERT, UPDATE, or DELETE). */
  protected final SqlKind kind;

  /** The path to the SQL file. */
  protected String sqlFilePath;

  /** The parameter name used in the SQL file. */
  protected String parameterName;

  /** The SQL file. */
  protected SqlFile sqlFile;

  /** Whether optimistic lock checking is required for this query. */
  protected boolean optimisticLockCheckRequired;

  /** Whether this query is executable. */
  protected boolean executable;

  /** The cause for skipping SQL execution, if applicable. */
  protected SqlExecutionSkipCause sqlExecutionSkipCause =
      SqlExecutionSkipCause.BATCH_TARGET_NONEXISTENT;

  /** The batch size for this query. */
  protected int batchSize;

  /** The SQL log type for this query. */
  protected SqlLogType sqlLogType;

  /** The property names to be included in the operation. */
  protected String[] includedPropertyNames = EMPTY_STRINGS;

  /** The property names to be excluded from the operation. */
  protected String[] excludedPropertyNames = EMPTY_STRINGS;

  /** The elements to be processed in the batch. */
  protected List<ELEMENT> elements;

  /** The current element being processed. */
  protected ELEMENT currentEntity;

  /** The prepared SQL statements for the batch. */
  protected List<PreparedSql> sqls;

  /**
   * Constructs a new instance with the specified element class and SQL kind.
   *
   * @param elementClass the class of elements in the batch
   * @param kind the SQL kind
   */
  protected SqlFileBatchModifyQuery(Class<ELEMENT> elementClass, SqlKind kind) {
    assertNotNull(elementClass, kind);
    this.elementClass = elementClass;
    this.kind = kind;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation validates that required components are not null.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, sqlFilePath, parameterName, elements, sqls);
  }

  /**
   * Prepares the SQL file for this query.
   *
   * <p>This method loads the SQL file from the repository using the configured dialect.
   */
  protected void prepareSqlFile() {
    sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
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
   * Prepares the SQL statement for the current entity.
   *
   * <p>This method creates an expression evaluator and SQL builder to generate the SQL statement
   * for the current entity, then adds it to the list of SQL statements for the batch.
   */
  protected void prepareSql() {
    Value value = new Value(elementClass, currentEntity);
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            Collections.singletonMap(parameterName, value),
            config.getDialect().getExpressionFunctions(),
            config.getClassHelper());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config,
            kind,
            sqlFile.getPath(),
            evaluator,
            sqlLogType,
            this::expandColumns,
            this::populateValues);
    PreparedSql sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
    sqls.add(sql);
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
   * Sets the parameter name used in the SQL file.
   *
   * @param parameterName the parameter name
   */
  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  /**
   * Sets the elements to be processed in the batch.
   *
   * @param elements the elements
   */
  public void setElements(Iterable<ELEMENT> elements) {
    assertNotNull(elements);
    if (elements instanceof Collection<?>) {
      this.elements = new ArrayList<>((Collection<ELEMENT>) elements);
    } else {
      this.elements = new ArrayList<>();
      for (ELEMENT element : elements) {
        this.elements.add(element);
      }
    }
    this.sqls = new ArrayList<>(this.elements.size());
  }

  /**
   * Returns the elements to be processed in the batch.
   *
   * @return the elements
   */
  public List<ELEMENT> getEntities() {
    return elements;
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

  /**
   * Sets the entity type for this query.
   *
   * @param entityType the entity type
   */
  public abstract void setEntityType(EntityType<ELEMENT> entityType);

  /** {@inheritDoc} */
  @Override
  public PreparedSql getSql() {
    return sqls.get(0);
  }

  /** {@inheritDoc} */
  @Override
  public List<PreparedSql> getSqls() {
    return sqls;
  }

  /** {@inheritDoc} */
  @Override
  public Config getConfig() {
    return config;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOptimisticLockCheckRequired() {
    return optimisticLockCheckRequired;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAutoGeneratedKeysSupported() {
    return false;
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
  public int getBatchSize() {
    return batchSize;
  }

  /** {@inheritDoc} */
  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return sqls.toString();
  }
}
