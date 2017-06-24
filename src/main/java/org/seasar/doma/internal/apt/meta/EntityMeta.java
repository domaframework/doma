/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.reflection.EntityReflection;
import org.seasar.doma.internal.apt.reflection.TableReflection;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityMeta implements TypeElementMeta {

    private final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<>();

    private final Map<String, EntityPropertyMeta> allPropertyMetaMap = new HashMap<>();

    private final List<EntityPropertyMeta> idPropertyMetas = new ArrayList<>();

    private final EntityReflection entityReflection;

    private final TypeElement entityElement;

    private boolean immutable;

    private NamingType namingType;

    private TypeMirror entityListener;

    private TypeElement entityListenerElement;

    private boolean genericEntityListener;

    private TableReflection tableReflection;

    private EntityPropertyMeta versionPropertyMeta;

    private EntityPropertyMeta generatedIdPropertyMeta;

    private String entityName;

    private String entityTypeName;

    private OriginalStatesMeta originalStatesMeta;

    private EntityConstructorMeta constructorMeta;

    private boolean error;

    public EntityMeta(EntityReflection entityReflection, TypeElement entityElement) {
        assertNotNull(entityReflection);
        this.entityReflection = entityReflection;
        this.entityElement = entityElement;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public EntityReflection getEntityReflection() {
        return entityReflection;
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

    public void setTableMirror(TableReflection tableReflection) {
        this.tableReflection = tableReflection;
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
        return tableReflection != null ? tableReflection.getCatalogValue() : "";
    }

    public String getSchemaName() {
        return tableReflection != null ? tableReflection.getSchemaValue() : "";
    }

    public String getTableName() {
        return tableReflection != null ? tableReflection.getNameValue() : "";
    }

    public boolean isQuoteRequired() {
        return tableReflection != null ? tableReflection.getQuoteValue() : false;
    }

    public boolean isAbstract() {
        return entityElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    public boolean hasEmbeddedProperties() {
        return allPropertyMetas.stream().anyMatch(
                EntityPropertyMeta::isEmbedded);
    }

    @Override
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
