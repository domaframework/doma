package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.*;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _ParentEntity extends AbstractEntityDesc<ParentEntity> {

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public DefaultPropertyDesc<ParentEntity, Integer, Integer> $aaa =
      new DefaultPropertyDesc<>(
          ParentEntity.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
          "aaa",
          "AAA",
          __namingType,
          true,
          true,
          false);

  public DefaultPropertyDesc<ParentEntity, Integer, Integer> $bbb =
      new DefaultPropertyDesc<>(
          ParentEntity.class,
          () -> new BasicScalar<>(new IntegerWrapper(), false),
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
  public EntityPropertyDesc<ParentEntity, ?> getEntityPropertyDesc(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyDesc<ParentEntity, ?>> getEntityPropertyDescs() {

    return null;
  }

  @Override
  public GeneratedIdPropertyDesc<ParentEntity, ?, ?> getGeneratedIdPropertyDesc() {

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
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

    return null;
  }

  @Override
  public VersionPropertyDesc<ParentEntity, ?, ?> getVersionPropertyDesc() {

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
  public List<EntityPropertyDesc<ParentEntity, ?>> getIdPropertyDescs() {
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
