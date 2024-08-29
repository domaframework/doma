package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
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

public class _ImmutableUser extends AbstractEntityType<ImmutableUser> {

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<ImmutableUser> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<ImmutableUser, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<ImmutableUser, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<ImmutableUser, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public List<EntityPropertyType<ImmutableUser, ?>> getIdPropertyTypes() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public ImmutableUser getOriginalStates(ImmutableUser entity) {

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
  public VersionPropertyType<ImmutableUser, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<ImmutableUser, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(ImmutableUser entity, PreDeleteContext<ImmutableUser> context) {}

  @Override
  public void preInsert(ImmutableUser entity, PreInsertContext<ImmutableUser> context) {}

  @Override
  public void preUpdate(ImmutableUser entity, PreUpdateContext<ImmutableUser> context) {}

  @Override
  public void postDelete(ImmutableUser entity, PostDeleteContext<ImmutableUser> context) {}

  @Override
  public void postInsert(ImmutableUser entity, PostInsertContext<ImmutableUser> context) {}

  @Override
  public void postUpdate(ImmutableUser entity, PostUpdateContext<ImmutableUser> context) {}

  @Override
  public void saveCurrentStates(ImmutableUser entity) {}

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _ImmutableUser getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public ImmutableUser newEntity(Map<String, Property<ImmutableUser, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
