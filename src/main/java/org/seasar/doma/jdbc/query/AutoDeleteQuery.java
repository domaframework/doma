package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.Property;

public class AutoDeleteQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements DeleteQuery {

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public AutoDeleteQuery(EntityDesc<ENTITY> entityDesc) {
    super(entityDesc);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityDesc);
    executable = true;
    preDelete();
    prepareIdAndVersionPropertyDescs();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareSql();
    assertNotNull(sql);
  }

  protected void preDelete() {
    var context = new AutoPreDeleteContext<>(entityDesc, method, config);
    entityDesc.preDelete(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  protected void prepareOptimisticLock() {
    if (versionPropertyDesc != null && !versionIgnored) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  protected void prepareSql() {
    var naming = config.getNaming();
    var dialect = config.getDialect();
    var builder = new PreparedSqlBuilder(config, SqlKind.DELETE, sqlLogType);
    builder.appendSql("delete from ");
    builder.appendSql(entityDesc.getQualifiedTableName(naming::apply, dialect::applyQuote));
    if (idPropertyDescs.size() > 0) {
      builder.appendSql(" where ");
      for (var propertyDesc : idPropertyDescs) {
        var property = propertyDesc.createProperty();
        property.load(entity);
        builder.appendSql(propertyDesc.getColumnName(naming::apply, dialect::applyQuote));
        builder.appendSql(" = ");
        builder.appendParameter(property.asInParameter());
        builder.appendSql(" and ");
      }
      builder.cutBackSql(5);
    }
    if (versionPropertyDesc != null && !versionIgnored) {
      if (idPropertyDescs.size() == 0) {
        builder.appendSql(" where ");
      } else {
        builder.appendSql(" and ");
      }
      Property<ENTITY, ?> property = versionPropertyDesc.createProperty();
      property.load(entity);
      builder.appendSql(versionPropertyDesc.getColumnName(naming::apply, dialect::applyQuote));
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
    var context = new AutoPostDeleteContext<>(entityDesc, method, config);
    entityDesc.postDelete(entity, context);
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

    public AutoPreDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }

  protected static class AutoPostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    public AutoPostDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }
}
