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
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

/**
 * @author nakamura
 *
 */
public class QueryReturnMetaFactory {

    private final Context ctx;

    public QueryReturnMetaFactory(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public QueryReturnMeta createQueryReturnMeta(QueryMeta queryMeta) {
        assertNotNull(queryMeta);
        ExecutableElement methodElement = queryMeta.getMethodElement();
        TypeElement daoElement = queryMeta.getDaoElement();
        TypeMirror returnType = methodElement.getReturnType();
        String typeName = ctx.getTypes().getTypeName(returnType);
        CtType ctType = createCtType(returnType);
        ctType.accept(new CtTypeValidator(methodElement, daoElement, typeName),
                null);

        QueryReturnMeta meta = new QueryReturnMeta(ctx);
        meta.setMethodElement(methodElement);
        meta.setDaoElement(daoElement);
        meta.setType(returnType);
        meta.setTypeName(typeName);
        meta.setCtType(ctType);
        return meta;
    }

    private CtType createCtType(TypeMirror typeMirror) {
        CtTypes ctTypes = ctx.getCtTypes();
        return ctTypes.toCtType(typeMirror, List.of(ctTypes::newIterableCtType,
                ctTypes::newStreamCtType, ctTypes::newEntityCtType,
                ctTypes::newOptionalCtType, ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newMapCtType));
    }

    protected class CtTypeValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        final ExecutableElement methodElement;

        final TypeElement daoElement;

        final String typeName;

        public CtTypeValidator(ExecutableElement methodElement,
                TypeElement daoElement, String typeName) {
            this.methodElement = methodElement;
            this.daoElement = daoElement;
            this.typeName = typeName;
        }

        @Override
        public Void visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4109, methodElement, new Object[] { typeName, daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, methodElement, new Object[] { typeName, daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getElementCtType()
                    .accept(new IterableElementValidator(methodElement,
                            daoElement), null);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4236, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4237, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            ctType.getElementCtType().accept(
                    new OptionalElementValidator(methodElement, daoElement),
                    null);
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4206, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4207, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

    protected class IterableElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final ExecutableElement methodElement;

        private final TypeElement daoElement;

        public IterableElementValidator(ExecutableElement methodElement,
                TypeElement daoElement) {
            this.methodElement = methodElement;
            this.daoElement = daoElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4210, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4211, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

    protected class OptionalElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final ExecutableElement methodElement;

        private final TypeElement daoElement;

        public OptionalElementValidator(ExecutableElement methodElement,
                TypeElement daoElement) {
            this.methodElement = methodElement;
            this.daoElement = daoElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4238, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4239, methodElement, new Object[] { ctType.getQualifiedName(),
                        daoElement.getQualifiedName(),
                        methodElement.getSimpleName() });
            }
            return null;
        }
    }

}
