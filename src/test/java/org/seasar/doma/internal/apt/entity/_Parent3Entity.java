package org.seasar.doma.internal.apt.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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

public class _Parent3Entity extends AbstractEntityType<Parent3Entity> {

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public DefaultPropertyType<Object, Parent3Entity, Integer, Integer> $aaa =
      new DefaultPropertyType<>(
          Parent3Entity.class,
          Integer.class,
          Integer.class,
          () -> new org.seasar.doma.wrapper.IntegerWrapper(),
          null,
          null,
          "aaa",
          "AAA",
          __namingType,
          true,
          true,
          false);

  public DefaultPropertyType<Object, Parent3Entity, Integer, Integer> $bbb =
      new DefaultPropertyType<>(
          Parent3Entity.class,
          Integer.class,
          Integer.class,
          () -> new org.seasar.doma.wrapper.IntegerWrapper(),
          null,
          null,
          "bbb",
          "BBB",
          __namingType,
          true,
          true,
          false);

  private _Parent3Entity() {}

  @Override
  public void saveCurrentStates(Parent3Entity entity) {}

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<Parent3Entity> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<Parent3Entity, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<Parent3Entity, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<Object, Parent3Entity, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public Parent3Entity getOriginalStates(Parent3Entity entity) {

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
  public VersionPropertyType<Object, Parent3Entity, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<Object, Parent3Entity, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(Parent3Entity entity, PreDeleteContext<Parent3Entity> context) {}

  @Override
  public void preInsert(Parent3Entity entity, PreInsertContext<Parent3Entity> context) {}

  @Override
  public void preUpdate(Parent3Entity entity, PreUpdateContext<Parent3Entity> context) {}

  @Override
  public void postDelete(Parent3Entity entity, PostDeleteContext<Parent3Entity> context) {}

  @Override
  public void postInsert(Parent3Entity entity, PostInsertContext<Parent3Entity> context) {}

  @Override
  public void postUpdate(Parent3Entity entity, PostUpdateContext<Parent3Entity> context) {}

  @Override
  public List<EntityPropertyType<Parent3Entity, ?>> getIdPropertyTypes() {
    return null;
  }

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _Parent3Entity getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Parent3Entity newEntity(Map<String, Property<Parent3Entity, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
