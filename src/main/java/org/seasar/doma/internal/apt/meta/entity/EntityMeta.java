package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.reflection.EntityReflection;
import org.seasar.doma.internal.apt.reflection.TableReflection;
import org.seasar.doma.jdbc.entity.NamingType;

public class EntityMeta implements TypeElementMeta {

  private final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<>();

  private final List<EntityPropertyMeta> idPropertyMetas = new ArrayList<>();

  private final EntityReflection entityReflection;

  private final TypeElement entityElement;

  private boolean immutable;

  private NamingType namingType;

  private TypeElement listenerElement;

  private TableReflection tableReflection;

  private EntityPropertyMeta versionPropertyMeta;

  private EntityPropertyMeta generatedIdPropertyMeta;

  private String entityTypeName;

  private OriginalStatesMeta originalStatesMeta;

  public EntityMeta(EntityReflection entityReflection, TypeElement entityElement) {
    assertNotNull(entityReflection, entityElement);
    this.entityReflection = entityReflection;
    this.entityElement = entityElement;
  }

  public String getEntityName() {
    return entityElement.getSimpleName().toString();
  }

  public EntityReflection getEntityReflection() {
    return entityReflection;
  }

  public TypeElement getEntityElement() {
    return entityElement;
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

  public void setTableMirror(TableReflection tableReflection) {
    this.tableReflection = tableReflection;
  }

  public void addPropertyMeta(EntityPropertyMeta propertyMeta) {
    assertNotNull(propertyMeta);
    allPropertyMetas.add(propertyMeta);
    if (propertyMeta.isId()) {
      idPropertyMetas.add(propertyMeta);
      if (propertyMeta.getIdGeneratorMeta() != null) {
        generatedIdPropertyMeta = propertyMeta;
      }
    }
    if (propertyMeta.isVersion()) {
      versionPropertyMeta = propertyMeta;
    }
  }

  public List<EntityPropertyMeta> getAllPropertyMetas() {
    return allPropertyMetas;
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

  public TypeElement getListenerElement() {
    return listenerElement;
  }

  public void setListenerElement(TypeElement listenerElement) {
    this.listenerElement = listenerElement;
  }

  public boolean hasGenericListener() {
    return !listenerElement.getTypeParameters().isEmpty();
  }

  public String getCatalogName() {
    return tableReflection != null ? tableReflection.getCatalogValue() : "";
  }

  public String getSchemaName() {
    return tableReflection != null ? tableReflection.getSchemaValue() : "";
  }

  public String getTableName() {
    return tableReflection != null ? tableReflection.getNameValue() : "";
  }

  public boolean isQuoteRequired() {
    return tableReflection != null && tableReflection.getQuoteValue();
  }

  public boolean isAbstract() {
    return entityElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public boolean hasEmbeddedProperties() {
    return allPropertyMetas.stream().anyMatch(EntityPropertyMeta::isEmbedded);
  }
}
