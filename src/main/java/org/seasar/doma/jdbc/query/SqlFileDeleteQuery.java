package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import org.seasar.doma.internal.jdbc.entity.AbstractPostDeleteContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreDeleteContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

public class SqlFileDeleteQuery extends SqlFileModifyQuery implements DeleteQuery {

  protected EntityHandler<?> entityHandler;

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public SqlFileDeleteQuery() {
    super(SqlKind.DELETE);
  }

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

  protected void preDelete() {
    if (entityHandler != null) {
      entityHandler.preDelete();
    }
  }

  protected void prepareOptimisticLock() {
    if (entityHandler != null) {
      entityHandler.prepareOptimisticLock();
    }
  }

  protected void prepareExecutable() {
    executable = true;
    sqlExecutionSkipCause = null;
  }

  @Override
  public void complete() {
    if (entityHandler != null) {
      entityHandler.postDelete();
    }
  }

  @Override
  public <E> void setEntityAndEntityDesc(String name, E entity, EntityDesc<E> entityDesc) {
    entityHandler = new EntityHandler<>(name, entity, entityDesc);
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored = versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  @SuppressWarnings("unchecked")
  public <E> E getEntity(Class<E> entityDesc) {
    if (entityHandler != null) {
      return (E) entityHandler.entity;
    }
    return null;
  }

  protected class EntityHandler<E> {

    protected final String name;

    protected E entity;

    protected final EntityDesc<E> entityDesc;

    protected final VersionPropertyDesc<E, ?, ?> versionPropertyDesc;

    protected EntityHandler(String name, E entity, EntityDesc<E> entityDesc) {
      assertNotNull(name, entity, entityDesc);
      this.name = name;
      this.entity = entity;
      this.entityDesc = entityDesc;
      this.versionPropertyDesc = entityDesc.getVersionPropertyDesc();
    }

    protected void preDelete() {
      SqlFilePreDeleteContext<E> context =
          new SqlFilePreDeleteContext<>(entityDesc, method, config);
      entityDesc.preDelete(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityDesc.getEntityClass(), entity);
      }
    }

    protected void postDelete() {
      SqlFilePostDeleteContext<E> context =
          new SqlFilePostDeleteContext<>(entityDesc, method, config);
      entityDesc.postDelete(entity, context);
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
  }

  protected static class SqlFilePreDeleteContext<E> extends AbstractPreDeleteContext<E> {

    public SqlFilePreDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }

  protected static class SqlFilePostDeleteContext<E> extends AbstractPostDeleteContext<E> {

    public SqlFilePostDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }
}
