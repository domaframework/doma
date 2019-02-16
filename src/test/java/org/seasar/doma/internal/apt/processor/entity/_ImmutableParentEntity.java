package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _ImmutableParentEntity extends AbstractEntityType<ImmutableParentEntity> {

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public DefaultPropertyType<ImmutableParentEntity, Integer, Integer> $aaa =
      new DefaultPropertyType<>(
          ImmutableParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new, false),
          "aaa",
          "AAA",
          __namingType,
          true,
          true,
          false);

  public DefaultPropertyType<ImmutableParentEntity, Integer, Integer> $bbb =
      new DefaultPropertyType<>(
          ImmutableParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new, false),
          "bbb",
          "BBB",
          __namingType,
          true,
          true,
          false);

  private _ImmutableParentEntity() {}

  @Override
  public void saveCurrentStates(ImmutableParentEntity entity) {}

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<ImmutableParentEntity> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<ImmutableParentEntity, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<ImmutableParentEntity, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<ImmutableParentEntity, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public ImmutableParentEntity getOriginalStates(ImmutableParentEntity entity) {

    return null;
  }

  @Override
  public String getSchemaName() {

    return null;
  }

  @Override
  public String getTableName() {

    return null;
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

    return null;
  }

  @Override
  public VersionPropertyType<ImmutableParentEntity, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<ImmutableParentEntity, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(
      ImmutableParentEntity entity, PreDeleteContext<ImmutableParentEntity> context) {}

  @Override
  public void preInsert(
      ImmutableParentEntity entity, PreInsertContext<ImmutableParentEntity> context) {}

  @Override
  public void preUpdate(
      ImmutableParentEntity entity, PreUpdateContext<ImmutableParentEntity> context) {}

  @Override
  public void postDelete(
      ImmutableParentEntity entity, PostDeleteContext<ImmutableParentEntity> context) {}

  @Override
  public void postInsert(
      ImmutableParentEntity entity, PostInsertContext<ImmutableParentEntity> context) {}

  @Override
  public void postUpdate(
      ImmutableParentEntity entity, PostUpdateContext<ImmutableParentEntity> context) {}

  @Override
  public List<EntityPropertyType<ImmutableParentEntity, ?>> getIdPropertyTypes() {
    return null;
  }

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _ImmutableParentEntity getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public ImmutableParentEntity newEntity(Map<String, Property<ImmutableParentEntity, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
