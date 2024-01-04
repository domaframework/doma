package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.ListIterator;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

public class AutoBatchDeleteQuery<ENTITY> extends AutoBatchModifyQuery<ENTITY>
    implements BatchDeleteQuery {

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public AutoBatchDeleteQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entities, sqls);
    int size = entities.size();
    if (size == 0) {
      return;
    }
    executable = true;
    sqlExecutionSkipCause = null;
    currentEntity = entities.get(0);
    preDelete();
    prepareIdAndVersionPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    entities.set(0, currentEntity);
    for (ListIterator<ENTITY> it = entities.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preDelete();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
  }

  protected void preDelete() {
    AutoBatchPreDeleteContext<ENTITY> context =
        new AutoBatchPreDeleteContext<>(entityType, method, config);
    entityType.preDelete(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
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
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.BATCH_DELETE, sqlLogType);
    builder.appendSql("delete from ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    boolean whereClauseAppended = false;
    if (idPropertyTypes.size() > 0) {
      builder.appendSql(" where ");
      whereClauseAppended = true;
      for (EntityPropertyType<ENTITY, ?> propertyType : idPropertyTypes) {
        Property<ENTITY, ?> property = propertyType.createProperty();
        property.load(currentEntity);
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
      property.load(currentEntity);
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
      property.load(currentEntity);
      builder.appendSql(tenantIdPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      builder.appendSql(" = ");
      builder.appendParameter(property.asInParameter());
    }

    PreparedSql sql = builder.build(this::comment);
    sqls.add(sql);
  }

  @Override
  public void complete() {
    for (ListIterator<ENTITY> it = entities.listIterator(); it.hasNext(); ) {
      currentEntity = it.next();
      postDelete();
      it.set(currentEntity);
    }
  }

  protected void postDelete() {
    AutoBatchPostDeleteContext<ENTITY> context =
        new AutoBatchPostDeleteContext<>(entityType, method, config);
    entityType.postDelete(currentEntity, context);
    if (context.getNewEntity() != null) {
      currentEntity = context.getNewEntity();
    }
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  protected static class AutoBatchPreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    public AutoBatchPreDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  protected static class AutoBatchPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    public AutoBatchPostDeleteContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
