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
            VariableElement parameterElement) {
        assertNotNull(parameterElement);
        String name = ctx.getElements().getParameterName(parameterElement);
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, parameterElement,
                    new Object[] { MetaConstants.RESERVED_NAME_PREFIX });
        }
        CtType ctType = createCtType(parameterElement);
        return new QueryParameterMeta(parameterElement, name, ctType);
    }

    private CtType createCtType(VariableElement parameterElement) {
        TypeMirror type = parameterElement.asType();
        CtTypes ctTypes = ctx.getCtTypes();
        CtType ctType = ctTypes.toCtType(type, List.of(
                ctTypes::newIterableCtType, ctTypes::newEntityCtType,
                ctTypes::newOptionalCtType, ctTypes::newOptionalIntCtType,
                ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newSelectOptionsCtType,
                ctTypes::newFunctionCtType, ctTypes::newCollectorCtType,
                ctTypes::newReferenceCtType, ctTypes::newBiFunctionCtType));
        ctType.accept(new CtTypeValidator(parameterElement), null);
        return ctType;
    }

    private class CtTypeValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        public CtTypeValidator(VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitBiFunctionCtType(BiFunctionCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4438, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4439, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitReferenceCtType(ReferenceCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRaw()) {
                throw new AptException(Message.DOMA4108, parameterElement);
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4112, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            ctType.getReferentCtType().accept(
                    new ReferenceReferentValidator(parameterElement), null);
            return null;
        }

        @Override
        public Void visitCollectorCtType(CollectorCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4258, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4259, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            ctType.getTargetCtType().accept(new CollectorTargetValidator(
                    parameterElement), null);
            return null;
        }

        @Override
        public Void visitFunctionCtType(FunctionCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4240, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4241, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            ctType.getTargetCtType().accept(
                    new FunctionTargetValidator(parameterElement), null);
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4208, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4209, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitIterableCtType(IterableCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4159, parameterElement);
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4160, parameterElement);
            }
            ctType.getElementCtType().accept(new IterableElementValidator(
                    parameterElement), null);
            return null;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4236, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4237, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            ctType.getElementCtType().accept(
                    new OptionalElementValidator(parameterElement), null);
            return null;
        }
    }

    private class IterableElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        private IterableElementValidator(VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4212, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4213, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

    private class OptionalElementValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        private OptionalElementValidator(VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4238, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4239, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

    private class FunctionTargetValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        private FunctionTargetValidator(VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitStreamCtType(StreamCtType ctType, Void p)
                throws RuntimeException {
            ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(),
                    p);
            return null;
        }

        private class StreamElementCtTypeVisitor
                extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

            @Override
            public Void visitHolderCtType(final HolderCtType ctType, Void p)
                    throws RuntimeException {
                if (ctType.isRawType()) {
                    throw new AptException(Message.DOMA4242, parameterElement,
                            new Object[] { ctType.getQualifiedName() });
                }
                if (ctType.isWildcardType()) {
                    throw new AptException(Message.DOMA4243, parameterElement,
                            new Object[] { ctType.getQualifiedName() });
                }
                return null;
            }
        }
    }

    private class CollectorTargetValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        private CollectorTargetValidator(
                VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4260, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4261, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

    private class ReferenceReferentValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement parameterElement;

        private ReferenceReferentValidator(VariableElement parameterElement) {
            this.parameterElement = parameterElement;
        }

        @Override
        public Void visitHolderCtType(final HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4218, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4219, parameterElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }
    }

}
