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

import javax.annotation.processing.ProcessingEnvironment;
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
import org.seasar.doma.internal.apt.mirror.ColumnMirror;
import org.seasar.doma.internal.apt.mirror.SequenceGeneratorMirror;
import org.seasar.doma.internal.apt.mirror.TableGeneratorMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

/**
 * 
 * @author taedium
 * 
 */
public class EntityPropertyMetaFactory {

    protected final ProcessingEnvironment env;

    public EntityPropertyMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public EntityPropertyMeta createEntityPropertyMeta(
            VariableElement fieldElement, EntityMeta entityMeta) {
        assertNotNull(fieldElement, entityMeta);
        TypeElement entityElement = ElementUtil.toTypeElement(
                fieldElement.getEnclosingElement(), env);
        if (entityElement == null) {
            throw new AptIllegalStateException(fieldElement.toString());
        }
        EntityPropertyMeta propertyMeta = new EntityPropertyMeta(entityElement,
                fieldElement, entityMeta.getNamingType(),
                entityElement.equals(entityMeta.getEntityElement()), env);
        doDataType(propertyMeta, fieldElement, entityMeta);
        doName(propertyMeta, fieldElement, entityMeta);
        doId(propertyMeta, fieldElement, entityMeta);
        doVersion(propertyMeta, fieldElement, entityMeta);
        doColumn(propertyMeta, fieldElement, entityMeta);
        return propertyMeta;
    }

    protected void doDataType(EntityPropertyMeta propertyMeta,
            final VariableElement fieldElement, EntityMeta entityMeta) {
        final TypeMirror type = fieldElement.asType();
        final DomainType domainType = DomainType.newInstance(type, env);
        if (domainType != null) {
            if (domainType.isRawType()) {
                throw new AptException(Message.DOMA4204, env, fieldElement,
                        domainType.getQualifiedName());
            }
            if (domainType.isWildcardType()) {
                throw new AptException(Message.DOMA4205, env, fieldElement,
                        domainType.getQualifiedName());
            }
            propertyMeta.setDataType(domainType);
        } else {
            BasicType basicType = BasicType.newInstance(type, env);
            if (basicType != null) {
                propertyMeta.setDataType(basicType);
            } else {
                throw new AptException(Message.DOMA4096, env, fieldElement,
                        type);
            }
        }
    }

    protected void doName(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        String name = fieldElement.getSimpleName().toString();
        if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
            throw new AptException(Message.DOMA4025, env, fieldElement,
                    MetaConstants.RESERVED_NAME_PREFIX);
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
            throw new AptException(Message.DOMA4033, env, fieldElement);
        }
        propertyMeta.setId(true);
        GeneratedValue generatedValue = fieldElement
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            validateSequenceGeneratorNotExistent(propertyMeta, fieldElement,
                    entityMeta);
            validateTableGeneratorNotExistent(propertyMeta, fieldElement,
                    entityMeta);
            return;
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(Message.DOMA4037, env, fieldElement);
        }
        if (!isNumber(propertyMeta.getDataType())) {
            throw new AptException(Message.DOMA4095, env, fieldElement);
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
            throw new AptException(Message.DOMA4030, env, fieldElement);
        }
    }

    protected void validateTableGeneratorNotExistent(
            EntityPropertyMeta propertyMeta, VariableElement fieldElement,
            EntityMeta entityMeta) {
        TableGenerator tableGenerator = fieldElement
                .getAnnotation(TableGenerator.class);
        if (tableGenerator != null) {
            throw new AptException(Message.DOMA4031, env, fieldElement);
        }
    }

    protected void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
    }

    protected void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        SequenceGeneratorMirror sequenceGeneratorMirror = SequenceGeneratorMirror
                .newInstance(fieldElement, env);
        if (sequenceGeneratorMirror == null) {
            throw new AptException(Message.DOMA4034, env, fieldElement);
        }
        validateSequenceIdGenerator(propertyMeta, fieldElement,
                sequenceGeneratorMirror);
        SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(
                sequenceGeneratorMirror);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected void validateSequenceIdGenerator(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement,
            SequenceGeneratorMirror sequenceGeneratorMirror) {
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                sequenceGeneratorMirror.getImplementerValue(), env);
        if (typeElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4170, env, fieldElement,
                    sequenceGeneratorMirror.getAnnotationMirror(),
                    sequenceGeneratorMirror.getImplementer(),
                    typeElement.getQualifiedName());
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                typeElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4171, env, fieldElement,
                    sequenceGeneratorMirror.getAnnotationMirror(),
                    sequenceGeneratorMirror.getImplementer(),
                    typeElement.getQualifiedName());
        }
    }

    protected void doTableIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        TableGeneratorMirror tableGeneratorMirror = TableGeneratorMirror
                .newInstance(fieldElement, env);
        if (tableGeneratorMirror == null) {
            throw new AptException(Message.DOMA4035, env, fieldElement);
        }
        validateTableIdGenerator(propertyMeta, fieldElement,
                tableGeneratorMirror);
        TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(
                tableGeneratorMirror);
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected void validateTableIdGenerator(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement,
            TableGeneratorMirror tableGeneratorMirror) {
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(
                tableGeneratorMirror.getImplementerValue(), env);
        if (typeElement == null) {
            throw new AptIllegalStateException(
                    "failed to convert to TypeElement");
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4168, env, fieldElement,
                    tableGeneratorMirror.getAnnotationMirror(),
                    tableGeneratorMirror.getImplementer(),
                    typeElement.getQualifiedName());
        }
        ExecutableElement constructor = ElementUtil.getNoArgConstructor(
                typeElement, env);
        if (constructor == null
                || !constructor.getModifiers().contains(Modifier.PUBLIC)) {
            throw new AptException(Message.DOMA4169, env, fieldElement,
                    tableGeneratorMirror.getAnnotationMirror(),
                    tableGeneratorMirror.getImplementer(),
                    typeElement.getQualifiedName());
        }
    }

    protected void doVersion(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Version version = fieldElement.getAnnotation(Version.class);
        if (version != null) {
            if (entityMeta.hasVersionPropertyMeta()) {
                throw new AptException(Message.DOMA4024, env, fieldElement);
            }
            if (!isNumber(propertyMeta.getDataType())) {
                throw new AptException(Message.DOMA4093, env, fieldElement);
            }
            propertyMeta.setVersion(true);
        }
    }

    protected void doColumn(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        ColumnMirror columnMirror = ColumnMirror.newInstance(fieldElement, env);
        if (columnMirror == null) {
            return;
        }
        if (propertyMeta.isId() || propertyMeta.isVersion()) {
            if (!columnMirror.getInsertableValue()) {
                throw new AptException(Message.DOMA4088, env, fieldElement,
                        columnMirror.getAnnotationMirror(),
                        columnMirror.getInsertable());
            }
            if (!columnMirror.getUpdatableValue()) {
                throw new AptException(Message.DOMA4089, env, fieldElement,
                        columnMirror.getAnnotationMirror(),
                        columnMirror.getUpdatable());
            }
        }
        propertyMeta.setColumnMirror(columnMirror);
    }

    protected boolean isNumber(DataType dataType) {
        Boolean isNumber = dataType.accept(
                new SimpleDataTypeVisitor<Boolean, Void, RuntimeException>() {

                    @Override
                    public Boolean visitDomainType(DomainType dataType, Void p)
                            throws RuntimeException {
                        return dataType.getBasicType().accept(this, p);
                    }

                    @Override
                    public Boolean visitBasicType(BasicType dataType, Void p)
                            throws RuntimeException {
                        TypeMirror boxedType = TypeMirrorUtil.boxIfPrimitive(
                                dataType.getTypeMirror(), env);
                        return TypeMirrorUtil.isAssignable(boxedType,
                                Number.class, env);
                    }
                }, null);
        return isNumber == Boolean.TRUE;
    }

}
