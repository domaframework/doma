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
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/**
 * A query that deletes data from a database using an external SQL file.
 *
 * <p>This class extends {@link SqlFileModifyQuery} to provide functionality for executing DELETE
 * statements defined in external SQL files. It handles entity lifecycle callbacks, optimistic
 * locking, and SQL file execution.
 */
public class SqlFileDeleteQuery extends SqlFileModifyQuery implements DeleteQuery {

  /** The entity handler for this query. */
  protected EntityHandler<?> entityHandler;

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /**
   * Constructs a new instance.
   *
   * <p>This constructor initializes the query with {@link SqlKind#DELETE} to indicate that it
   * performs a DELETE operation.
   */
  public SqlFileDeleteQuery() {
    super(SqlKind.DELETE);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statement by processing the SQL file and handling
   * entity lifecycle callbacks and optimistic locking.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, sqlFilePath);
    preDelete();
    prepareOptions();
    prepareOptimisticLock();
    prepareExecutable();
    prepareSql();
    assertNotNull(sql);
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
   * Prepares the executable state for this query.
   *
   * <p>This method sets the query as executable and clears any skip cause.
   */
  protected void prepareExecutable() {
    executable = true;
    sqlExecutionSkipCause = null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation executes post-delete entity lifecycle callbacks.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      entityHandler.postDelete();
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
   * A handler for entity lifecycle callbacks and optimistic locking.
   *
   * @param <E> the entity type
   */
  protected class EntityHandler<E> {

    /** The parameter name for the entity. */
    protected final String name;

    /** The entity being deleted. */
    protected E entity;

    /** The entity type. */
    protected final EntityType<E> entityType;

    /** The version property type for optimistic locking. */
    protected final VersionPropertyType<E, ?, ?> versionPropertyType;

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param name the parameter name for the entity
     * @param entity the entity being deleted
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
     * Executes pre-delete entity lifecycle callbacks.
     *
     * <p>This method creates a pre-delete context and calls the entity type's preDelete method.
     */
    protected void preDelete() {
      SqlFilePreDeleteContext<E> context =
          new SqlFilePreDeleteContext<>(entityType, method, config);
      entityType.preDelete(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityType.getEntityClass(), entity);
      }
    }

    /**
     * Executes post-delete entity lifecycle callbacks.
     *
     * <p>This method creates a post-delete context and calls the entity type's postDelete method.
     */
    protected void postDelete() {
      SqlFilePostDeleteContext<E> context =
          new SqlFilePostDeleteContext<>(entityType, method, config);
      entityType.postDelete(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
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
  protected static class SqlFilePreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  /**
   * A context for post-delete entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFilePostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
