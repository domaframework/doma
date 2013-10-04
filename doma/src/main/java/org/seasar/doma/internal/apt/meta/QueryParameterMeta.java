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

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.IterationCallbackCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

public class QueryParameterMeta {

    protected final VariableElement element;

    protected final ProcessingEnvironment env;

    protected final String name;

    protected final String typeName;

    protected final TypeMirror type;

    protected final String qualifiedName;

    protected final CtType ctType;

    public QueryParameterMeta(VariableElement parameterElement,
            ProcessingEnvironment env) {
        assertNotNull(parameterElement, env);
        this.element = parameterElement;
        this.env = env;
        name = ElementUtil.getParameterName(parameterElement);
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, env, parameterElement,
                    MetaConstants.RESERVED_NAME_PREFIX);
        }
        type = parameterElement.asType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement != null) {
            qualifiedName = typeElement.getQualifiedName().toString();
        } else {
            qualifiedName = typeName;
        }
        ctType = createCtType(parameterElement, env);
    }

    protected CtType createCtType(final VariableElement parameterElement,
            final ProcessingEnvironment env) {
        IterableCtType iterableCtType = IterableCtType.newInstance(type, env);
        if (iterableCtType != null) {
            if (iterableCtType.isRawType()) {
                throw new AptException(Message.DOMA4159, env, parameterElement);
            }
            if (iterableCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4160, env, parameterElement);
            }
            iterableCtType.getElementType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainCtType(final DomainCtType ctType,
                                Void p) throws RuntimeException {
                            if (ctType.isRawType()) {
                                throw new AptException(Message.DOMA4212, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            if (ctType.isWildcardType()) {
                                throw new AptException(Message.DOMA4213, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterableCtType;
        }

        EntityCtType entityCtType = EntityCtType.newInstance(type, env);
        if (entityCtType != null) {
            return entityCtType;
        }

        final DomainCtType domainCtType = DomainCtType.newInstance(type, env);
        if (domainCtType != null) {
            if (domainCtType.isRawType()) {
                throw new AptException(Message.DOMA4208, env, parameterElement,
                        domainCtType.getQualifiedName());
            }
            if (domainCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4209, env, parameterElement,
                        domainCtType.getQualifiedName());
            }
            return domainCtType;
        }

        BasicCtType basicCtType = BasicCtType.newInstance(type, env);
        if (basicCtType != null) {
            return basicCtType;
        }

        SelectOptionsCtType selectOptionsCtType = SelectOptionsCtType.newInstance(
                type, env);
        if (selectOptionsCtType != null) {
            return selectOptionsCtType;
        }

        IterationCallbackCtType iterationCallbackCtType = IterationCallbackCtType
                .newInstance(type, env);
        if (iterationCallbackCtType != null) {
            if (iterationCallbackCtType.isRawType()) {
                throw new AptException(Message.DOMA4110, env, parameterElement,
                        qualifiedName);
            }
            if (iterationCallbackCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            iterationCallbackCtType.getReturnType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainCtType(final DomainCtType ctType,
                                Void p) throws RuntimeException {
                            if (ctType.isRawType()) {
                                throw new AptException(Message.DOMA4214, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            if (ctType.isWildcardType()) {
                                throw new AptException(Message.DOMA4215, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            iterationCallbackCtType.getTargetType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainCtType(final DomainCtType ctType,
                                Void p) throws RuntimeException {
                            if (ctType.isRawType()) {
                                throw new AptException(Message.DOMA4216, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            if (ctType.isWildcardType()) {
                                throw new AptException(Message.DOMA4217, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterationCallbackCtType;
        }

        ReferenceCtType referenceCtType = ReferenceCtType.newInstance(type, env);
        if (referenceCtType != null) {
            if (referenceCtType.isRaw()) {
                throw new AptException(Message.DOMA4108, env, parameterElement,
                        qualifiedName);
            }
            if (referenceCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            referenceCtType.getReferentType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainCtType(final DomainCtType ctType,
                                Void p) throws RuntimeException {
                            if (ctType.isRawType()) {
                                throw new AptException(Message.DOMA4218, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            if (ctType.isWildcardType()) {
                                throw new AptException(Message.DOMA4219, env,
                                        parameterElement,
                                        ctType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return referenceCtType;
        }

        return AnyCtType.newInstance(type, env);
    }

    public VariableElement getElement() {
        return element;
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
        return ctType.accept(
                new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isBindable() {
        return ctType.accept(
                new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitEntityCtType(EntityCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitIterableCtType(IterableCtType ctType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitReferenceCtType(ReferenceCtType ctType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitAnyCtType(AnyCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

}
