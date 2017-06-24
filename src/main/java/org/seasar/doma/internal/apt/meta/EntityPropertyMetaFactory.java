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

import java.util.List;

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
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.CtTypes;
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
        String name = getName(fieldElement);
        CtType ctType = createCtType(fieldElement);
        ColumnReflection columnReflection = ctx.getReflections()
                .newColumnReflection(fieldElement);
        String filedPrefix = ctx.getOptions().getEntityFieldPrefix();
        EntityPropertyMeta propertyMeta = new EntityPropertyMeta(ctx,
                fieldElement, name, ctType, columnReflection,
                filedPrefix);
        doId(propertyMeta, fieldElement, entityMeta);
        doVersion(propertyMeta, fieldElement, entityMeta);
        doColumn(propertyMeta, fieldElement, entityMeta);
        return propertyMeta;
    }

    private String getName(VariableElement fieldElement) {
        String name = fieldElement.getSimpleName().toString();
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, fieldElement,
                    new Object[] { MetaConstants.RESERVED_NAME_PREFIX });
        }
        return name;
    }

    private CtType createCtType(VariableElement fieldElement) {
        TypeMirror type = fieldElement.asType();
        CtTypes ctTypes = ctx.getCtTypes();
        CtType ctType = ctTypes.toCtType(type, List.of(
                ctTypes::newOptionalCtType,
                ctTypes::newOptionalIntCtType, ctTypes::newOptionalLongCtType,
                ctTypes::newOptionalDoubleCtType, ctTypes::newHolderCtType,
                ctTypes::newEmbeddableCtType, ctTypes::newBasicCtType));
        ctType.accept(new CtTypeValidator(fieldElement), null);
        return ctType;
    }

    private void doId(EntityPropertyMeta propertyMeta,
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
            throw new AptException(Message.DOMA4033, fieldElement);
        }
        if (propertyMeta.isEmbedded()) {
            throw new AptException(Message.DOMA4302, fieldElement);
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
            throw new AptException(Message.DOMA4303, fieldElement);
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(Message.DOMA4037, fieldElement);
        }
        if (!isNumber(propertyMeta.getCtType())) {
            throw new AptException(Message.DOMA4095, fieldElement);
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

    private void validateSequenceGeneratorNotExistent(
            EntityPropertyMeta propertyMeta, VariableElement fieldElement,
            EntityMeta entityMeta) {
        SequenceGenerator sequenceGenerator = fieldElement
                .getAnnotation(SequenceGenerator.class);
        if (sequenceGenerator != null) {
            throw new AptException(Message.DOMA4030, fieldElement);
        }
    }

    private void validateTableGeneratorNotExistent(
            EntityPropertyMeta propertyMeta, VariableElement fieldElement,
            EntityMeta entityMeta) {
        TableGenerator tableGenerator = fieldElement
                .getAnnotation(TableGenerator.class);
        if (tableGenerator != null) {
            throw new AptException(Message.DOMA4031, fieldElement);
        }
    }

    private void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
    }

    private void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        SequenceGeneratorReflection sequenceGeneratorReflection = ctx
                .getReflections()
                .newSequenceGeneratorReflection(fieldElement);
        if (sequenceGeneratorReflection == null) {
            throw new AptException(Message.DOMA4034, fieldElement);
        }
        validateSequenceIdGenerator(propertyMeta, fieldElement,
                sequenceGeneratorReflection);
        SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(
                sequenceGeneratorReflection);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    private void validateSequenceIdGenerator(EntityPropertyMeta propertyMeta,
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

    private void doTableIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        TableGeneratorReflection tableGeneratorReflection = ctx
                .getReflections()
                .newTableGeneratorReflection(fieldElement);
        if (tableGeneratorReflection == null) {
            throw new AptException(Message.DOMA4035, fieldElement);
        }
        validateTableIdGenerator(propertyMeta, fieldElement,
                tableGeneratorReflection);
        TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(
                tableGeneratorReflection);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    private void validateTableIdGenerator(EntityPropertyMeta propertyMeta,
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

    private void doVersion(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Version version = fieldElement.getAnnotation(Version.class);
        if (version != null) {
            if (propertyMeta.isEmbedded()) {
                throw new AptException(Message.DOMA4304, fieldElement);
            }
            if (entityMeta.hasVersionPropertyMeta()) {
                throw new AptException(Message.DOMA4024, fieldElement);
            }
            if (!isNumber(propertyMeta.getCtType())) {
                throw new AptException(Message.DOMA4093, fieldElement);
            }
            propertyMeta.setVersion(true);
        }
    }

    private void doColumn(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        ColumnReflection columnReflection = propertyMeta.getColumnReflection();
        if (columnReflection == null) {
            return;
        }
        if (propertyMeta.isEmbedded()) {
            throw new AptException(Message.DOMA4306, fieldElement,
                    columnReflection.getAnnotationMirror());
        }
        if (propertyMeta.isId() || propertyMeta.isVersion()) {
            if (!columnReflection.getInsertableValue()) {
                throw new AptException(Message.DOMA4088, fieldElement, columnReflection.getAnnotationMirror(),
                        columnReflection.getInsertable());
            }
            if (!columnReflection.getUpdatableValue()) {
                throw new AptException(Message.DOMA4089, fieldElement, columnReflection.getAnnotationMirror(),
                        columnReflection.getUpdatable());
            }
        }
    }

    private boolean isNumber(CtType ctType) {
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
                throw new AptException(Message.DOMA4232, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4233, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitHolderCtType(HolderCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4204, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4205, fieldElement,
                        new Object[] { ctType.getQualifiedName() });
            }
            return null;
        }

        @Override
        public Void visitAnyCtType(AnyCtType ctType, Void p)
                throws RuntimeException {
            throw new AptException(Message.DOMA4096, fieldElement,
                    new Object[] { ctType.getTypeMirror() });
        }

    }
}
