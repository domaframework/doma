package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.mirror.EntityMirror;
import org.seasar.doma.internal.apt.mirror.TableMirror;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityMeta implements TypeElementMeta {

  protected final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<>();

  protected final Map<String, EntityPropertyMeta> allPropertyMetaMap = new HashMap<>();

  protected final List<EntityPropertyMeta> idPropertyMetas = new ArrayList<>();

  protected final EntityMirror entityMirror;

  protected final TypeElement entityElement;

  protected boolean immutable;

  protected NamingType namingType;

  protected TypeMirror entityListener;

  protected TypeElement entityListenerElement;

  protected boolean genericEntityListener;

  protected TableMirror tableMirror;

  protected EntityPropertyMeta versionPropertyMeta;

  protected EntityPropertyMeta tenantIdPropertyMeta;

  protected EntityPropertyMeta generatedIdPropertyMeta;

  protected String entityName;

  protected String entityTypeName;

  protected OriginalStatesMeta originalStatesMeta;

  protected EntityConstructorMeta constructorMeta;

  protected boolean error;

  public EntityMeta(EntityMirror entityMirror, TypeElement entityElement) {
    assertNotNull(entityMirror);
    this.entityMirror = entityMirror;
    this.entityElement = entityElement;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  EntityMirror getEntityMirror() {
    return entityMirror;
  }

  public NamingType getNamingType() {
    return namingType;
  }

  public void setNamingType(NamingType namingType) {
    this.namingType = namingType;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public void setImmutable(boolean immutable) {
    this.immutable = immutable;
  }

  public TypeElement getEntityElement() {
    return entityElement;
  }

  public void setTableMirror(TableMirror tableMirror) {
    this.tableMirror = tableMirror;
  }

  public void addPropertyMeta(EntityPropertyMeta propertyMeta) {
    assertNotNull(propertyMeta);
    allPropertyMetas.add(propertyMeta);
    allPropertyMetaMap.put(propertyMeta.getName(), propertyMeta);
    if (propertyMeta.isId()) {
      idPropertyMetas.add(propertyMeta);
      if (propertyMeta.getIdGeneratorMeta() != null) {
        generatedIdPropertyMeta = propertyMeta;
      }
    }
    if (propertyMeta.isVersion()) {
      versionPropertyMeta = propertyMeta;
    }
    if (propertyMeta.isTenantId()) {
      tenantIdPropertyMeta = propertyMeta;
    }
  }

  public List<EntityPropertyMeta> getAllPropertyMetas() {
    if (constructorMeta == null) {
      return allPropertyMetas;
    }
    return constructorMeta.getEntityPropertyMetas();
  }

  public List<EntityPropertyMeta> getIdPropertyMetas() {
    return idPropertyMetas;
  }

  public boolean hasVersionPropertyMeta() {
    return versionPropertyMeta != null;
  }

  public EntityPropertyMeta getVersionPropertyMeta() {
    return versionPropertyMeta;
  }

  public boolean hasTenantIdPropertyMeta() {
    return tenantIdPropertyMeta != null;
  }

  public EntityPropertyMeta getTenanatIdPropertyMeta() {
    return tenantIdPropertyMeta;
  }

  public boolean hasGeneratedIdPropertyMeta() {
    return generatedIdPropertyMeta != null;
  }

  public EntityPropertyMeta getGeneratedIdPropertyMeta() {
    return generatedIdPropertyMeta;
  }

  public String getEntityTypeName() {
    return entityTypeName;
  }

  public void setEntityTypeName(String entityTypeName) {
    this.entityTypeName = entityTypeName;
  }

  public boolean hasOriginalStatesMeta() {
    return originalStatesMeta != null;
  }

  public OriginalStatesMeta getOriginalStatesMeta() {
    return originalStatesMeta;
  }

  public void setOriginalStatesMeta(OriginalStatesMeta originalStatesMeta) {
    this.originalStatesMeta = originalStatesMeta;
  }

  public EntityConstructorMeta getConstructorMeta() {
    return constructorMeta;
  }

  public void setConstructorMeta(EntityConstructorMeta constructorMeta) {
    this.constructorMeta = constructorMeta;
  }

  public TypeMirror getEntityListener() {
    return entityListener;
  }

  public void setEntityListener(TypeMirror entityListener) {
    this.entityListener = entityListener;
  }

  public TypeElement getEntityListenerElement() {
    return entityListenerElement;
  }

  public void setEntityListenerElement(TypeElement entityListenerElement) {
    this.entityListenerElement = entityListenerElement;
  }

  public boolean isGenericEntityListener() {
    return genericEntityListener;
  }

  public void setGenericEntityListener(boolean genericEntityListener) {
    this.genericEntityListener = genericEntityListener;
  }

  public String getCatalogName() {
    return tableMirror != null ? tableMirror.getCatalogValue() : "";
  }

  public String getSchemaName() {
    return tableMirror != null ? tableMirror.getSchemaValue() : "";
  }

  public String getTableName() {
    return tableMirror != null ? tableMirror.getNameValue() : "";
  }

  public boolean isQuoteRequired() {
    return tableMirror != null ? tableMirror.getQuoteValue() : false;
  }

  public boolean isAbstract() {
    return entityElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public boolean hasEmbeddedProperties() {
    return allPropertyMetas.stream().anyMatch(EntityPropertyMeta::isEmbedded);
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }
}
