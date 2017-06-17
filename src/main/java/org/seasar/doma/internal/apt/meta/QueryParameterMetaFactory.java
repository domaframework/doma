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

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.message.Message;

/**
 * @author nakamura
 *
 */
public class QueryParameterMetaFactory {

    private final Context ctx;

    public QueryParameterMetaFactory(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public QueryParameterMeta createQueryParameterMeta(
            VariableElement parameterElement, QueryMeta queryMeta) {
        assertNotNull(parameterElement, queryMeta);
        
        ExecutableElement methodElement = queryMeta.getMethodElement();
        TypeElement daoElement = queryMeta.getDaoElement();
        String name = ctx.getElements().getParameterName(parameterElement);
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, parameterElement, new Object[] { MetaConstants.RESERVED_NAME_PREFIX,
                    daoElement.getQualifiedName(),
                    methodElement.getSimpleName() });
        }
        TypeMirror type = parameterElement.asType();
        String typeName = ctx.getTypes().getTypeName(type);
        String qualifiedName;
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement != null) {
            qualifiedName = typeElement.getQualifiedName().toString();
        } else {
            qualifiedName = typeName;
        }
        CtType ctType = createCtType(type);
        ctType.accept(new CtTypeValidator(parameterElement, daoElement,
                methodElement, qualifiedName), null);

        QueryParameterMeta meta = new QueryParameterMeta();
        meta.setElement(parameterElement);
        meta.setMethodElement(methodElement);
        meta.setDaoElement(daoElement);
        meta.setName(name);
        meta.setType(type);
        meta.setTypeName(typeName);
        meta.setQualifiedName(qualifiedName);
        meta.setCtType(ctType);
        return meta;
    }

    private CtType createCtType(TypeMirror typeMirror) {
        CtTypes ctTypes = ctx.getCtTypes();
        return ctTypes.toCtType(typeMirror, List.of(
                ctTypes::newIterableCtType, ctTypes::newEntityCtType,
                ctTypes::newOptionalCtType, ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newSelectOptionsCtType,
                ctTypes::newFunctionCtType, ctTypes::newCollectorCtType,
                ctTypes::newReferenceCtType, ctTypes::newBiFunctionCtType
        ));
    }

    protected class CtTypeValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        final VariableElement parameterElement;

        final TypeElement daoElement;

        final ExecutableElement methodElement;

        final String qualifiedName;

        public CtTypeValidator(VariableElement parameterElement,
                TypeElement daoElement,
                ExecutableElement methodElement, String qualifiedName) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
            this.qualifiedName = qualifiedName;
        }

        @Override
        public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4438, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4439, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }

        @Override
        public Void visitReferenceCtType(ReferenceCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRaw()) {
                throw new AptException(Message.DOMA4108, parameterElement, new Object[] { daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getReferentCtType()
                    .accept(new ReferenceReferentValidator(parameterElement,
                            daoElement, methodElement), null);
            return null;
        }

        @Override
        public Void visitCollectorCtType(CollectorCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4258, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4259, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getTargetCtType().accept(new CollectorTargetValidator(
                    parameterElement, daoElement, methodElement), null);
            return null;
        }

        @Override
        public Void visitFunctionCtType(FunctionCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4240, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4241, parameterElement, new Object[] { qualifiedName,
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getTargetCtType().accept(new FunctionTargetValidator(
                    parameterElement, daoElement, methodElement), null);
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4208, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4209, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }

        @Override
        public Void visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4159, parameterElement, new Object[] { daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4160, parameterElement, new Object[] { daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getElementCtType().accept(new IterableElementValidator(
                    parameterElement, daoElement, methodElement), null);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4236, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4237, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getElementCtType().accept(new OptionalElementValidator(
                    parameterElement, daoElement, methodElement), null);
            return null;
        }
    }

    protected class IterableElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final VariableElement parameterElement;

        protected final TypeElement daoElement;

        protected final ExecutableElement methodElement;

        protected IterableElementValidator(VariableElement parameterElement,
                TypeElement daoElement, ExecutableElement methodElement) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4212, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4213, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

    protected class OptionalElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final VariableElement parameterElement;

        protected final TypeElement daoElement;

        protected final ExecutableElement methodElement;

        protected OptionalElementValidator(
                VariableElement parameterElement, TypeElement daoElement,
                ExecutableElement methodElement) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4238, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4239, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

    protected class FunctionTargetValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final VariableElement parameterElement;

        protected final TypeElement daoElement;

        protected final ExecutableElement methodElement;

        protected FunctionTargetValidator(
                VariableElement parameterElement, TypeElement daoElement,
                ExecutableElement methodElement) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
        }

        @Override
        public Void visitStreamCtType(StreamCtType ctType, Void p)
                throws RuntimeException {
            ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(),
                    p);
            return null;
        }

        protected class StreamElementCtTypeVisitor
                extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

            @Override
            public Void visitHolderCtType(final HolderCtType ctType, Void p)
                    throws RuntimeException {
                if (ctType.isRawType()) {
                    throw new AptException(Message.DOMA4242, parameterElement,
                            new Object[] { ctType.getQualifiedName(),
                                    daoElement.getQualifiedName(),
                                    methodElement.getSimpleName() });
                }
                if (ctType.isWildcardType()) {
                    throw new AptException(Message.DOMA4243, parameterElement,
                            new Object[] { ctType.getQualifiedName(),
                                    daoElement.getQualifiedName(),
                                    methodElement.getSimpleName() });
                }
                return null;
            }
        }
    }

    protected class CollectorTargetValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final VariableElement parameterElement;

        protected final TypeElement daoElement;

        protected final ExecutableElement methodElement;

        protected CollectorTargetValidator(
                VariableElement parameterElement, TypeElement daoElement,
                ExecutableElement methodElement) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4260, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4261, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

    protected class ReferenceReferentValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        protected final VariableElement parameterElement;

        protected final TypeElement daoElement;

        protected final ExecutableElement methodElement;

        protected ReferenceReferentValidator(
                VariableElement parameterElement, TypeElement daoElement,
                ExecutableElement methodElement) {
            this.parameterElement = parameterElement;
            this.daoElement = daoElement;
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4218, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4219, parameterElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

}
