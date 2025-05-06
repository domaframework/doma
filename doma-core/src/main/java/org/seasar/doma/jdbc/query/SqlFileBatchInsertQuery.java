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
import java.sql.Statement;
import java.util.ListIterator;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * A query that performs batch insert operations using an external SQL file.
 *
 * <p>This class extends {@link SqlFileBatchModifyQuery} to provide functionality for executing
 * batch INSERT statements defined in external SQL files. It handles entity lifecycle callbacks and
 * SQL file execution for multiple entities.
 *
 * @param <ELEMENT> the type of elements in the batch
 */
public class SqlFileBatchInsertQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
    implements BatchInsertQuery {

  /** The entity handler for this query. */
  protected EntityHandler entityHandler;

  /**
   * Constructs a new instance with the specified element class.
   *
   * @param elementClass the class of elements in the batch
   */
  public SqlFileBatchInsertQuery(Class<ELEMENT> elementClass) {
    super(elementClass, SqlKind.BATCH_INSERT);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the SQL statements for each entity in the batch, executing
   * pre-insert callbacks.
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
    preInsert();
    prepareSqlFile();
    prepareOptions();
    prepareSql();
    elements.set(0, currentEntity);
    for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preInsert();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
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
   * {@inheritDoc}
   *
   * <p>This implementation does nothing because this query does not support auto-generated keys.
   */
  @Override
  public void generateId(Statement statement, int index) {}

  /**
   * {@inheritDoc}
   *
   * <p>This implementation executes post-insert entity lifecycle callbacks for each entity in the
   * batch.
   */
  @Override
  public void complete() {
    if (entityHandler != null) {
      for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
        currentEntity = it.next();
        entityHandler.postInsert();
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

  /** {@inheritDoc} */
  @Override
  public boolean isBatchSupported() {
    return true;
  }

  /** A handler for entity lifecycle callbacks. */
  protected class EntityHandler {

    /** The entity type. */
    protected final EntityType<ELEMENT> entityType;

    /**
     * Constructs a new instance with the specified entity type.
     *
     * @param entityType the entity type
     * @throws NullPointerException if the entity type is null
     */
    protected EntityHandler(EntityType<ELEMENT> entityType) {
      assertNotNull(entityType);
      this.entityType = entityType;
    }

    /**
     * Executes pre-insert entity lifecycle callbacks.
     *
     * <p>This method creates a pre-insert context and calls the entity type's preInsert method.
     */
    protected void preInsert() {
      SqlFileBatchPreInsertContext<ELEMENT> context =
          new SqlFileBatchPreInsertContext<>(entityType, method, config);
      entityType.preInsert(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    /**
     * Executes post-insert entity lifecycle callbacks.
     *
     * <p>This method creates a post-insert context and calls the entity type's postInsert method.
     */
    protected void postInsert() {
      SqlFileBatchPostInsertContext<ELEMENT> context =
          new SqlFileBatchPostInsertContext<>(entityType, method, config);
      entityType.postInsert(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }
  }

  /**
   * A context for pre-insert entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFileBatchPreInsertContext<E> extends AbstractPreInsertContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPreInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config, DuplicateKeyType.EXCEPTION);
    }
  }

  /**
   * A context for post-insert entity lifecycle callbacks.
   *
   * @param <E> the entity type
   */
  protected static class SqlFileBatchPostInsertContext<E> extends AbstractPostInsertContext<E> {

    /**
     * Constructs a new instance with the specified entity information.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     */
    public SqlFileBatchPostInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config, DuplicateKeyType.EXCEPTION);
    }
  }
}
