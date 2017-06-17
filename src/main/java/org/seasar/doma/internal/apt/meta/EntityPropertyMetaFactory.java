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
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Version;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.ColumnReflection;
import org.seasar.doma.internal.apt.reflection.SequenceGeneratorReflection;
import org.seasar.doma.internal.apt.reflection.TableGeneratorReflection;
import org.seasar.doma.message.Message;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMetaFactory {

    private final Context ctx;

    public EntityPropertyMetaFactory(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public EntityPropertyMeta createEntityPropertyMeta(
            VariableElement fieldElement, EntityMeta entityMeta) {
        assertNotNull(fieldElement, entityMeta);
        TypeElement entityElement = ctx.getElements()
                .toTypeElement(fieldElement.getEnclosingElement());
        if (entityElement == null) {
            throw new AptIllegalStateException(fieldElement.toString());
        }
        EntityPropertyMeta propertyMeta = new EntityPropertyMeta(ctx,
                entityElement, fieldElement, entityMeta.getNamingType());
        doCtType(propertyMeta, fieldElement, entityMeta);
        doName(propertyMeta, fieldElement, entityMeta);
        doId(propertyMeta, fieldElement, entityMeta);
        doVersion(propertyMeta, fieldElement, entityMeta);
        doColumn(propertyMeta, fieldElement, entityMeta);
        return propertyMeta;
    }

    protected void doCtType(EntityPropertyMeta propertyMeta,
            final VariableElement fieldElement, EntityMeta entityMeta) {
        CtType ctType = resolveCtType(fieldElement, fieldElement.asType(),
                entityMeta);
        propertyMeta.setCtType(ctType);
    }

    protected CtType resolveCtType(VariableElement fieldElement,
            TypeMirror type, EntityMeta entityMeta) {
        final OptionalCtType optionalCtType = ctx.getCtTypes()
                .newOptionalCtType(type);
        if (optionalCtType != null) {
            if (optionalCtType.isRawType()) {
                throw new AptException(Message.DOMA4232, fieldElement, new Object[] {
                        optionalCtType.getQualifiedName(),
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (optionalCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4233, fieldElement, new Object[] {
                        optionalCtType.getQualifiedName(),
                        entityMeta.getEntityElement()
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
                throw new AptException(Message.DOMA4204, fieldElement, new Object[] {
                        holderCtType.getQualifiedName(),
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (holderCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4205, fieldElement, new Object[] {
                        holderCtType.getQualifiedName(),
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            return holderCtType;
        }

        final EmbeddableCtType embeddableCtType = ctx.getCtTypes()
                .newEmbeddableCtType(type);
        if (embeddableCtType != null) {
            return embeddableCtType;
        }

        BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(type);
        if (basicCtType != null) {
            return basicCtType;
        }

        throw new AptException(Message.DOMA4096, fieldElement, new Object[] { type,
                entityMeta.getEntityElement().getQualifiedName(),
                fieldElement.getSimpleName() });
    }

    protected void doName(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        String name = fieldElement.getSimpleName().toString();
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, fieldElement, new Object[] { MetaConstants.RESERVED_NAME_PREFIX,
                    entityMeta.getEntityElement().getQualifiedName() });
        }
        propertyMeta.setName(name);
    }

    protected void doId(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Id id = fieldElement.getAnnotation(Id.class);
        if (id == null) {
            GeneratedValue generatedValue = fieldElement
                    .getAnnotation(GeneratedValue.class);
            if (generatedValue == null) {
                validateSequenceGeneratorNotExistent(propertyMeta,
                        fieldElement, entityMeta);
                validateTableGeneratorNotExistent(propertyMeta, fieldElement,
                        entityMeta);
                return;
            }
            throw new AptException(Message.DOMA4033, fieldElement, new Object[] { entityMeta.getEntityElement(),
                    fieldElement.getSimpleName() });
        }
        if (propertyMeta.isEmbedded()) {
            throw new AptException(Message.DOMA4302, fieldElement, new Object[] {
                    entityMeta.getEntityElement().getQualifiedName(),
                    fieldElement.getSimpleName() });
        }
        propertyMeta.setId(true);
        final GeneratedValue generatedValue = fieldElement
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            validateSequenceGeneratorNotExistent(propertyMeta, fieldElement,
                    entityMeta);
            validateTableGeneratorNotExistent(propertyMeta, fieldElement,
                    entityMeta);
            return;
        }
        if (propertyMeta.isEmbedded()) {
            throw new AptException(Message.DOMA4303, fieldElement, new Object[] {
                    entityMeta.getEntityElement().getQualifiedName(),
                    fieldElement.getSimpleName() });
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(Message.DOMA4037, fieldElement, new Object[] {
                    entityMeta.getEntityElement().getQualifiedName(),
                    fieldElement.getSimpleName() });
        }
        if (!isNumber(propertyMeta.getCtType())) {
            throw new AptException(Message.DOMA4095, fieldElement, new Object[] {
                    entityMeta.getEntityElement().getQualifiedName(),
                    fieldElement.getSimpleName() });
        }
        switch (generatedValue.strategy()) {
        case IDENTITY:
            doIdentityIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
            break;
        case SEQUENCE:
            doSequenceIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
            break;
        case TABLE:
            doTableIdGeneratorMeta(propertyMeta, fieldElement, entityMeta);
            break;
        default:
            assertUnreachable();
            break;
        }
    }

    protected void validateSequenceGeneratorNotExistent(
            EntityPropertyMeta propertyMeta, VariableElement fieldElement,
            EntityMeta entityMeta) {
        SequenceGenerator sequenceGenerator = fieldElement
                .getAnnotation(SequenceGenerator.class);
        if (sequenceGenerator != null) {
            throw new AptException(Message.DOMA4030, fieldElement, new Object[] { entityMeta.getEntityElement(),
                    fieldElement.getSimpleName() });
        }
    }

    protected void validateTableGeneratorNotExistent(
            EntityPropertyMeta propertyMeta, VariableElement fieldElement,
            EntityMeta entityMeta) {
        TableGenerator tableGenerator = fieldElement
                .getAnnotation(TableGenerator.class);
        if (tableGenerator != null) {
            throw new AptException(Message.DOMA4031, fieldElement, new Object[] { entityMeta.getEntityElement(),
                    fieldElement.getSimpleName() });
        }
    }

    protected void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
    }

    protected void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        SequenceGeneratorReflection sequenceGeneratorReflection = ctx
                .getReflections()
                .newSequenceGeneratorReflection(fieldElement);
        if (sequenceGeneratorReflection == null) {
            throw new AptException(Message.DOMA4034, fieldElement, new Object[] { entityMeta.getEntityElement(),
                    fieldElement.getSimpleName() });
        }
        validateSequenceIdGenerator(propertyMeta, fieldElement,
                sequenceGeneratorReflection);
        SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(
                sequenceGeneratorReflection);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected void validateSequenceIdGenerator(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement,
            SequenceGeneratorReflection sequenceGeneratorReflection) {
        TypeElement typeElement = ctx.getTypes().toTypeElement(
                sequenceGeneratorReflection.getImplementerValue());
        if (typeElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4170, fieldElement, sequenceGeneratorReflection.getAnnotationMirror(),
                    sequenceGeneratorReflection.getImplementer(),
                    new Object[] { typeElement.getQualifiedName() });
        }
        ExecutableElement constructor = ctx.getElements()
                .getNoArgConstructor(typeElement);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4171, fieldElement, sequenceGeneratorReflection.getAnnotationMirror(),
                    sequenceGeneratorReflection.getImplementer(),
                    new Object[] { typeElement.getQualifiedName() });
        }
    }

    protected void doTableIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        TableGeneratorReflection tableGeneratorReflection = ctx
                .getReflections()
                .newTableGeneratorReflection(fieldElement);
        if (tableGeneratorReflection == null) {
            throw new AptException(Message.DOMA4035, fieldElement, new Object[] { entityMeta.getEntityElement(),
                    fieldElement.getSimpleName() });
        }
        validateTableIdGenerator(propertyMeta, fieldElement,
                tableGeneratorReflection);
        TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(
                tableGeneratorReflection);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected void validateTableIdGenerator(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement,
            TableGeneratorReflection tableGeneratorReflection) {
        TypeElement typeElement = ctx.getTypes()
                .toTypeElement(tableGeneratorReflection.getImplementerValue());
        if (typeElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4168, fieldElement, tableGeneratorReflection.getAnnotationMirror(),
                    tableGeneratorReflection.getImplementer(),
                    new Object[] { typeElement.getQualifiedName() });
        }
        ExecutableElement constructor = ctx.getElements()
                .getNoArgConstructor(typeElement);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4169, fieldElement, tableGeneratorReflection.getAnnotationMirror(),
                    tableGeneratorReflection.getImplementer(),
                    new Object[] { typeElement.getQualifiedName() });
        }
    }

    protected void doVersion(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Version version = fieldElement.getAnnotation(Version.class);
        if (version != null) {
            if (propertyMeta.isEmbedded()) {
                throw new AptException(Message.DOMA4304, fieldElement, new Object[] {
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (entityMeta.hasVersionPropertyMeta()) {
                throw new AptException(Message.DOMA4024, fieldElement, new Object[] {
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            if (!isNumber(propertyMeta.getCtType())) {
                throw new AptException(Message.DOMA4093, fieldElement, new Object[] {
                        entityMeta.getEntityElement()
                                .getQualifiedName(),
                        fieldElement.getSimpleName() });
            }
            propertyMeta.setVersion(true);
        }
    }

    protected void doColumn(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        ColumnReflection columnReflection = ctx.getReflections()
                .newColumnReflection(fieldElement);
        if (columnReflection == null) {
            return;
        }
        if (propertyMeta.isEmbedded()) {
            throw new AptException(Message.DOMA4306, fieldElement, columnReflection.getAnnotationMirror(),
                    new Object[] {
                            entityMeta.getEntityElement().getQualifiedName(),
                            fieldElement.getSimpleName() });
        }
        if (propertyMeta.isId() || propertyMeta.isVersion()) {
            if (!columnReflection.getInsertableValue()) {
                throw new AptException(Message.DOMA4088, fieldElement, columnReflection.getAnnotationMirror(),
                        columnReflection.getInsertable(),
                        new Object[] {
                                entityMeta.getEntityElement()
                                        .getQualifiedName(),
                                fieldElement.getSimpleName() });
            }
            if (!columnReflection.getUpdatableValue()) {
                throw new AptException(Message.DOMA4089, fieldElement, columnReflection.getAnnotationMirror(),
                        columnReflection.getUpdatable(),
                        new Object[] {
                                entityMeta.getEntityElement()
                                        .getQualifiedName(),
                                fieldElement.getSimpleName() });
            }
        }
        propertyMeta.setColumnReflection(columnReflection);
    }

    protected boolean isNumber(CtType ctType) {
        Boolean isNumber = ctType.accept(
                new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

                    @Override
                    public Boolean visitOptionalCtType(OptionalCtType ctType,
                            Void p) throws RuntimeException {
                        return ctType.getElementCtType().accept(this, p);
                    }

                    @Override
                    public Boolean visitOptionalIntCtType(
                            OptionalIntCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitOptionalLongCtType(
                            OptionalLongCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitOptionalDoubleCtType(
                            OptionalDoubleCtType ctType, Void p)
                            throws RuntimeException {
                        return true;
                    }

                    @Override
                    public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                            throws RuntimeException {
                        return ctType.getBasicCtType().accept(this, p);
                    }

                    @Override
                    public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                            throws RuntimeException {
                        TypeMirror boxedType = ctx.getTypes()
                                .boxIfPrimitive(ctType.getTypeMirror());
                        return ctx.getTypes().isAssignable(boxedType,
                                Number.class);
                    }
                }, null);
        return isNumber == Boolean.TRUE;
    }

}
