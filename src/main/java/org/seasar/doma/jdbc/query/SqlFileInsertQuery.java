package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.Statement;
import org.seasar.doma.internal.jdbc.entity.AbstractPostInsertContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreInsertContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityType;

/** @author taedium */
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
  public <E> E getEntity(Class<E> entityType) {
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
  public <E> void setEntityAndEntityType(String name, E entity, EntityType<E> entityType) {
    entityHandler = new EntityHandler<E>(name, entity, entityType);
  }

  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  protected class EntityHandler<E> {

    protected String name;

    protected E entity;

    protected EntityType<E> entityType;

    protected EntityHandler(String name, E entity, EntityType<E> entityType) {
      assertNotNull(name, entity, entityType);
      this.name = name;
      this.entity = entity;
      this.entityType = entityType;
    }

    protected void preInsert() {
      SqlFilePreInsertContext<E> context =
          new SqlFilePreInsertContext<E>(entityType, method, config);
      entityType.preInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityType.getEntityClass(), entity);
      }
    }

    protected void postInsert() {
      SqlFilePostInsertContext<E> context =
          new SqlFilePostInsertContext<E>(entityType, method, config);
      entityType.postInsert(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
      }
    }
  }

  protected static class SqlFilePreInsertContext<E> extends AbstractPreInsertContext<E> {

    public SqlFilePreInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }

  protected static class SqlFilePostInsertContext<E> extends AbstractPostInsertContext<E> {

    public SqlFilePostInsertContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }
  }
}
