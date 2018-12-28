package org.seasar.doma.internal.apt.dao;

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

public class _Issue214Entity extends AbstractEntityType<Issue214Entity> {

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<Issue214Entity> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<Issue214Entity, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<Issue214Entity, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<Object, Issue214Entity, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public List<EntityPropertyType<Issue214Entity, ?>> getIdPropertyTypes() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public Issue214Entity getOriginalStates(Issue214Entity entity) {

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
  public VersionPropertyType<Object, Issue214Entity, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<Object, Issue214Entity, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(Issue214Entity entity, PreDeleteContext<Issue214Entity> context) {}

  @Override
  public void preInsert(Issue214Entity entity, PreInsertContext<Issue214Entity> context) {}

  @Override
  public void preUpdate(Issue214Entity entity, PreUpdateContext<Issue214Entity> context) {}

  @Override
  public void postDelete(Issue214Entity entity, PostDeleteContext<Issue214Entity> context) {}

  @Override
  public void postInsert(Issue214Entity entity, PostInsertContext<Issue214Entity> context) {}

  @Override
  public void postUpdate(Issue214Entity entity, PostUpdateContext<Issue214Entity> context) {}

  @Override
  public void saveCurrentStates(Issue214Entity entity) {}

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _Issue214Entity getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Issue214Entity newEntity(Map<String, Property<Issue214Entity, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
