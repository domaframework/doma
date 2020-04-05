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

public class _ParentEntity extends AbstractEntityType<ParentEntity> {

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public DefaultPropertyType<ParentEntity, Integer, Integer> $aaa =
      new DefaultPropertyType<>(
          ParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "aaa",
          "AAA",
          __namingType,
          true,
          true,
          false);

  public DefaultPropertyType<ParentEntity, Integer, Integer> $bbb =
      new DefaultPropertyType<>(
          ParentEntity.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "bbb",
          "BBB",
          __namingType,
          true,
          true,
          false);

  private _ParentEntity() {}

  @Override
  public void saveCurrentStates(ParentEntity entity) {}

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<ParentEntity> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<ParentEntity, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<ParentEntity, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<ParentEntity, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public ParentEntity getOriginalStates(ParentEntity entity) {

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
  public VersionPropertyType<ParentEntity, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<ParentEntity, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(ParentEntity entity, PreDeleteContext<ParentEntity> context) {}

  @Override
  public void preInsert(ParentEntity entity, PreInsertContext<ParentEntity> context) {}

  @Override
  public void preUpdate(ParentEntity entity, PreUpdateContext<ParentEntity> context) {}

  @Override
  public void postDelete(ParentEntity entity, PostDeleteContext<ParentEntity> context) {}

  @Override
  public void postInsert(ParentEntity entity, PostInsertContext<ParentEntity> context) {}

  @Override
  public void postUpdate(ParentEntity entity, PostUpdateContext<ParentEntity> context) {}

  @Override
  public List<EntityPropertyType<ParentEntity, ?>> getIdPropertyTypes() {
    return null;
  }

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _ParentEntity getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public ParentEntity newEntity(Map<String, Property<ParentEntity, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
