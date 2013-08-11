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
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.ReferenceType;
import org.seasar.doma.internal.apt.type.SelectOptionsType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
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

    protected final DataType dataType;

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
        dataType = createDataType(parameterElement, env);
    }

    protected DataType createDataType(final VariableElement parameterElement,
            final ProcessingEnvironment env) {
        IterableType iterableType = IterableType.newInstance(type, env);
        if (iterableType != null) {
            if (iterableType.isRawType()) {
                throw new AptException(Message.DOMA4159, env, parameterElement);
            }
            if (iterableType.isWildcardType()) {
                throw new AptException(Message.DOMA4160, env, parameterElement);
            }
            iterableType.getElementType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainType(final DomainType dataType,
                                Void p) throws RuntimeException {
                            if (dataType.isRawType()) {
                                throw new AptException(Message.DOMA4212, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            if (dataType.isWildcardType()) {
                                throw new AptException(Message.DOMA4213, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterableType;
        }

        EntityType entityType = EntityType.newInstance(type, env);
        if (entityType != null) {
            return entityType;
        }

        final DomainType domainType = DomainType.newInstance(type, env);
        if (domainType != null) {
            if (domainType.isRawType()) {
                throw new AptException(Message.DOMA4208, env, parameterElement,
                        domainType.getQualifiedName());
            }
            if (domainType.isWildcardType()) {
                throw new AptException(Message.DOMA4209, env, parameterElement,
                        domainType.getQualifiedName());
            }
            return domainType;
        }

        BasicType basicType = BasicType.newInstance(type, env);
        if (basicType != null) {
            return basicType;
        }

        SelectOptionsType selectOptionsType = SelectOptionsType.newInstance(
                type, env);
        if (selectOptionsType != null) {
            return selectOptionsType;
        }

        IterationCallbackType iterationCallbackType = IterationCallbackType
                .newInstance(type, env);
        if (iterationCallbackType != null) {
            if (iterationCallbackType.isRawType()) {
                throw new AptException(Message.DOMA4110, env, parameterElement,
                        qualifiedName);
            }
            if (iterationCallbackType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            iterationCallbackType.getReturnType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainType(final DomainType dataType,
                                Void p) throws RuntimeException {
                            if (dataType.isRawType()) {
                                throw new AptException(Message.DOMA4214, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            if (dataType.isWildcardType()) {
                                throw new AptException(Message.DOMA4215, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            iterationCallbackType.getTargetType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainType(final DomainType dataType,
                                Void p) throws RuntimeException {
                            if (dataType.isRawType()) {
                                throw new AptException(Message.DOMA4216, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            if (dataType.isWildcardType()) {
                                throw new AptException(Message.DOMA4217, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterationCallbackType;
        }

        ReferenceType referenceType = ReferenceType.newInstance(type, env);
        if (referenceType != null) {
            if (referenceType.isRaw()) {
                throw new AptException(Message.DOMA4108, env, parameterElement,
                        qualifiedName);
            }
            if (referenceType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, env, parameterElement,
                        qualifiedName);
            }
            referenceType.getReferentType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainType(final DomainType dataType,
                                Void p) throws RuntimeException {
                            if (dataType.isRawType()) {
                                throw new AptException(Message.DOMA4218, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            if (dataType.isWildcardType()) {
                                throw new AptException(Message.DOMA4219, env,
                                        parameterElement,
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return referenceType;
        }

        return AnyType.newInstance(type, env);
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

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return dataType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicType(BasicType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainType(DomainType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isBindable() {
        return dataType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>(
                        false) {

                    @Override
                    public Boolean visitBasicType(BasicType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitDomainType(DomainType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitEntityType(EntityType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitIterableType(IterableType dataType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitReferenceType(ReferenceType dataType,
                            Void p) throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitAnyType(AnyType dataType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                }, null);
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return element.getAnnotation(annotationType) != null;
    }

}
