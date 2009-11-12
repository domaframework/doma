/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.EntityMirror;
import org.seasar.doma.internal.apt.mirror.TableMirror;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityMeta {

    protected final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<EntityPropertyMeta>();

    protected final EntityMirror entityMirror;

    protected final NamingType namingType;

    protected TableMirror tableMirror;

    protected EntityPropertyMeta versionPropertyMeta;

    protected EntityPropertyMeta generatedIdPropertyMeta;

    protected String entityName;

    protected String entityTypeName;

    protected OriginalStatesMeta originalStatesMeta;

    public EntityMeta(EntityMirror entityMirror) {
        assertNotNull(entityMirror);
        this.entityMirror = entityMirror;
        this.namingType = entityMirror.getNamingValue();
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

    public void setTableMirror(TableMirror tableMirror) {
        this.tableMirror = tableMirror;
    }

    public void addPropertyMeta(EntityPropertyMeta propertyMeta) {
        assertNotNull(propertyMeta);
        allPropertyMetas.add(propertyMeta);
        if (propertyMeta.isId()) {
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

    public TypeMirror getEntityListener() {
        return entityMirror.getListenerValue();
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
        return !tableName.isEmpty() ? tableName : namingType
                .apply(entityName);
    }
}
