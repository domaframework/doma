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
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

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
    AutoPreDeleteContext<ENTITY> context = new AutoPreDeleteContext<>(entityType, method, config);
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
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.DELETE, sqlLogType);
    builder.appendSql("delete from ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    boolean whereClauseAppended = false;
    if (idPropertyTypes.size() > 0) {
      builder.appendSql(" where ");
      whereClauseAppended = true;
      for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(entity);
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
      property.load(entity);
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
      property.load(entity);
      builder.appendSql(tenantIdPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(" = ");
      builder.appendParameter(property.asInParameter());
    }
    sql = builder.build(this::comment);
  }

  @Override
  public void complete() {
    postDelete();
  }

  protected void postDelete() {
    AutoPostDeleteContext<ENTITY> context = new AutoPostDeleteContext<>(entityType, method, config);
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

    public AutoPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  protected static class AutoPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    public AutoPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
