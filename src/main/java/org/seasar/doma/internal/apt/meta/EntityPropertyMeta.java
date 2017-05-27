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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;
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

    protected final String boxedTypeName;

    protected final String boxedClassName;

    protected final String fieldPrefix;

    protected final ProcessingEnvironment env;

    protected String name;

    protected boolean id;

    protected boolean version;

    protected ColumnReflection columnReflection;

    protected IdGeneratorMeta idGeneratorMeta;

    protected CtType ctType;

    public EntityPropertyMeta(TypeElement entityElement,
            VariableElement propertyElement, NamingType namingType,
            ProcessingEnvironment env) {
        assertNotNull(entityElement, propertyElement, env);
        this.entityName = entityElement.getSimpleName().toString();
        this.entityTypeName = entityElement.getQualifiedName().toString();
        this.entityMetaTypeName = MetaUtil.toFullMetaName(entityElement, env);
        this.namingType = namingType;
        this.type = propertyElement.asType();
        this.typeName = TypeMirrorUtil.getTypeName(type, env);
        this.boxedTypeName = TypeMirrorUtil.getBoxedTypeName(type, env);
        this.boxedClassName = TypeMirrorUtil.getBoxedClassName(type, env);
        this.fieldPrefix = Options.getEntityFieldPrefix(env);
        this.env = env;
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

    public String getBoxedTypeName() {
        return boxedTypeName;
    }

    public String getBoxedClassName() {
        return boxedClassName;
    }

    public CtType getCtType() {
        return ctType;
    }

    public void setCtType(CtType ctType) {
        this.ctType = ctType;
    }

    public void setColumnReflection(ColumnReflection columnReflection) {
        this.columnReflection = columnReflection;
    }

    public String getColumnName() {
        return columnReflection != null ? columnReflection.getNameValue() : "";
    }

    public boolean isColumnInsertable() {
        return columnReflection != null ? columnReflection.getInsertableValue() : true;
    }

    public boolean isColumnUpdatable() {
        return columnReflection != null ? columnReflection.getUpdatableValue() : true;
    }

    public boolean isColumnQuoteRequired() {
        return columnReflection != null ? columnReflection.getQuoteValue() : false;
    }

    public boolean isEmbedded() {
        return ctType
                .accept(new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {
                    @Override
                    public Boolean visitEmbeddableCtType(
                            EmbeddableCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }
                }, null);
    }

    public String getEmbeddableMetaTypeName() {
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            throw new AptIllegalStateException("typeElement must not be null.");
        }
        return MetaUtil.toFullMetaName(typeElement, env);
    }
}
