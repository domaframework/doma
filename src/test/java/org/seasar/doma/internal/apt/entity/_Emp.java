package org.seasar.doma.internal.apt.entity;

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

/** @author taedium */
public class _Emp extends AbstractEntityType<Emp> {

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<Emp> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyType<Emp, ?> getEntityPropertyType(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyType<Emp, ?>> getEntityPropertyTypes() {

    return null;
  }

  @Override
  public GeneratedIdPropertyType<Object, Emp, ?, ?> getGeneratedIdPropertyType() {

    return null;
  }

  @Override
  public List<EntityPropertyType<Emp, ?>> getIdPropertyTypes() {

    return null;
  }

  @Override
  public String getName() {

    return null;
  }

  @Override
  public Emp getOriginalStates(Emp entity) {

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
  public VersionPropertyType<Object, Emp, ?, ?> getVersionPropertyType() {

    return null;
  }

  @Override
  public TenantIdPropertyType<Object, Emp, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public void preDelete(Emp entity, PreDeleteContext<Emp> context) {}

  @Override
  public void preInsert(Emp entity, PreInsertContext<Emp> context) {}

  @Override
  public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {}

  @Override
  public void postDelete(Emp entity, PostDeleteContext<Emp> context) {}

  @Override
  public void postInsert(Emp entity, PostInsertContext<Emp> context) {}

  @Override
  public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {}

  @Override
  public void saveCurrentStates(Emp entity) {}

  @Override
  public NamingType getNamingType() {
    return null;
  }

  public static _Emp getSingletonInternal() {
    return null;
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Emp newEntity(Map<String, Property<Emp, ?>> args) {
    return null;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }
}
