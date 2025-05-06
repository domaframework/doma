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
import java.util.List;
import java.util.ListIterator;
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
 * A query that performs batch update operations using an external SQL file.
 *
 * <p>This class extends {@link SqlFileBatchModifyQuery} to provide functionality for executing
 * batch UPDATE statements defined in external SQL files. It handles entity lifecycle callbacks,
 * optimistic locking, version incrementation, and SQL file execution for multiple entities.
 *
 * @param <ELEMENT> the type of elements in the batch
 */
public class SqlFileBatchUpdateQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
    implements BatchUpdateQuery {

  /** The entity handler for this query. */
  protected EntityHandler entityHandler;

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /**
   * Constructs a new instance with the specified element class.
   *
   * @param elementClass the class of elements in the batch
   */
  public SqlFileBatchUpdateQuery(Class<ELEMENT> elementClass) {
    super(elementClass, SqlKind.BATCH_UPDATE);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statements for each entity in the batch, executing
   * pre-update callbacks, optimistic lock preparation, and target property type preparation.
   */
  @Override
  public void prepare() {
    super.prepare();
    int size = elements.size();
    if (size == 0) {
      return;
    }
    executable = true;
    sqlExecutionSkipCause = null;
    currentEntity = elements.get(0);
    initEntityHandler();
    preUpdate();
    prepareSqlFile();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyTypes();
    prepareSql();
    elements.set(0, currentEntity);
    for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preUpdate();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
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
   * {@inheritDoc}
   *
   * <p>This implementation delegates to the entity handler to populate values in the SQL context.
   *
   * @throws UnsupportedOperationException if the entity handler is not available
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
   * <p>This implementation delegates to the entity handler to increment version values for
   * optimistic locking.
   */
  @Override
  public void incrementVersions() {
    if (entityHandler != null) {
      entityHandler.incrementVersions();
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation executes post-update entity lifecycle callbacks for each entity in the
   * batch.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
        currentEntity = it.next();
        entityHandler.postUpdate();
        it.set(currentEntity);
      }
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation creates an entity handler for the specified entity type.
   */
  @Override
  public void setEntityType(EntityType<ELEMENT> entityType) {
    entityHandler = new EntityHandler(entityType);
  }

  /**
   * Sets whether to include the version property in the update.
   *
   * @param versionIncluded whether to include the version property
   */
  public void setVersionIncluded(boolean versionIncluded) {
    this.versionIgnored |= versionIncluded;
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

  /** A handler for entity lifecycle callbacks, optimistic locking, and value population. */
  protected class EntityHandler {

    /** The entity type. */
    protected final EntityType<ELEMENT> entityType;

    /** The version property type for optimistic locking. */
    protected final VersionPropertyType<ELEMENT, ?, ?> versionPropertyType;

    /** The property types to be included in the update. */
    protected List<EntityPropertyType<ELEMENT, ?>> targetPropertyTypes;

    /** The helper for batch update operations. */
    protected BatchUpdateQueryHelper<ELEMENT> helper;

    /**
     * Constructs a new instance with the specified entity type.
     *
     * @param entityType the entity type
     * @throws NullPointerException if the entity type is null
     */
    protected EntityHandler(EntityType<ELEMENT> entityType) {
      assertNotNull(entityType);
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    /**
     * Initializes this handler.
     *
     * <p>This method creates a batch update query helper with the configured properties.
     */
    protected void init() {
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
     * Executes pre-update entity lifecycle callbacks.
     *
     * <p>This method creates a pre-update context and calls the entity type's preUpdate method.
     */
    protected void preUpdate() {
      SqlFileBatchPreUpdateContext<ELEMENT> context =
          new SqlFileBatchPreUpdateContext<>(entityType, method, config);
      entityType.preUpdate(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    /**
     * Prepares the target property types for this query.
     *
     * <p>This method retrieves the target property types from the helper.
     */
    protected void prepareTargetPropertyTypes() {
      targetPropertyTypes = helper.getTargetPropertyTypes();
    }

    /**
     * Executes post-update entity lifecycle callbacks.
     *
     * <p>This method creates a post-update context, calls the entity type's postUpdate method, and
     * saves the current state of the entity.
     */
    protected void postUpdate() {
      SqlFileBatchPostUpdateContext<ELEMENT> context =
          new SqlFileBatchPostUpdateContext<>(entityType, method, config);
      entityType.postUpdate(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
      entityType.saveCurrentStates(currentEntity);
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
     * Increments version values for optimistic locking.
     *
     * <p>This method increments the version property value for each entity in the batch if
     * optimistic locking is enabled.
     */
    protected void incrementVersions() {
      if (versionPropertyType != null && !versionIgnored) {
        for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
          ELEMENT newEntity = versionPropertyType.increment(entityType, it.next());
          it.set(newEntity);
        }
      }
    }

    /**
     * Populates values in the SQL context.
     *
     * <p>This method delegates to the helper to populate values for the current entity.
     *
     * @param context the SQL context
     */
    protected void populateValues(SqlContext context) {
      helper.populateValues(currentEntity, targetPropertyTypes, versionPropertyType, context);
    }
  }

  /**
   * A context for pre-update entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFileBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPreUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns true for batch operations.
     */
    @Override
    public boolean isEntityChanged() {
      return true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns true for batch operations.
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
  protected static class SqlFileBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPostUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation always returns true for batch operations.
     */
    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
