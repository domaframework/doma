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

public class AutoDeleteQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements DeleteQuery {

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public AutoDeleteQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

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

  protected void preDelete() {
    AutoPreDeleteContext<ENTITY> context =
        new AutoPreDeleteContext<>(entityType, method, config, returning);
    entityType.preDelete(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  protected void prepareOptimisticLock() {
    if (versionPropertyType != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

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

  @Override
  public void complete() {
    postDelete();
  }

  protected void postDelete() {
    AutoPostDeleteContext<ENTITY> context =
        new AutoPostDeleteContext<>(entityType, method, config, returning);
    entityType.postDelete(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  protected static class AutoPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    private final ReturningProperties returningProperties;

    public AutoPreDeleteContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }

  protected static class AutoPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    private final ReturningProperties returningProperties;

    public AutoPostDeleteContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        ReturningProperties returningProperties) {
      super(entityType, method, config);
      this.returningProperties = Objects.requireNonNull(returningProperties);
    }

    @Override
    public ReturningProperties getReturningProperties() {
      return returningProperties;
    }
  }
}
