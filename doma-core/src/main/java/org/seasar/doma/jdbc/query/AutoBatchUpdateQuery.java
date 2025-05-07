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
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
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
 * An auto batch update query that is executed against an entity class.
 *
 * <p>This class implements {@link BatchUpdateQuery} to provide batch UPDATE operations for
 * entities. It handles version checking for optimistic locking and supports tenant ID-based
 * isolation.
 *
 * @param <ENTITY> the entity type
 */
public class AutoBatchUpdateQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
    implements BatchUpdateQuery {

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /** Helper for batch update queries. */
  protected BatchUpdateQueryHelper<ENTITY> helper;

  public AutoBatchUpdateQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * Prepares this query for execution.
   * 
   * <p>This method initializes the query by setting up the helper, preparing the entities,
   * and generating SQL statements for each entity in the batch.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entities, sqls);
    int size = entities.size();
    if (size == 0) {
      return;
    }
    currentEntity = entities.get(0);
    setupHelper();
    preUpdate();
    prepareIdAndVersionPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyTypes();
    prepareSql();
    entities.set(0, currentEntity);
    for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preUpdate();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(entities.size(), sqls.size());
  }

  /**
   * Sets up the batch update query helper.
   * 
   * <p>This method initializes the helper with the configuration, entity type,
   * included and excluded property names, and optimistic locking settings.
   */
  protected void setupHelper() {
    helper =
        new BatchUpdateQueryHelper<>(
            config,
            entityType,
            includedPropertyNames,
            excludedPropertyNames,
            versionIgnored,
            optimisticLockExceptionSuppressed);
  }

  /**
   * Executes pre-update processing for the current entity.
   * 
   * <p>This method calls the entity type's preUpdate method with the current entity
   * and updates the current entity if a new entity is returned from the context.
   */
  protected void preUpdate() {
    AutoBatchPreUpdateContext<ENTITY> context =
        new AutoBatchPreUpdateContext<>(entityType, method, config);
    entityType.preUpdate(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  /**
   * Prepares optimistic locking settings for this query.
   * 
   * <p>This method sets the optimistic lock check flag based on the version property type
   * and the version ignored and optimistic lock exception suppressed flags.
   */
  protected void prepareOptimisticLock() {
    if (versionPropertyType != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  /**
   * Prepares the target property types for this query.
   * 
   * <p>This method gets the target property types from the helper and sets the executable flag
   * to true if there are any target properties to update.
   */
  protected void prepareTargetPropertyTypes() {
    targetPropertyTypes = helper.getTargetPropertyTypes();
    if (!targetPropertyTypes.isEmpty()) {
      executable = true;
      sqlExecutionSkipCause = null;
    }
  }

  /**
   * Prepares the SQL statement for the current entity.
   * 
   * <p>This method builds an UPDATE SQL statement with the appropriate SET clause
   * for the target properties and WHERE clause for the ID properties, version property,
   * and tenant ID property as needed.
   */
  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_UPDATE, sqlLogType);
    builder.appendSql("update ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    builder.appendSql(" set ");
    helper.populateValues(currentEntity, targetPropertyTypes, versionPropertyType, builder);
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
   * Increments the version property values of all entities.
   * 
   * <p>This method is called after successful execution of the batch update
   * to increment the version values for optimistic locking.
   */
  @Override
  public void incrementVersions() {
    if (versionPropertyType != null && !versionIgnored) {
      for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext(); ) {
        ENTITY newEntity = versionPropertyType.increment(entityType, it.next());
        it.set(newEntity);
      }
    }
  }

  /**
   * Completes this query by performing post-update processing for all entities.
   * 
   * <p>This method is called after the batch update has been executed
   * to perform any necessary post-processing on the entities.
   */
  @Override
  public void complete() {
    for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext(); ) {
      currentEntity = it.next();
      postUpdate();
      it.set(currentEntity);
    }
  }

  /**
   * Executes post-update processing for the current entity.
   * 
   * <p>This method calls the entity type's postUpdate method with the current entity,
   * updates the current entity if a new entity is returned from the context,
   * and saves the current states of the entity.
   */
  protected void postUpdate() {
    AutoBatchPostUpdateContext<ENTITY> context =
        new AutoBatchPostUpdateContext<>(entityType, method, config);
    entityType.postUpdate(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
    entityType.saveCurrentStates(currentEntity);
  }

  /**
   * Sets whether to ignore the version property for optimistic locking.
   * 
   * <p>If set to {@code true}, the version property will not be used for optimistic locking
   * in the WHERE clause of the UPDATE statement, and the version will not be incremented.
   * 
   * @param versionIgnored whether to ignore the version property
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  /**
   * Sets whether to suppress optimistic lock exceptions.
   * 
   * <p>If set to {@code true}, optimistic lock exceptions will not be thrown
   * even if the version check fails during the update operation.
   * 
   * @param optimisticLockExceptionSuppressed whether to suppress optimistic lock exceptions
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /**
   * A context class for pre-update processing in batch update operations.
   * 
   * <p>This class provides context information for the pre-update phase
   * of batch update operations.
   * 
   * @param <E> the entity type
   */
  protected static class AutoBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    /**
     * Constructs a new instance.
     * 
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public AutoBatchPreUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * Indicates whether the entity has been changed.
     * 
     * <p>This method always returns {@code true} for batch update operations.
     * 
     * @return {@code true}
     */
    @Override
    public boolean isEntityChanged() {
      return true;
    }

    /**
     * Indicates whether the specified property has been changed.
     * 
     * <p>This method always returns {@code true} for batch update operations.
     * 
     * @param propertyName the property name
     * @return {@code true}
     * @throws IllegalArgumentException if the property is not defined in the entity
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }

  /**
   * A context class for post-update processing in batch update operations.
   * 
   * <p>This class provides context information for the post-update phase
   * of batch update operations.
   * 
   * @param <E> the entity type
   */
  protected static class AutoBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    /**
     * Constructs a new instance.
     * 
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public AutoBatchPostUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * Indicates whether the specified property has been changed.
     * 
     * <p>This method always returns {@code true} for batch update operations.
     * 
     * @param propertyName the property name
     * @return {@code true}
     * @throws IllegalArgumentException if the property is not defined in the entity
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
