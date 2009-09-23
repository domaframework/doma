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

/**
 * @author taedium
 * 
 */
public class EntityMeta {

    protected final List<EntityPropertyMeta> allPropertyMetas = new ArrayList<EntityPropertyMeta>();

    protected EntityPropertyMeta versionPropertyMeta;

    protected EntityPropertyMeta generatedIdPropertyMeta;

    protected TableMeta tableMeta;

    protected String entityName;

    protected String entityTypeName;

    protected String listenerTypeName;

    protected String modifiedPropertiesFieldName;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public TableMeta getTableMeta() {
        return tableMeta;
    }

    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
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

    public String getListenerTypeName() {
        return listenerTypeName;
    }

    public void setListenerTypeName(String listenerTypeName) {
        this.listenerTypeName = listenerTypeName;
    }

    public String getModifiedPropertiesFieldName() {
        return modifiedPropertiesFieldName;
    }

    public void setModifiedPropertiesFieldName(
            String modifiedPropertiesFieldName) {
        this.modifiedPropertiesFieldName = modifiedPropertiesFieldName;
    }

}
