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

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;
import org.seasar.doma.message.Message;

/**
 * @author nakamura-to
 *
 */
public class EmbeddablePropertyMetaFactory {

    protected final Context ctx;

    public EmbeddablePropertyMetaFactory(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public EmbeddablePropertyMeta createEmbeddablePropertyMeta(
            VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
        EmbeddablePropertyMeta embeddablePropertyMeta = new EmbeddablePropertyMeta(
                ctx, fieldElement);
        embeddablePropertyMeta.setName(fieldElement.getSimpleName().toString());
        CtType ctType = resolveCtType(fieldElement, fieldElement.asType(),
                embeddableMeta);
        embeddablePropertyMeta.setCtType(ctType);
        ColumnReflection columnReflection = ctx.getReflections()
                .newColumnReflection(fieldElement);
        if (columnReflection != null) {
            embeddablePropertyMeta.setColumnReflection(columnReflection);
        }
        return embeddablePropertyMeta;
    }

    protected CtType resolveCtType(VariableElement fieldElement,
            TypeMirror type, EmbeddableMeta embeddableMeta) {

        final OptionalCtType optionalCtType = ctx.getCtTypes()
                .newOptionalCtType(type);
        if (optionalCtType != null) {
            if (optionalCtType.isRawType()) {
                throw new AptException(Message.DOMA4299, fieldElement, new Object[] {
                        optionalCtType.getQualifiedName(),
                        embeddableMeta.getEmbeddableElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (optionalCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4301, fieldElement, new Object[] {
                        optionalCtType.getQualifiedName(),
                        embeddableMeta.getEmbeddableElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            return optionalCtType;
        }

        OptionalIntCtType optionalIntCtType = ctx.getCtTypes()
                .newOptionalIntCtType(type);
        if (optionalIntCtType != null) {
            return optionalIntCtType;
        }

        OptionalLongCtType optionalLongCtType = ctx.getCtTypes()
                .newOptionalLongCtType(type);
        if (optionalLongCtType != null) {
            return optionalLongCtType;
        }

        OptionalDoubleCtType optionalDoubleCtType = ctx.getCtTypes()
                .newOptionalDoubleCtType(type);
        if (optionalDoubleCtType != null) {
            return optionalDoubleCtType;
        }

        final HolderCtType holderCtType = ctx.getCtTypes()
                .newHolderCtType(type);
        if (holderCtType != null) {
            if (holderCtType.isRawType()) {
                throw new AptException(Message.DOMA4295, fieldElement, new Object[] {
                        holderCtType.getQualifiedName(),
                        embeddableMeta.getEmbeddableElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (holderCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4296, fieldElement, new Object[] {
                        holderCtType.getQualifiedName(),
                        embeddableMeta.getEmbeddableElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            return holderCtType;
        }

        BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(type);
        if (basicCtType != null) {
            return basicCtType;
        }

        final EmbeddableCtType embeddableCtType = ctx.getCtTypes()
                .newEmbeddableCtType(type);
        if (embeddableCtType != null) {
            throw new AptException(Message.DOMA4297, fieldElement, new Object[] {
                    type,
                    embeddableMeta.getEmbeddableElement()
                            .getQualifiedName(),
                    fieldElement.getSimpleName() });
        }

        throw new AptException(Message.DOMA4298, fieldElement, new Object[] {
                type,
                embeddableMeta.getEmbeddableElement()
                        .getQualifiedName(),
                fieldElement.getSimpleName() });
    }

}
