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

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * @author nakamura-to
 *
 */
public class EmbeddablePropertyMeta {

    protected final TypeMirror type;

    protected final String typeName;

    protected final String boxedTypeName;

    protected final String boxedClassName;

    protected String name;

    protected ColumnReflection columnReflection;

    protected CtType ctType;

    public EmbeddablePropertyMeta(Context ctx, VariableElement fieldElement) {
        assertNotNull(ctx, fieldElement);
        this.type = fieldElement.asType();
        this.typeName = ctx.getTypes().getTypeName(type);
        this.boxedTypeName = ctx.getTypes().getBoxedTypeName(type);
        this.boxedClassName = ctx.getTypes().getBoxedClassName(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnReflection getColumnReflection() {
        return columnReflection;
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

    public CtType getCtType() {
        return ctType;
    }

    public void setCtType(CtType ctType) {
        this.ctType = ctType;
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

}
