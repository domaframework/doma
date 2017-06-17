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

import java.lang.annotation.Annotation;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;

public class QueryParameterMeta {

    protected VariableElement element;

    protected ExecutableElement methodElement;

    protected TypeElement daoElement;

    protected String name;

    protected String typeName;

    protected TypeMirror type;

    protected String qualifiedName;

    protected CtType ctType;

    public QueryParameterMeta() {
    }

    public void setElement(VariableElement element) {
        this.element = element;
    }

    public void setMethodElement(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }

    public void setDaoElement(TypeElement daoElement) {
        this.daoElement = daoElement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public void setCtType(CtType ctType) {
        this.ctType = ctType;
    }

    public VariableElement getElement() {
        return element;
    }

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public CtType getCtType() {
        return ctType;
    }

    public boolean isNullable() {
        return ctType.accept(new NullableCtTypeVisitor(false), null);
    }

    public boolean isBindable() {
        return ctType.accept(new BindableCtTypeVisitor(false), null);
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class NullableCtTypeVisitor
            extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

        public NullableCtTypeVisitor(boolean nullable) {
            super(nullable);
        }

        @Override
        public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class BindableCtTypeVisitor
            extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

        public BindableCtTypeVisitor(boolean bindable) {
            super(bindable);
        }

        @Override
        public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitEmbeddableCtType(EmbeddableCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitEntityCtType(EntityCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitOptionalLongCtType(OptionalLongCtType ctType,
                Void p) throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitOptionalDoubleCtType(OptionalDoubleCtType ctType,
                Void p) throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitReferenceCtType(ReferenceCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }

        @Override
        public Boolean visitAnyCtType(AnyCtType ctType, Void p)
                throws RuntimeException {
            return true;
        }
    }

}
