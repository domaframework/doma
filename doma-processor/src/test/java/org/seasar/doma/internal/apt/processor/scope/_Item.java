package org.seasar.doma.internal.apt.processor.scope;

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

public class _Item extends AbstractEntityType<Item> {

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<Item> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<Item, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<Item, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<Item, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public List<EntityPropertyType<Item, ?>> getIdPropertyTypes() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public Item getOriginalStates(Item entity) {

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
  public VersionPropertyType<Item, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<Item, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(Item entity, PreDeleteContext<Item> context) {}

  @Override
  public void preInsert(Item entity, PreInsertContext<Item> context) {}

  @Override
  public void preUpdate(Item entity, PreUpdateContext<Item> context) {}

  @Override
  public void postDelete(Item entity, PostDeleteContext<Item> context) {}

  @Override
  public void postInsert(Item entity, PostInsertContext<Item> context) {}

  @Override
  public void postUpdate(Item entity, PostUpdateContext<Item> context) {}

  @Override
  public void saveCurrentStates(Item entity) {}

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _Item getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Item newEntity(Map<String, Property<Item, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
