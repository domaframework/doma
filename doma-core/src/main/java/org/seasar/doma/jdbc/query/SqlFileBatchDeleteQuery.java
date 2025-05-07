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
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * A query that performs batch delete operations using an external SQL file.
 *
 * <p>This class extends {@link SqlFileBatchModifyQuery} to provide functionality for executing
 * batch DELETE statements defined in external SQL files. It handles entity lifecycle callbacks,
 * optimistic locking, and SQL file execution for multiple entities.
 *
 * @param <ELEMENT> the type of elements in the batch
 */
public class SqlFileBatchDeleteQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
    implements BatchDeleteQuery {

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
  public SqlFileBatchDeleteQuery(Class<ELEMENT> elementClass) {
    super(elementClass, SqlKind.BATCH_DELETE);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statements for each entity in the batch, executing
   * pre-delete callbacks and optimistic lock preparation.
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
    preDelete();
    prepareSqlFile();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    elements.set(0, currentEntity);
    for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preDelete();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
  }

  /**
   * Executes pre-delete entity lifecycle callbacks.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void preDelete() {
    if (entityHandler != null) {
      entityHandler.preDelete();
    }
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
   * <p>This implementation executes post-delete entity lifecycle callbacks for each entity in the
   * batch.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
        currentEntity = it.next();
        entityHandler.postDelete();
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
   * Sets whether to ignore the version property for optimistic locking.
   *
   * @param versionIgnored whether to ignore the version property
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  /**
   * Sets whether to suppress optimistic lock exceptions.
   *
   * @param optimisticLockExceptionSuppressed whether to suppress optimistic lock exceptions
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /** A handler for entity lifecycle callbacks and optimistic locking. */
  protected class EntityHandler {

    /** The entity type. */
    protected final EntityType<ELEMENT> entityType;

    /** The version property type for optimistic locking. */
    protected final VersionPropertyType<ELEMENT, ?, ?> versionPropertyType;

    /**
     * Constructs a new instance with the specified entity type.
     *
     * @param entityType the entity type
     */
    protected EntityHandler(EntityType<ELEMENT> entityType) {
      assertNotNull(entityType);
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    /**
     * Executes pre-delete entity lifecycle callbacks.
     *
     * <p>This method creates a pre-delete context and calls the entity type's preDelete method.
     */
    protected void preDelete() {
      SqlFileBatchPreDeleteContext<ELEMENT> context =
          new SqlFileBatchPreDeleteContext<>(entityType, method, config);
      entityType.preDelete(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    /**
     * Executes post-delete entity lifecycle callbacks.
     *
     * <p>This method creates a post-delete context and calls the entity type's postDelete method.
     */
    protected void postDelete() {
      SqlFileBatchPostDeleteContext<ELEMENT> context =
          new SqlFileBatchPostDeleteContext<>(entityType, method, config);
      entityType.postDelete(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
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
  }

  /**
   * A context for pre-delete entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFileBatchPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  /**
   * A context for post-delete entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFileBatchPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
