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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.mirror.ColumnMirror;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.util.MetaUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMeta {

    protected final String entityName;

    protected final String entityTypeName;

    protected final String entityMetaTypeName;

    protected final NamingType namingType;

    protected final TypeMirror type;

    protected final String typeName;

    protected final boolean ownProperty;

    protected final String fieldPrefix;

    protected String name;

    protected boolean id;

    protected boolean version;

    protected ColumnMirror columnMirror;

    protected IdGeneratorMeta idGeneratorMeta;

    protected DataType dataType;

    public EntityPropertyMeta(TypeElement entityElement,
            VariableElement propertyElement, NamingType namingType,
            boolean ownProperty, ProcessingEnvironment env) {
        assertNotNull(entityElement, propertyElement, namingType, env);
        this.entityName = entityElement.getSimpleName().toString();
        this.entityTypeName = entityElement.getQualifiedName().toString();
        this.entityMetaTypeName = MetaUtil.getMetaTypeName(entityTypeName);
        this.namingType = namingType;
        this.type = propertyElement.asType();
        this.typeName = TypeMirrorUtil.getTypeName(type, env);
        this.ownProperty = ownProperty;
        this.fieldPrefix = Options.getEntityFieldPrefix(env);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public String getEntityMetaTypeName() {
        return entityMetaTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldPrefix + name;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public IdGeneratorMeta getIdGeneratorMeta() {
        return idGeneratorMeta;
    }

    public void setIdGeneratorMeta(IdGeneratorMeta idGeneratorMeta) {
        this.idGeneratorMeta = idGeneratorMeta;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isOwnProperty() {
        return ownProperty;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setColumnMirror(ColumnMirror columnMirror) {
        this.columnMirror = columnMirror;
    }

    public String getColumnName() {
        String columnName = columnMirror != null ? columnMirror.getNameValue()
                : "";
        return !columnName.isEmpty() ? columnName : namingType.apply(name);
    }

    public boolean isColumnInsertable() {
        return columnMirror != null ? columnMirror.getInsertableValue() : true;
    }

    public boolean isColumnUpdatable() {
        return columnMirror != null ? columnMirror.getUpdatableValue() : true;
    }

}
