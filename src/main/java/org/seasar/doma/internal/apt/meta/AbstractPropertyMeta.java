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

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;

/**
 * @author nakamura
 *
 */
public abstract class AbstractPropertyMeta {

    protected final String name;

    protected final VariableElement fieldElement;

    protected final CtType ctType;

    protected final ColumnReflection columnReflection;

    protected AbstractPropertyMeta(VariableElement fieldElement, String name,
            CtType ctType,
            ColumnReflection columnReflection) {
        assertNotNull(fieldElement, name, ctType);
        this.fieldElement = fieldElement;
        this.name = name;
        this.ctType = ctType;
        this.columnReflection = columnReflection;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return ctType.getTypeName();
    }

    public String getBoxedTypeName() {
        return ctType.accept(
                new SimpleCtTypeVisitor<String, Void, RuntimeException>() {

                    @Override
                    protected String defaultAction(CtType ctType, Void p)
                            throws RuntimeException {
                        return ctType.getTypeName();
                    }

                    @Override
                    public String visitBasicCtType(BasicCtType ctType, Void p)
                            throws RuntimeException {
                        return ctType.getBoxedTypeName();
                    }
                }, null);
    }

    public CtType getCtType() {
        return ctType;
    }

    public TypeMirror getType() {
        return ctType.getType();
    }

    public ColumnReflection getColumnReflection() {
        return columnReflection;
    }

    public String getColumnName() {
        return columnReflection != null ? columnReflection.getNameValue() : "";
    }

    public boolean isColumnInsertable() {
        return columnReflection != null ? columnReflection.getInsertableValue()
                : true;
    }

    public boolean isColumnUpdatable() {
        return columnReflection != null ? columnReflection.getUpdatableValue()
                : true;
    }

    public boolean isColumnQuoteRequired() {
        return columnReflection != null ? columnReflection.getQuoteValue()
                : false;
    }

}