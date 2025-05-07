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

import java.lang.reflect.Method;
import java.util.ListIterator;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

/**
 * An auto batch delete query that is executed against an entity class.
 *
 * <p>This class implements {@link BatchDeleteQuery} to provide batch DELETE operations for
 * entities. It handles version checking for optimistic locking and supports tenant ID-based
 * isolation.
 *
 * @param <ENTITY> the entity type
 */
public class AutoBatchDeleteQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
    implements BatchDeleteQuery {

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public AutoBatchDeleteQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Prepares this query for execution.
   *
   * <p>This method validates the state of this instance, prepares SQL statements for all entities,
   * and ensures that the number of SQL statements matches the number of entities.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entities, sqls);
    int size = entities.size();
    if (size == 0) {
      return;
    }
    executable = true;
    sqlExecutionSkipCause = null;
    currentEntity = entities.get(0);
    preDelete();
    prepareIdAndVersionPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    entities.set(0, currentEntity);
    for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preDelete();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
  }

  /**
   * Executes pre-delete processing for the current entity.
   *
   * <p>This method creates a context for pre-delete operations and calls the entity type's
   * preDelete method. If a new entity is returned by the context, it replaces the current entity.
   */
  protected void preDelete() {
    AutoBatchPreDeleteContext<ENTITY> context =
        new AutoBatchPreDeleteContext<>(entityType, method, config);
    entityType.preDelete(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  /**
   * Prepares optimistic locking settings for this query.
   *
   * <p>This method sets the optimistic lock check flag based on the version property type
   * and the configuration settings for version handling and exception suppression.
   */
  protected void prepareOptimisticLock() {
    if (versionPropertyType != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  /**
   * Prepares the SQL statement for the current entity.
   *
   * <p>This method builds a DELETE SQL statement with appropriate WHERE clauses for ID properties,
   * version property (for optimistic locking), and tenant ID property (for multi-tenancy).
   * The completed SQL statement is added to the list of SQL statements to be executed.
   */
  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_DELETE, sqlLogType);
    builder.appendSql("delete from ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    boolean whereClauseAppended = false;
    if (idPropertyTypes.size() > 0) {
      builder.appendSql(" where ");
      whereClauseAppended = true;
      for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(currentEntity);
        builder.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
        builder.appendSql(" = ");
        builder.appendParameter(property.asInParameter());
        builder.appendSql(" and ");
      }
      builder.cutBackSql(5);
    }
    if (versionPropertyType != null && !versionIgnored) {
      if (whereClauseAppended) {
        builder.appendSql(" and ");
      } else {
        builder.appendSql(" where ");
        whereClauseAppended = true;
      }
      Property<ENTITY, ?> property = versionPropertyType.createProperty();
      property.load(currentEntity);
      builder.appendSql(versionPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(" = ");
      builder.appendParameter(property.asInParameter());
    }
    if (tenantIdPropertyType != null) {
      if (whereClauseAppended) {
        builder.appendSql(" and ");
      } else {
        builder.appendSql(" where ");
        //noinspection UnusedAssignment
        whereClauseAppended = true;
      }
      Property<ENTITY, ?> property = tenantIdPropertyType.createProperty();
      property.load(currentEntity);
      builder.appendSql(tenantIdPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(" = ");
      builder.appendParameter(property.asInParameter());
    }

    PreparedSql sql = builder.build(this::comment);
    sqls.add(sql);
  }

  /**
   * Completes this query after execution.
   *
   * <p>This method performs post-processing for all entities after the batch delete operation
   * has been executed. It calls the postDelete method for each entity.
   */
  @Override
  public void complete() {
    for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext(); ) {
      currentEntity = it.next();
      postDelete();
      it.set(currentEntity);
    }
  }

  /**
   * Executes post-delete processing for the current entity.
   *
   * <p>This method creates a context for post-delete operations and calls the entity type's
   * postDelete method. If a new entity is returned by the context, it replaces the current entity.
   */
  protected void postDelete() {
    AutoBatchPostDeleteContext<ENTITY> context =
        new AutoBatchPostDeleteContext<>(entityType, method, config);
    entityType.postDelete(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  /**
   * Sets whether to ignore the version property for optimistic locking.
   *
   * @param versionIgnored true to ignore the version property, false otherwise
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  /**
   * Sets whether to suppress optimistic lock exceptions.
   *
   * @param optimisticLockExceptionSuppressed true to suppress optimistic lock exceptions, false otherwise
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /**
   * The context class for pre-delete processing in batch operations.
   *
   * @param <E> the entity type
   */
  protected static class AutoBatchPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public AutoBatchPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  /**
   * The context class for post-delete processing in batch operations.
   *
   * @param <E> the entity type
   */
  protected static class AutoBatchPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public AutoBatchPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
