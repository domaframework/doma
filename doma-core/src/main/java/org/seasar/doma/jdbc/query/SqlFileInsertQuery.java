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
import java.sql.Statement;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * A query that inserts data into a database using an external SQL file.
 *
 * <p>This class extends {@link SqlFileModifyQuery} to provide functionality for executing INSERT
 * statements defined in external SQL files. It handles entity lifecycle callbacks and SQL file
 * execution.
 */
public class SqlFileInsertQuery extends SqlFileModifyQuery implements InsertQuery {

  /** Whether to exclude null properties from the insert operation. */
  protected boolean nullExcluded;

  /** The entity handler for this query. */
  protected EntityHandler<?> entityHandler;

  /**
   * Constructs a new instance.
   *
   * <p>This constructor initializes the query with {@link SqlKind#INSERT} to indicate that it
   * performs an INSERT operation.
   */
  public SqlFileInsertQuery() {
    super(SqlKind.INSERT);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statement by processing the SQL file and handling entity
   * lifecycle callbacks.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, sqlFilePath);
    preInsert();
    prepareOptions();
    prepareExecutable();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Executes pre-insert entity lifecycle callbacks.
   *
   * <p>This method delegates to the entity handler if one is available.
   */
  protected void preInsert() {
    if (entityHandler != null) {
      entityHandler.preInsert();
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
   * <p>This implementation does nothing because this query does not support auto-generated keys.
   */
  @Override
  public void generateId(Statement statement) {}

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
   * <p>This implementation executes post-insert entity lifecycle callbacks.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      entityHandler.postInsert();
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
   * Sets whether to exclude null properties from the insert operation.
   *
   * @param nullExcluded whether to exclude null properties
   */
  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  /**
   * A handler for entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected class EntityHandler<E> {

    /** The parameter name for the entity. */
    protected final String name;

    /** The entity being inserted. */
    protected E entity;

    /** The entity type. */
    protected final EntityType<E> entityType;

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param name the parameter name for the entity
     * @param entity the entity being inserted
     * @param entityType the entity type
     * @throws NullPointerException if any parameter is null
     */
    protected EntityHandler(String name, E entity, EntityType<E> entityType) {
      assertNotNull(name, entity, entityType);
      this.name = name;
      this.entity = entity;
      this.entityType = entityType;
    }

    /**
     * Executes pre-insert entity lifecycle callbacks.
     *
     * <p>This method creates a pre-insert context and calls the entity type's preInsert method.
     */
    protected void preInsert() {
      SqlFilePreInsertContext<E> context =
          new SqlFilePreInsertContext<>(entityType, method, config);
      entityType.preInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityType.getEntityClass(), entity);
      }
    }

    /**
     * Executes post-insert entity lifecycle callbacks.
     *
     * <p>This method creates a post-insert context and calls the entity type's postInsert method.
     */
    protected void postInsert() {
      SqlFilePostInsertContext<E> context =
          new SqlFilePostInsertContext<>(entityType, method, config);
      entityType.postInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
      }
    }
  }

  /**
   * A context for pre-insert entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFilePreInsertContext<E> extends AbstractPreInsertContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePreInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config, DuplicateKeyType.EXCEPTION);
    }
  }

  /**
   * A context for post-insert entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFilePostInsertContext<E> extends AbstractPostInsertContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFilePostInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config, DuplicateKeyType.EXCEPTION);
    }
  }
}
