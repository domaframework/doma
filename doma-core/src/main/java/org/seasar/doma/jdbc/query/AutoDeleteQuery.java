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
import java.util.Objects;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * An auto delete query that is executed against an entity class.
 *
 * <p>This class implements {@link DeleteQuery} to provide DELETE operations for entities. It
 * handles version checking for optimistic locking and supports tenant ID-based isolation.
 *
 * @param <ENTITY> the entity type
 */
public class AutoDeleteQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements DeleteQuery {

  /** Whether to ignore the version property for optimistic locking. */
  protected boolean versionIgnored;

  /** Whether to suppress optimistic lock exceptions. */
  protected boolean optimisticLockExceptionSuppressed;

  /**
   * Creates a new instance with the specified entity type.
   *
   * @param entityType the entity type
   */
  public AutoDeleteQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation prepares the query for execution by performing pre-delete operations,
   * preparing special property types, validating ID existence, preparing options and optimistic
   * locking, and building the SQL statement.
   */
  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityType);
    executable = true;
    preDelete();
    prepareSpecialPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    assertNotNull(sql);
  }

  /**
   * Performs pre-delete operations on the entity. This method is called before the entity is
   * deleted from the database.
   */
  protected void preDelete() {
    AutoPreDeleteContext<ENTITY> context =
        new AutoPreDeleteContext<>(entityType, method, config, returning);
    entityType.preDelete(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  /**
   * Prepares optimistic locking for this query. If a version property exists and is not ignored,
   * optimistic lock checking will be enabled unless it is explicitly suppressed.
   */
  protected void prepareOptimisticLock() {
    if (versionPropertyType != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  /**
   * Prepares the SQL statement for this query. This method builds a DELETE statement using the
   * entity type and its properties.
   */
  protected void prepareSql() {
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.DELETE, sqlLogType);
    DeleteAssemblerContext<ENTITY> assemblerContext =
        DeleteAssemblerContextBuilder.build(
            builder,
            entityType,
            config.getNaming(),
            dialect,
            idPropertyTypes,
            versionPropertyType,
            tenantIdPropertyType,
            versionIgnored,
            entity,
            returning);
    DeleteAssembler deleteAssembler = dialect.getDeleteAssembler(assemblerContext);
    deleteAssembler.assemble();
    sql = builder.build(this::comment);
  }

  /**
   * {@inheritDoc}
   *
   * <p>This implementation performs post-delete operations on the entity.
   */
  @Override
  public void complete() {
    postDelete();
  }

  /**
   * Performs post-delete operations on the entity. This method is called after the entity is
   * deleted from the database.
   */
  protected void postDelete() {
    AutoPostDeleteContext<ENTITY> context =
        new AutoPostDeleteContext<>(entityType, method, config, returning);
    entityType.postDelete(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  /**
   * Sets whether to ignore the version property for optimistic locking.
   *
   * @param versionIgnored {@code true} to ignore the version property
   */
  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  /**
   * Sets whether to suppress optimistic lock exceptions.
   *
   * @param optimisticLockExceptionSuppressed {@code true} to suppress optimistic lock exceptions
   */
  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  /**
   * Context class for pre-delete operations.
   *
   * @param <E> the entity type
   */
  protected static class AutoPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    /** The properties to be returned from the DELETE operation. */
    private final ReturningProperties returningProperties;

    /**
     * Creates a new context for pre-delete operations.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     * @param returningProperties the properties to be returned
     */
    public AutoPreDeleteContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    /**
     * {@inheritDoc}
     *
     * @return the properties to be returned from the DELETE operation
     */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }

  /**
   * Context class for post-delete operations.
   *
   * @param <E> the entity type
   */
  protected static class AutoPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    /** The properties to be returned from the DELETE operation. */
    private final ReturningProperties returningProperties;

    /**
     * Creates a new context for post-delete operations.
     *
     * @param entityType the entity type
     * @param method the method
     * @param config the configuration
     * @param returningProperties the properties to be returned
     */
    public AutoPostDeleteContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    /**
     * {@inheritDoc}
     *
     * @return the properties to be returned from the DELETE operation
     */
    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }
}
