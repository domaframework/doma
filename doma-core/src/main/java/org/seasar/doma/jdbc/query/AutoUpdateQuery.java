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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * A query implementation for automatically updating an entity.
 *
 * <p>This class handles the automatic generation of SQL UPDATE statements based on entity
 * definitions and configuration settings. It provides support for optimistic concurrency control
 * through version properties, and allows customization of which properties are included in the
 * update operation.
 *
 * @param <ENTITY> the entity type
 */
public class AutoUpdateQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements UpdateQuery {

  /** Indicates whether null properties should be excluded from the UPDATE statement. */
  protected boolean nullExcluded;

  /** Indicates whether the version property should be ignored for optimistic locking. */
  protected boolean versionIgnored;

  /** Indicates whether optimistic lock exceptions should be suppressed. */
  protected boolean optimisticLockExceptionSuppressed;

  /** Indicates whether unchanged properties should be included in the UPDATE statement. */
  protected boolean unchangedPropertyIncluded;

  /** Helper for building the update query. */
  protected UpdateQueryHelper<ENTITY> helper;

  /**
   * Constructs an instance.
   *
   * @param entityType the entity type
   */
  public AutoUpdateQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method prepares the query for execution by setting up the helper, calling pre-update
   * hooks, preparing property types, validating entity IDs, setting up optimistic locking, and
   * generating the SQL statement.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityType, entity);
    setupHelper();
    preUpdate();
    prepareSpecialPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyTypes();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Sets up the helper for this query.
   *
   * <p>This method initializes the {@link UpdateQueryHelper} with the current configuration and
   * settings for property inclusion/exclusion, null handling, version handling, optimistic lock
   * exception handling, and unchanged property handling.
   */
  protected void setupHelper() {
    helper =
        new UpdateQueryHelper<>(
            config,
            entityType,
            includedPropertyNames,
            excludedPropertyNames,
            nullExcluded,
            versionIgnored,
            optimisticLockExceptionSuppressed,
            unchangedPropertyIncluded);
  }

  /**
   * Executes pre-update processing for the entity.
   *
   * <p>This method calls the entity's pre-update hooks with the appropriate context, allowing the
   * entity to perform any necessary operations before the update is executed. If the pre-update
   * hook returns a new entity instance, it will replace the current one.
   */
  protected void preUpdate() {
    List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    AutoPreUpdateContext<ENTITY> context =
        new AutoPreUpdateContext<>(entityType, method, config, targetPropertyTypes, returning);
    entityType.preUpdate(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  /**
   * Prepares optimistic locking for this query.
   *
   * <p>This method sets up optimistic concurrency control based on the version property and
   * configuration settings. If version checking is enabled and not suppressed, the query will be
   * configured to check for optimistic lock violations.
   */
  protected void prepareOptimisticLock() {
    if (!versionIgnored && versionPropertyType != null) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  /**
   * Prepares the target property types for this query.
   *
   * <p>This method determines which entity properties will be included in the UPDATE statement
   * based on the configuration and entity definition. If there are properties to update, the query
   * is marked as executable.
   */
  protected void prepareTargetPropertyTypes() {
    targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    if (!targetPropertyTypes.isEmpty()) {
      executable = true;
      sqlExecutionSkipCause = null;
    }
  }

  /**
   * Prepares the SQL statement for this query.
   *
   * <p>This method builds the SQL UPDATE statement using the dialect-specific assembler and the
   * configured property types. It creates a context with all necessary information for the
   * assembler to generate the appropriate SQL for the current database dialect.
   */
  protected void prepareSql() {
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.UPDATE, sqlLogType);

    UpdateAssemblerContext<ENTITY> context =
        UpdateAssemblerContextBuilder.build(
            builder,
            entityType,
            config.getNaming(),
            dialect,
            helper,
            idPropertyTypes,
            targetPropertyTypes,
            versionPropertyType,
            tenantIdPropertyType,
            versionIgnored,
            entity,
            returning);
    UpdateAssembler updateAssembler = dialect.getUpdateAssembler(context);
    updateAssembler.assemble();

    sql = builder.build(this::comment);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method increments the version number of the entity if optimistic concurrency control is
   * enabled. The version property is used to detect concurrent modifications and prevent lost
   * updates.
   */
  @Override
  public void incrementVersion() {
    if (!versionIgnored && versionPropertyType != null) {
      entity = versionPropertyType.increment(entityType, entity);
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This method completes the query execution by performing post-update processing.
   */
  @Override
  public void complete() {
    postUpdate();
  }

  /**
   * Executes post-update processing for the entity.
   *
   * <p>This method calls the entity's post-update hooks with the appropriate context, allowing the
   * entity to perform any necessary operations after the update is executed. If the post-update
   * hook returns a new entity instance, it will replace the current one. Finally, the current state
   * of the entity is saved to track future changes.
   */
  protected void postUpdate() {
    List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    if (!versionIgnored && versionPropertyType != null) {
      targetPropertyTypes.add(versionPropertyType);
    }
    AutoPostUpdateContext<ENTITY> context =
        new AutoPostUpdateContext<>(entityType, method, config, targetPropertyTypes, returning);
    entityType.postUpdate(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
    entityType.saveCurrentStates(entity);
  }

  /**
   * Sets whether null properties should be excluded from the UPDATE statement.
   *
   * @param nullExcluded true to exclude null properties, false otherwise
   */
  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  /**
   * Sets whether the version property should be ignored for optimistic locking.
   *
   * <p>This method uses a bitwise OR operation to combine the current setting with the new value,
   * ensuring that once version checking is ignored, it remains ignored.
   *
   * @param versionIgnored true to ignore the version property, false otherwise
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  /**
   * Sets whether optimistic lock exceptions should be suppressed.
   *
   * @param optimisticLockExceptionSuppressed true to suppress optimistic lock exceptions, false
   *     otherwise
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /**
   * Sets whether unchanged properties should be included in the UPDATE statement.
   *
   * @param unchangedPropertyIncluded true to include unchanged properties, false otherwise
   */
  public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
    this.unchangedPropertyIncluded = unchangedPropertyIncluded;
  }

  /**
   * A context class for pre-update processing.
   *
   * <p>This class provides context information for entity pre-update hooks, including which
   * properties are being changed and which properties should be returned.
   *
   * @param <E> the entity type
   */
  protected static class AutoPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    /** The properties to be returned from the update operation. */
    private final ReturningProperties returningProperties;

    /** The names of properties that are being changed in this update. */
    protected final Set<String> changedPropertyNames;

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method that triggered the update
     * @param config the configuration
     * @param targetPropertyTypes the property types targeted for update
     * @param returningProperties the properties to be returned
     */
    public AutoPreUpdateContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        List<EntityPropertyType<E, ?>> targetPropertyTypes,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      assertNotNull(targetPropertyTypes, returningProperties);
      this.returningProperties = returningProperties;
      changedPropertyNames = new HashSet<>(targetPropertyTypes.size());
      for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
        changedPropertyNames.add(propertyType.getName());
      }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The entity is considered changed if there are any properties being updated.
     */
    @Override
    public boolean isEntityChanged() {
      return !changedPropertyNames.isEmpty();
    }

    /**
     * {@inheritDoc}
     *
     * <p>A property is considered changed if it is included in the set of properties targeted for
     * update.
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return changedPropertyNames.contains(propertyName);
    }

    /** {@inheritDoc} */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }

  /**
   * A context class for post-update processing.
   *
   * <p>This class provides context information for entity post-update hooks, including which
   * properties were changed and which properties should be returned.
   *
   * @param <E> the entity type
   */
  protected static class AutoPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    /** The properties to be returned from the update operation. */
    private final ReturningProperties returningProperties;

    /** The names of properties that were changed in this update. */
    protected final Set<String> changedPropertyNames;

    /**
     * Constructs an instance.
     *
     * @param entityType the entity type
     * @param method the method that triggered the update
     * @param config the configuration
     * @param targetPropertyTypes the property types that were updated
     * @param returningProperties the properties to be returned
     */
    public AutoPostUpdateContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        List<EntityPropertyType<E, ?>> targetPropertyTypes,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      assertNotNull(targetPropertyTypes, returningProperties);
      this.returningProperties = returningProperties;
      changedPropertyNames = new HashSet<>(targetPropertyTypes.size());
      for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
        changedPropertyNames.add(propertyType.getName());
      }
    }

    /**
     * {@inheritDoc}
     *
     * <p>A property is considered changed if it was included in the set of properties that were
     * updated.
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return changedPropertyNames.contains(propertyName);
    }

    /** {@inheritDoc} */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }
}
