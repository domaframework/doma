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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.EntityMirror;
import org.seasar.doma.internal.apt.mirror.TableMirror;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityMeta implements TypeElementMeta {

    protected final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<EntityPropertyMeta>();

    protected final Map<String, EntityPropertyMeta> allPropertyMetaMap = new HashMap<String, EntityPropertyMeta>();

    protected final List<EntityPropertyMeta> idPropertyMetas = new ArrayList<EntityPropertyMeta>();

    protected final EntityMirror entityMirror;

    protected final TypeElement entityElement;

    protected boolean immutable;

    protected NamingType namingType;

    protected TypeMirror entityListener;

    protected TypeElement entityListenerElement;

    protected boolean genericEntityListener;

    protected TableMirror tableMirror;

    protected EntityPropertyMeta versionPropertyMeta;

    protected EntityPropertyMeta generatedIdPropertyMeta;

    protected String entityName;

    protected String entityTypeName;

    protected OriginalStatesMeta originalStatesMeta;

    protected ExecutableElement constructor;

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
    }

    public List<EntityPropertyMeta> getAllPropertyMetas() {
        return allPropertyMetas;
    }

    public List<EntityPropertyMeta> getAllPropertyMetasInCtorArgsOrder() {
        if (constructor == null) {
            return allPropertyMetas;
        }
        List<EntityPropertyMeta> results = new ArrayList<EntityPropertyMeta>();
        for (VariableElement param : constructor.getParameters()) {
            results.add(allPropertyMetaMap
                    .get(param.getSimpleName().toString()));
        }
        return results;
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

    public ExecutableElement getConstructor() {
        return constructor;
    }

    public void setConstructor(ExecutableElement constructor) {
        this.constructor = constructor;
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
        String tableName = tableMirror != null ? tableMirror.getNameValue()
                : "";
        return !tableName.isEmpty() ? tableName : namingType.apply(entityName);
    }

    public boolean isAbstract() {
        return entityElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    @Override
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
