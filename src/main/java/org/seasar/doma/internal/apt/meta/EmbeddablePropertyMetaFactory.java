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
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypes;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;
import org.seasar.doma.message.Message;


/**
 * @author nakamura-to
 *
 */
public class EmbeddablePropertyMetaFactory {

    private final Context ctx;

    public EmbeddablePropertyMetaFactory(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public EmbeddablePropertyMeta createEmbeddablePropertyMeta(
            VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
        String name = fieldElement.getSimpleName().toString();
        CtType ctType = resolveCtType(fieldElement, fieldElement.asType());
        ColumnReflection columnReflection = ctx.getReflections()
                .newColumnReflection(fieldElement);
        return new EmbeddablePropertyMeta(ctx, fieldElement, name, ctType,
                columnReflection);
    }

    private CtType resolveCtType(VariableElement fieldElement,
            TypeMirror type) {
        CtTypes ctTypes = ctx.getCtTypes();
        CtType ctType = ctTypes.toCtType(type, List.of(
                ctTypes::newOptionalCtType,
                ctTypes::newOptionalIntCtType, ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newBasicCtType, ctTypes::newEmbeddableCtType));
        ctType.accept(new CtTypeValidator(fieldElement), null);
        return ctType;
    }

    private class CtTypeValidator
            extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        private final VariableElement fieldElement;

        public CtTypeValidator(VariableElement fieldElement) {
            this.fieldElement = fieldElement;
        }

        @Override
        public Void visitOptionalCtType(OptionalCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4299, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
                throw new AptException(Message.DOMA4301, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4295, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.hasWildcardType() || ctType.hasTypevarType()) {
                throw new AptException(Message.DOMA4296, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitEmbeddableCtType(EmbeddableCtType ctType, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4297, fieldElement,
                    new Object[] { ctType.getType() });
        }

        @Override
        public Void visitAnyCtType(AnyCtType ctType, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4298, fieldElement,
                    new Object[] { ctType.getType() });
        }
    }
}
