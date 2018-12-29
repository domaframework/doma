package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

/** @author taedium */
public class SqlFileUpdateQuery extends SqlFileModifyQuery implements UpdateQuery {

  protected EntityHandler<?> entityHandler;

  protected boolean nullExcluded;

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  protected boolean unchangedPropertyIncluded;

  public SqlFileUpdateQuery() {
    super(SqlKind.UPDATE);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, sqlFilePath);
    initEntityHandler();
    preUpdate();
    prepareOptimisticLock();
    prepareOptions();
    prepareTargetPropertyTypes();
    prepareExecutable();
    prepareSql();
    assertNotNull(sql);
  }

  protected void initEntityHandler() {
    if (entityHandler != null) {
      entityHandler.init();
    }
  }

  protected void preUpdate() {
    if (entityHandler != null) {
      entityHandler.preUpdate();
    }
  }

  protected void prepareTargetPropertyTypes() {
    if (entityHandler != null) {
      entityHandler.prepareTargetPropertyTypes();
    }
  }

  protected void prepareExecutable() {
    if (entityHandler == null || entityHandler.hasTargetPropertyTypes()) {
      executable = true;
      sqlExecutionSkipCause = null;
    }
  }

  @Override
  protected void populateValues(PopulateNode node, SqlContext context) {
    if (entityHandler == null) {
      throw new UnsupportedOperationException();
    }
    entityHandler.populateValues(context);
  }

  protected void prepareOptimisticLock() {
    if (entityHandler != null) {
      entityHandler.prepareOptimisticLock();
    }
  }

  @Override
  public void incrementVersion() {
    if (entityHandler != null) {
      entityHandler.incrementVersion();
    }
  }

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
      entityHandler.postUpdate();
    }
  }

  @Override
  public <E> void setEntityAndEntityType(String name, E entity, EntityType<E> entityType) {
    entityHandler = new EntityHandler<E>(name, entity, entityType);
  }

  public void setNullExcluded(boolean nullExcluded) {
    this.nullExcluded = nullExcluded;
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  public void setUnchangedPropertyIncluded(Boolean unchangedPropertyIncluded) {
    this.unchangedPropertyIncluded = unchangedPropertyIncluded;
  }

  protected class EntityHandler<E> {

    protected String name;

    protected E entity;

    protected EntityType<E> entityType;

    protected VersionPropertyType<? super E, E, ?, ?> versionPropertyType;

    protected List<EntityPropertyType<E, ?>> targetPropertyTypes;

    protected UpdateQueryHelper<E> helper;

    protected EntityHandler(String name, E entity, EntityType<E> entityType) {
      assertNotNull(name, entity, entityType);
      this.name = name;
      this.entity = entity;
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    protected void init() {
      helper =
          new UpdateQueryHelper<E>(
              config,
              entityType,
              includedPropertyNames,
              excludedPropertyNames,
              nullExcluded,
              versionIgnored,
              optimisticLockExceptionSuppressed,
              unchangedPropertyIncluded);
    }

    protected void preUpdate() {
      SqlFilePreUpdateContext<E> context =
          new SqlFilePreUpdateContext<E>(entityType, method, config);
      entityType.preUpdate(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
        addParameterInternal(name, entityType.getEntityClass(), entity);
      }
    }

    protected void prepareTargetPropertyTypes() {
      targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    }

    protected boolean hasTargetPropertyTypes() {
      return targetPropertyTypes != null && !targetPropertyTypes.isEmpty();
    }

    protected void postUpdate() {
      SqlFilePostUpdateContext<E> context =
          new SqlFilePostUpdateContext<E>(entityType, method, config);
      entityType.postUpdate(entity, context);
      if (context.getNewEntity() != null) {
        entity = context.getNewEntity();
      }
      entityType.saveCurrentStates(entity);
    }

    protected void prepareOptimisticLock() {
      if (versionPropertyType != null && !versionIgnored) {
        if (!optimisticLockExceptionSuppressed) {
          optimisticLockCheckRequired = true;
        }
      }
    }

    protected void incrementVersion() {
      if (versionPropertyType != null && !versionIgnored) {
        entity = versionPropertyType.increment(entityType, entity);
      }
    }

    protected void populateValues(SqlContext context) {
      helper.populateValues(entity, targetPropertyTypes, versionPropertyType, context);
    }
  }

  protected static class SqlFilePreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    public SqlFilePreUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    @Override
    public boolean isEntityChanged() {
      return true;
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }

  protected static class SqlFilePostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    public SqlFilePostUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
