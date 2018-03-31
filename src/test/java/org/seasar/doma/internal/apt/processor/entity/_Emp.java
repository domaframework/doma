package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.entity.*;

/** @author taedium */
public class _Emp extends AbstractEntityDesc<Emp> {

  @Override
  public String getCatalogName() {

    return null;
  }

  @Override
  public Class<Emp> getEntityClass() {

    return null;
  }

  @Override
  public EntityPropertyDesc<Emp, ?> getEntityPropertyDesc(String name) {

    return null;
  }

  @Override
  public List<EntityPropertyDesc<Emp, ?>> getEntityPropertyDescs() {

    return null;
  }

  @Override
  public GeneratedIdPropertyDesc<Emp, ?, ?> getGeneratedIdPropertyDesc() {

    return null;
  }

  @Override
  public List<EntityPropertyDesc<Emp, ?>> getIdPropertyDescs() {

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
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

    return null;
  }

  @Override
  public VersionPropertyDesc<Emp, ?, ?> getVersionPropertyDesc() {

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
