package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.seasar.doma.internal.jdbc.entity.AbstractPostUpdateContext;
import org.seasar.doma.internal.jdbc.entity.AbstractPreUpdateContext;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

public class AutoUpdateQuery<ENTITY> extends AutoModifyQuery<ENTITY> implements UpdateQuery {

  protected boolean nullExcluded;

  protected boolean versionIgnored;

  protected boolean optimisticLockExceptionSuppressed;

  protected boolean unchangedPropertyIncluded;

  protected UpdateQueryHelper<ENTITY> helper;

  public AutoUpdateQuery(EntityType<ENTITY> entityType) {
    super(entityType);
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(method, entityType, entity);
    setupHelper();
    preUpdate();
    prepareSpecialPropertyTypes();
    validateIdExistent();
    prepareOptions();
    prepareOptimisticLock();
    prepareTargetPropertyTypes();
    prepareSql();
    assertNotNull(sql);
  }

  protected void setupHelper() {
    helper =
        new UpdateQueryHelper<>(
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
    List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    AutoPreUpdateContext<ENTITY> context =
        new AutoPreUpdateContext<>(entityType, method, config, targetPropertyTypes);
    entityType.preUpdate(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
  }

  protected void prepareOptimisticLock() {
    if (!versionIgnored && versionPropertyType != null) {
      if (!optimisticLockExceptionSuppressed) {
        optimisticLockCheckRequired = true;
      }
    }
  }

  protected void prepareTargetPropertyTypes() {
    targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    if (!targetPropertyTypes.isEmpty()) {
      executable = true;
      sqlExecutionSkipCause = null;
    }
  }

  protected void prepareSql() {
    Naming naming = config.getNaming();
    Dialect dialect = config.getDialect();
    PreparedSqlBuilder builder = new PreparedSqlBuilder(config, SqlKind.UPDATE, sqlLogType);
    builder.appendSql("update ");
    builder.appendSql(entityType.getQualifiedTableName(naming::apply, dialect::applyQuote));
    builder.appendSql(" set ");
    helper.populateValues(entity, targetPropertyTypes, versionPropertyType, builder);
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
    if (!versionIgnored && versionPropertyType != null) {
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
  public void incrementVersion() {
    if (!versionIgnored && versionPropertyType != null) {
      entity = versionPropertyType.increment(entityType, entity);
    }
  }

  @Override
  public void complete() {
    postUpdate();
  }

  protected void postUpdate() {
    List<EntityPropertyType<ENTITY, ?>> targetPropertyTypes = helper.getTargetPropertyTypes(entity);
    if (!versionIgnored && versionPropertyType != null) {
      targetPropertyTypes.add(versionPropertyType);
    }
    AutoPostUpdateContext<ENTITY> context =
        new AutoPostUpdateContext<>(entityType, method, config, targetPropertyTypes);
    entityType.postUpdate(entity, context);
    if (context.getNewEntity() != null) {
      entity = context.getNewEntity();
    }
    entityType.saveCurrentStates(entity);
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

  protected static class AutoPreUpdateContext<E> extends AbstractPreUpdateContext<E> {

    protected final Set<String> changedPropertyNames;

    public AutoPreUpdateContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        List<EntityPropertyType<E, ?>> targetPropertyTypes) {
      super(entityType, method, config);
      assertNotNull(targetPropertyTypes);
      changedPropertyNames = new HashSet<>(targetPropertyTypes.size());
      for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
        changedPropertyNames.add(propertyType.getName());
      }
    }

    @Override
    public boolean isEntityChanged() {
      return !changedPropertyNames.isEmpty();
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return changedPropertyNames.contains(propertyName);
    }
  }

  protected static class AutoPostUpdateContext<E> extends AbstractPostUpdateContext<E> {

    protected final Set<String> changedPropertyNames;

    public AutoPostUpdateContext(
        EntityType<E> entityType,
        Method method,
        Config config,
        List<EntityPropertyType<E, ?>> targetPropertyTypes) {
      super(entityType, method, config);
      assertNotNull(targetPropertyTypes);
      changedPropertyNames = new HashSet<>(targetPropertyTypes.size());
      for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
        changedPropertyNames.add(propertyType.getName());
      }
    }

    @Override
    public boolean isPropertyChanged(String propertyName) {
      validatePropertyDefined(propertyName);
      return changedPropertyNames.contains(propertyName);
    }
  }
}
