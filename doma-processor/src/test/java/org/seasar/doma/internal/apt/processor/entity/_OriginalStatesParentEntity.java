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

public class _OriginalStatesParentEntity extends AbstractEntityType<OriginalStatesParentEntity> {

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public DefaultPropertyType<OriginalStatesParentEntity, Integer, Integer> $aaa =
      new DefaultPropertyType<>(
          OriginalStatesParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "aaa",
          "AAA",
          __namingType,
          true,
          true,
          false);

  public DefaultPropertyType<OriginalStatesParentEntity, Integer, Integer> $bbb =
      new DefaultPropertyType<>(
          OriginalStatesParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "bbb",
          "BBB",
          __namingType,
          true,
          true,
          false);

  private _OriginalStatesParentEntity() {}

  @Override
  public void saveCurrentStates(OriginalStatesParentEntity entity) {}

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<OriginalStatesParentEntity> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<OriginalStatesParentEntity, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<OriginalStatesParentEntity, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<OriginalStatesParentEntity, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public OriginalStatesParentEntity getOriginalStates(OriginalStatesParentEntity entity) {

    return null;
  }

  @Override
  public String getSchemaName() {

    return null;
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

    return null;
  }

  @Override
  public VersionPropertyType<OriginalStatesParentEntity, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<OriginalStatesParentEntity, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(
      OriginalStatesParentEntity entity, PreDeleteContext<OriginalStatesParentEntity> context) {}

  @Override
  public void preInsert(
      OriginalStatesParentEntity entity, PreInsertContext<OriginalStatesParentEntity> context) {}

  @Override
  public void preUpdate(
      OriginalStatesParentEntity entity, PreUpdateContext<OriginalStatesParentEntity> context) {}

  @Override
  public void postDelete(
      OriginalStatesParentEntity entity, PostDeleteContext<OriginalStatesParentEntity> context) {}

  @Override
  public void postInsert(
      OriginalStatesParentEntity entity, PostInsertContext<OriginalStatesParentEntity> context) {}

  @Override
  public void postUpdate(
      OriginalStatesParentEntity entity, PostUpdateContext<OriginalStatesParentEntity> context) {}

  @Override
  public List<EntityPropertyType<OriginalStatesParentEntity, ?>> getIdPropertyTypes() {
    return null;
  }

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _OriginalStatesParentEntity getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public OriginalStatesParentEntity newEntity(
      Map<String, Property<OriginalStatesParentEntity, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
