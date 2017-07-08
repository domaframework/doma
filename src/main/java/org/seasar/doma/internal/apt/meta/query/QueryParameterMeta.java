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
package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

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

    private final String name;

    private final VariableElement element;

    private final CtType ctType;

    public QueryParameterMeta(VariableElement element, String name,
            CtType ctType) {
        assertNotNull(element, name, ctType);
        this.element = element;
        this.name = name;
        this.ctType = ctType;
    }

    public VariableElement getElement() {
        return element;
    }

    public String getName() {
        return name;
    }

    public CtType getCtType() {
        return ctType;
    }

    public String getTypeName() {
        return ctType.getTypeName();
    }

    public String getQualifiedName() {
        return ctType.getQualifiedName();
    }

    public boolean isArray() {
        return ctType.getType().getKind() == TypeKind.ARRAY;
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
    private class NullableCtTypeVisitor
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
    private class BindableCtTypeVisitor
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
