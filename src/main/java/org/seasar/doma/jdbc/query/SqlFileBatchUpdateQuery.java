package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;

public class SqlFileBatchUpdateQuery<ELEMENT> extends SqlFileBatchModifyQuery<ELEMENT>
    implements BatchUpdateQuery {

  protected EntityHandler entityHandler;

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  public SqlFileBatchUpdateQuery(Class<ELEMENT> elementClass) {
    super(elementClass, SqlKind.BATCH_UPDATE);
  }

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
    initEntityHandler();
    preUpdate();
    prepareSqlFile();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyTypes();
    prepareSql();
    elements.set(0, currentEntity);
    for (ListIterator<ELEMENT> it = elements.listIterator(1); it.hasNext(); ) {
      currentEntity = it.next();
      preUpdate();
      prepareSql();
      it.set(currentEntity);
    }
    assertEquals(size, sqls.size());
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
  public void incrementVersions() {
    if (entityHandler != null) {
      entityHandler.incrementVersions();
    }
  }

  @Override
  public void complete() {
    if (entityHandler != null) {
      for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
        currentEntity = it.next();
        entityHandler.postUpdate();
        it.set(currentEntity);
      }
    }
  }

  @Override
  public void setEntityType(EntityType<ELEMENT> entityType) {
    entityHandler = new EntityHandler(entityType);
  }

  public void setVersionIncluded(boolean versionIncluded) {
    this.versionIgnored |= versionIncluded;
  }

  public void setVersionIgnored(boolean versionIgnored) {
    this.versionIgnored |= versionIgnored;
  }

  public void setOptimisticLockExceptionSuppressed(boolean optimisticLockExceptionSuppressed) {
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
  }

  protected class EntityHandler {

    protected EntityType<ELEMENT> entityType;

    protected VersionPropertyType<? super ELEMENT, ELEMENT, ?, ?> versionPropertyType;

    protected List<EntityPropertyType<ELEMENT, ?>> targetPropertyTypes;

    protected BatchUpdateQueryHelper<ELEMENT> helper;

    protected EntityHandler(EntityType<ELEMENT> entityType) {
      assertNotNull(entityType);
      this.entityType = entityType;
      this.versionPropertyType = entityType.getVersionPropertyType();
    }

    protected void init() {
      helper =
          new BatchUpdateQueryHelper<>(
              config,
              entityType,
              includedPropertyNames,
              excludedPropertyNames,
              versionIgnored,
              optimisticLockExceptionSuppressed);
    }

    protected void preUpdate() {
      SqlFileBatchPreUpdateContext<ELEMENT> context =
          new SqlFileBatchPreUpdateContext<ELEMENT>(entityType, method, config);
      entityType.preUpdate(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
    }

    protected void prepareTargetPropertyTypes() {
      targetPropertyTypes = helper.getTargetPropertyTypes();
    }

    protected void postUpdate() {
      SqlFileBatchPostUpdateContext<ELEMENT> context =
          new SqlFileBatchPostUpdateContext<ELEMENT>(entityType, method, config);
      entityType.postUpdate(currentEntity, context);
      if (context.getNewEntity() != null) {
        currentEntity = context.getNewEntity();
      }
      entityType.saveCurrentStates(currentEntity);
    }

    protected void prepareOptimisticLock() {
      if (versionPropertyType != null && !versionIgnored) {
        if (!optimisticLockExceptionSuppressed) {
          optimisticLockCheckRequired = true;
        }
      }
    }

    protected void incrementVersions() {
      if (versionPropertyType != null && !versionIgnored) {
        for (ListIterator<ELEMENT> it = elements.listIterator(); it.hasNext(); ) {
          ELEMENT newEntity = versionPropertyType.increment(entityType, it.next());
          it.set(newEntity);
        }
      }
    }

    protected void populateValues(SqlContext context) {
      helper.populateValues(currentEntity, targetPropertyTypes, versionPropertyType, context);
    }
  }

  protected static class SqlFileBatchPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    public SqlFileBatchPreUpdateContext(EntityType<E> entityType, Method method, Config config) {
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

  protected static class SqlFileBatchPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    public SqlFileBatchPostUpdateContext(EntityType<E> entityType, Method method, Config config) {
      super(entityType, method, config);
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return true;
    }
  }
}
