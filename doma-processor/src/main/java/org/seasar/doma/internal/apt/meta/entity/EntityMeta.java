/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.EntityAnnot;
import org.seasar.doma.internal.apt.annot.TableAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.NullEntityListener;

public class EntityMeta implements TypeElementMeta {

  private final List<AssociationPropertyMeta> associationPropertyMetas = new ArrayList<>();

  private final List<EntityFieldMeta> allFieldMetas = new ArrayList<>();

  private final List<EntityPropertyMeta> idPropertyMetas = new ArrayList<>();

  private final List<ScopeClassMeta> scopeClassMetas = new ArrayList<>();

  private final EntityAnnot entityAnnot;

  private final TypeElement typeElement;

  private final TypeMirror type;

  private boolean immutable;

  private NamingType namingType;

  private TypeElement entityListenerElement;

  private boolean genericEntityListener;

  private TableAnnot tableAnnot;

  private EntityPropertyMeta versionPropertyMeta;

  private EntityPropertyMeta tenantIdPropertyMeta;

  private EntityPropertyMeta generatedIdPropertyMeta;

  private String entityName;

  private OriginalStatesMeta originalStatesMeta;

  private EntityConstructorMeta constructorMeta;

  private boolean error;

  public EntityMeta(EntityAnnot entityAnnot, TypeElement typeElement) {
    assertNotNull(entityAnnot);
    this.entityAnnot = entityAnnot;
    this.typeElement = typeElement;
    this.type = typeElement.asType();
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public EntityAnnot getEntityAnnot() {
    return entityAnnot;
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

  public void setTableAnnot(TableAnnot tableAnnot) {
    this.tableAnnot = tableAnnot;
  }

  public void addAssociationPropertyMeta(AssociationPropertyMeta associationPropertyMeta) {
    associationPropertyMetas.add(associationPropertyMeta);
  }

  public List<AssociationPropertyMeta> getAssociationPropertyMetas() {
    return associationPropertyMetas;
  }

  public void addFieldMeta(EntityFieldMeta fieldMeta) {
    assertNotNull(fieldMeta);
    allFieldMetas.add(fieldMeta);
    if (fieldMeta instanceof EntityPropertyMeta propertyMeta) {
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
  }

  public List<EntityFieldMeta> getAllFieldMetas() {
    if (constructorMeta == null) {
      return allFieldMetas;
    }
    return constructorMeta.getEntityFieldMetas();
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

  public EntityPropertyMeta getTenantIdPropertyMeta() {
    return tenantIdPropertyMeta;
  }

  public boolean hasGeneratedIdPropertyMeta() {
    return generatedIdPropertyMeta != null;
  }

  public EntityPropertyMeta getGeneratedIdPropertyMeta() {
    return generatedIdPropertyMeta;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public TypeMirror getType() {
    return type;
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

  public void setConstructorMeta(EntityConstructorMeta constructorMeta) {
    this.constructorMeta = constructorMeta;
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

  public boolean isNullEntityListener() {
    return entityListenerElement
        .getQualifiedName()
        .contentEquals(NullEntityListener.class.getName());
  }

  public String getCatalogName() {
    return tableAnnot != null ? tableAnnot.getCatalogValue() : "";
  }

  public String getSchemaName() {
    return tableAnnot != null ? tableAnnot.getSchemaValue() : "";
  }

  public String getTableName() {
    return tableAnnot != null ? tableAnnot.getNameValue() : "";
  }

  public boolean isQuoteRequired() {
    return tableAnnot != null && tableAnnot.getQuoteValue();
  }

  public boolean isAbstract() {
    return typeElement.getModifiers().contains(Modifier.ABSTRACT);
  }

  public boolean hasEmbeddedFields() {
    return allFieldMetas.stream().anyMatch(it -> it instanceof EmbeddedMeta);
  }

  public void addScopeClassMeta(ScopeClassMeta scopeClassMeta) {
    scopeClassMetas.add(scopeClassMeta);
  }

  public List<ScopeClassMeta> getScopeClassMetas() {
    return scopeClassMetas;
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }
}
