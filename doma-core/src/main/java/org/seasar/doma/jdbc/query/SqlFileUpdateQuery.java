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
import java.util.List;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * A query that updates data in a database using an external SQL file.
 *
 * <p>This class extends {@link SqlFileModifyQuery} to provide functionality for executing UPDATE
 * statements defined in external SQL files. It handles entity lifecycle callbacks, optimistic
 * locking, and SQL file execution.
 */
public class SqlFileUpdateQuery extends SqlFileModifyQuery implements UpdateQuery {

  /** The entity handler for this query. */
  protected EntityHandler<?> entityHandler;

  /** Whether to exclude null properties from the update operation. */
  protected boolean nullExcluded;

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /** Whether to include properties that haven't changed in the update operation. */
  protected boolean unchangedPropertyIncluded;

  /**
   * Constructs a new instance.
   *
   * <p>This constructor initializes the query with {@link SqlKind#UPDATE} to indicate that it
   * performs an UPDATE operation.
   */
  public SqlFileUpdateQuery() {
    super(SqlKind.UPDATE);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statement by processing the SQL file and handling entity
   * lifecycle callbacks and optimistic locking.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, sqlFilePath);
    initEntityHandler();
    preUpdate();
    prepareOptimisticLock();
    prepareOptions();
    prepareTargetPropertyTypes();
    prepareExecutable();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Initializes the entity handler.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void initEntityHandler() {
    if (entityHandler != null) {
      entityHandler.init();
    }
  }

  /**
   * Executes pre-update entity lifecycle callbacks.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void preUpdate() {
    if (entityHandler != null) {
      entityHandler.preUpdate();
    }
  }

  /**
   * Prepares the target property types for this query.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void prepareTargetPropertyTypes() {
    if (entityHandler != null) {
      entityHandler.prepareTargetPropertyTypes();
    }
  }

  /**
   * Prepares the executable state for this query.
   *
   * <p>This method sets the query as executable if there is no entity handler or if the entity
   * handler has target property types.
   */
  protected void prepareExecutable() {
    if (entityHandler == null || entityHandler.hasTargetPropertyTypes()) {
      executable = true;
      sqlExecutionSkipCause = null;
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation delegates to the entity handler to populate values in the SQL context.
   *
   * @throws UnsupportedOperationException if no entity handler is available
   */
  @Override
  protected void populateValues(PopulateNode node, SqlContext context) {
    if (entityHandler == null) {
      throw new UnsupportedOperationException();
    }
    entityHandler.populateValues(context);
  }

  /**
   * Prepares optimistic locking for this query.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void prepareOptimisticLock() {
    if (entityHandler != null) {
      entityHandler.prepareOptimisticLock();
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation delegates to the entity handler to increment the version property.
   */
  @Override
  public void incrementVersion() {
    if (entityHandler != null) {
      entityHandler.incrementVersion();
    }
  }

  /**
   * Returns the entity associated with this query.
   *
   * @param <E> the entity type
   * @param entityType the entity class
   * @return the entity, or null if no entity is associated with this query
   */
  @SuppressWarnings("unchecked")
  public <E> E getEntity(Class<E> entityType) {
    if (entityHandler != null) {
      return (E) entityHandler.entity;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation executes post-update entity lifecycle callbacks.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      entityHandler.postUpdate();
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation creates an entity handler for the specified entity.
   */
  @Override
  public <E> void setEntityAndEntityType(String name, E entity, EntityType<E> entityType) {
    entityHandler = new EntityHandler<>(name, entity, entityType);
  }

  /**
   * Sets whether to exclude null properties from the update operation.
   *
   * @param nullExcluded whether to exclude null properties
   */
  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  /**
   * Sets whether to ignore the version property for optimistic locking.
   *
   * @param versionIgnored whether to ignore the version property
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  /**
   * Sets whether to suppress optimistic lock exceptions.
   *
   * @param optimisticLockExceptionSuppressed whether to suppress optimistic lock exceptions
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /**
   * Sets whether to include properties that haven't changed in the update operation.
   *
   * @param unchangedPropertyIncluded whether to include unchanged properties
   */
  public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
    this.unchangedPropertyIncluded = unchangedPropertyIncluded;
  }

  /**
   * A handler for entity lifecycle callbacks and optimistic locking.
   *
   * @param <E> the entity type
   */
  protected class EntityHandler<E> {

    /** The parameter name for the entity. */
    protected final String name;

    /** The entity being updated. */
    protected E entity;

    /** The entity type. */
    protected final EntityType<E> entityType;

    /** The version property type for optimistic locking. */
    protected final VersionPropertyType<E, ?, ?> versionPropertyType;

    /** The property types to be updated. */
    protected List<EntityPropertyType<E, ?>> targetPropertyTypes;

    /** The helper for update operations. */
    protected UpdateQueryHelper<E> helper;

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param name the parameter name for the entity
     * @param entity the entity being updated
     * @param entityType the entity type
     * @throws NullPointerException if any parameter is null
     */
    protected EntityHandler(String name, E entity, EntityType<E> entityType) {
      assertNotNull(name, entity, entityType);
      this.name = name;
      this.entity = entity;
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    /**
     * Initializes the update query helper.
     *
     * <p>This method creates a helper with the current configuration and entity information.
     */
    protected void init() {
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
     * Executes pre-update entity lifecycle callbacks.
     *
     * <p>This method creates a pre-update context and calls the entity type's preUpdate method.
     */
    protected void preUpdate() {
      SqlFilePreUpdateContext<E> context =
          new SqlFilePreUpdateContext<>(entityType, method, config);
      entityType.preUpdate(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityType.getEntityClass(), entity);
      }
    }

    /**
     * Prepares the target property types for this query.
     *
     * <p>This method uses the helper to determine which properties should be updated.
     */
    protected void prepareTargetPropertyTypes() {
      targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    }

    /**
     * Returns whether this handler has target property types.
     *
     * @return {@code true} if this handler has target property types, {@code false} otherwise
     */
    protected boolean hasTargetPropertyTypes() {
      return targetPropertyTypes != null && !targetPropertyTypes.isEmpty();
    }

    /**
     * Executes post-update entity lifecycle callbacks.
     *
     * <p>This method creates a post-update context and calls the entity type's postUpdate method.
     */
    protected void postUpdate() {
      SqlFilePostUpdateContext<E> context =
          new SqlFilePostUpdateContext<>(entityType, method, config);
      entityType.postUpdate(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
      }
      entityType.saveCurrentStates(entity);
    }

    /**
     * Prepares optimistic locking for this query.
     *
     * <p>This method sets the optimistic lock check flag based on the version property and
     * configuration.
     */
    protected void prepareOptimisticLock() {
      if (versionPropertyType != null && !versionIgnored) {
        if (!optimisticLockExceptionSuppressed) {
          optimisticLockCheckRequired = true;
        }
      }
    }

    /**
     * Increments the version property for optimistic locking.
     *
     * <p>This method increments the version property if one exists and version checking is not
     * ignored.
     */
    protected void incrementVersion() {
      if (versionPropertyType != null && !versionIgnored) {
        entity = versionPropertyType.increment(entityType, entity);
      }
    }

    /**
     * Populates values in the SQL context.
     *
     * <p>This method uses the helper to populate values for the target properties.
     *
     * @param context the SQL context
     */
    protected void populateValues(SqlContext context) {
      helper.populateValues(entity, targetPropertyTypes, versionPropertyType, context);
    }
  }

  /**
   * A context for pre-update entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFilePreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePreUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns {@code true} because SQL file-based updates always
     * consider the entity changed.
     */
    @Override
    public boolean isEntityChanged() {
      return true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns {@code true} because SQL file-based updates always
     * consider properties changed.
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }

  /**
   * A context for post-update entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFilePostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePostUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns {@code true} because SQL file-based updates always
     * consider properties changed.
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
