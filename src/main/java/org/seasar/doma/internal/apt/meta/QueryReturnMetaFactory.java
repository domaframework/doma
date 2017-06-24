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

    public QueryReturnMeta createQueryReturnMeta(
            ExecutableElement methodElement) {
        assertNotNull(methodElement);
        CtType ctType = createCtType(methodElement);
        return new QueryReturnMeta(ctx, methodElement, ctType);
    }

    private CtType createCtType(ExecutableElement methodElement) {
        TypeMirror returnType = methodElement.getReturnType();
        CtTypes ctTypes = ctx.getCtTypes();
        CtType ctType = ctTypes.toCtType(returnType, List.of(ctTypes::newIterableCtType,
                ctTypes::newStreamCtType, ctTypes::newEntityCtType,
                ctTypes::newOptionalCtType, ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newMapCtType));
        ctType.accept(new CtTypeValidator(methodElement), null);
        return ctType;
    }

    private class CtTypeValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final ExecutableElement methodElement;

        public CtTypeValidator(ExecutableElement methodElement) {
            this.methodElement = methodElement;
        }

        @Override
        public Void visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4109, methodElement,
                        new Object[] { ctType.getTypeMirror() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, methodElement,
                        new Object[] { ctType.getTypeMirror() });
            }
            ctType.getElementCtType()
                    .accept(new IterableElementValidator(methodElement), null);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4236, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4237, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            ctType.getElementCtType()
                    .accept(new OptionalElementValidator(methodElement), null);
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4206, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4207, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

    private class IterableElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final ExecutableElement methodElement;

        public IterableElementValidator(ExecutableElement methodElement) {
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4210, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4211, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

    private class OptionalElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final ExecutableElement methodElement;

        public OptionalElementValidator(ExecutableElement methodElement) {
            this.methodElement = methodElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4238, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4239, methodElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

}
