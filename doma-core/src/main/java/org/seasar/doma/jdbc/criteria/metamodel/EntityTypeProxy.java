package org.seasar.doma.jdbc.criteria.metamodel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
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

public class EntityTypeProxy<ENTITY> implements EntityType<ENTITY> {

  private final EntityType<ENTITY> entityType;
  private final String qualifiedTableName;

  public EntityTypeProxy(EntityType<ENTITY> entityType, String qualifiedTableName) {
    this.entityType = Objects.requireNonNull(entityType);
    this.qualifiedTableName = Objects.requireNonNull(qualifiedTableName);
    if (qualifiedTableName.contains("'")) {
      throw new DomaIllegalArgumentException(
          "qualifiedTableName", "The qualifiedTableName must not contain the single quotation(').");
    }
    if (qualifiedTableName.contains(";")) {
      throw new DomaIllegalArgumentException(
          "qualifiedTableName", "The qualifiedTableName must not contain the semicolon(;).");
    }
    if (qualifiedTableName.contains("--")) {
      throw new DomaIllegalArgumentException(
          "qualifiedTableName", "The qualifiedTableName must not contain the two hyphens(--).");
    }
    if (qualifiedTableName.contains("/")) {
      throw new DomaIllegalArgumentException(
          "qualifiedTableName", "The qualifiedTableName must not contain the slash(/).");
    }
  }

  @Override
  public boolean isImmutable() {
    return entityType.isImmutable();
  }

  @Override
  public String getName() {
    return entityType.getName();
  }

  @Override
  public String getCatalogName() {
    return entityType.getCatalogName();
  }

  @Override
  public String getSchemaName() {
    return entityType.getSchemaName();
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {
    return entityType.getTableName(namingFunction);
  }

  @Override
  public String getQualifiedTableName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction) {
    return qualifiedTableName;
  }

  @Override
  public boolean isQuoteRequired() {
    return entityType.isQuoteRequired();
  }

  @Override
  public NamingType getNamingType() {
    return entityType.getNamingType();
  }

  @Override
  public GeneratedIdPropertyType<ENTITY, ?, ?> getGeneratedIdPropertyType() {
    return entityType.getGeneratedIdPropertyType();
  }

  @Override
  public VersionPropertyType<ENTITY, ?, ?> getVersionPropertyType() {
    return entityType.getVersionPropertyType();
  }

  @Override
  public TenantIdPropertyType<ENTITY, ?, ?> getTenantIdPropertyType() {
    return entityType.getTenantIdPropertyType();
  }

  @Override
  public List<EntityPropertyType<ENTITY, ?>> getIdPropertyTypes() {
    return entityType.getIdPropertyTypes();
  }

  @Override
  public EntityPropertyType<ENTITY, ?> getEntityPropertyType(String __name) {
    return entityType.getEntityPropertyType(__name);
  }

  @Override
  public List<EntityPropertyType<ENTITY, ?>> getEntityPropertyTypes() {
    return entityType.getEntityPropertyTypes();
  }

  @Override
  public ENTITY newEntity(Map<String, Property<ENTITY, ?>> __args) {
    return entityType.newEntity(__args);
  }

  @Override
  public Class<ENTITY> getEntityClass() {
    return entityType.getEntityClass();
  }

  @Override
  public void saveCurrentStates(ENTITY entity) {
    entityType.saveCurrentStates(entity);
  }

  @Override
  public ENTITY getOriginalStates(ENTITY entity) {
    return entityType.getOriginalStates(entity);
  }

  @Override
  public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
    entityType.preInsert(entity, context);
  }

  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
    entityType.preUpdate(entity, context);
  }

  @Override
  public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {
    entityType.preDelete(entity, context);
  }

  @Override
  public void postInsert(ENTITY entity, PostInsertContext<ENTITY> context) {
    entityType.postInsert(entity, context);
  }

  @Override
  public void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context) {
    entityType.postUpdate(entity, context);
  }

  @Override
  public void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context) {
    entityType.postDelete(entity, context);
  }
}
