package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityDesc;

public class SqlFileInsertQuery extends SqlFileModifyQuery implements InsertQuery {

  protected boolean nullExcluded;

  protected EntityHandler<?> entityHandler;

  public SqlFileInsertQuery() {
    super(SqlKind.INSERT);
  }

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

  protected void preInsert() {
    if (entityHandler != null) {
      entityHandler.preInsert();
    }
  }

  protected void prepareExecutable() {
    executable = true;
    sqlExecutionSkipCause = null;
  }

  @Override
  public void generateId(Statement statement) {}

  @SuppressWarnings("unchecked")
  public <E> E getEntity(Class<E> entityDesc) {
    if (entityHandler != null) {
      return (E) entityHandler.entity;
    }
    return null;
  }

  @Override
  public void complete() {
    if (entityHandler != null) {
      entityHandler.postInsert();
    }
  }

  @Override
  public <E> void setEntityAndEntityDesc(String name, E entity, EntityDesc<E> entityDesc) {
    entityHandler = new EntityHandler<>(name, entity, entityDesc);
  }

  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  protected class EntityHandler<E> {

    protected final String name;

    protected E entity;

    protected final EntityDesc<E> entityDesc;

    protected EntityHandler(String name, E entity, EntityDesc<E> entityDesc) {
      assertNotNull(name, entity, entityDesc);
      this.name = name;
      this.entity = entity;
      this.entityDesc = entityDesc;
    }

    protected void preInsert() {
      SqlFilePreInsertContext<E> context =
          new SqlFilePreInsertContext<>(entityDesc, method, config);
      entityDesc.preInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityDesc.getEntityClass(), entity);
      }
    }

    protected void postInsert() {
      SqlFilePostInsertContext<E> context =
          new SqlFilePostInsertContext<>(entityDesc, method, config);
      entityDesc.postInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
      }
    }
  }

  protected static class SqlFilePreInsertContext<E> extends AbstractPreInsertContext<E> {

    public SqlFilePreInsertContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }

  protected static class SqlFilePostInsertContext<E> extends AbstractPostInsertContext<E> {

    public SqlFilePostInsertContext(EntityDesc<E> entityDesc, Method method, Config config) {
      super(entityDesc, method, config);
    }
  }
}
