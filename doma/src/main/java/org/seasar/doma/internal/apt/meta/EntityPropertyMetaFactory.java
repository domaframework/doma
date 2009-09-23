/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Column;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.TableGenerator;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

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
        EntityPropertyMeta propertyMeta = new EntityPropertyMeta();
        doName(propertyMeta, fieldElement, entityMeta);
        doId(propertyMeta, fieldElement, entityMeta);
        doTransient(propertyMeta, fieldElement, entityMeta);
        doVersion(propertyMeta, fieldElement, entityMeta);
        doColumnMeta(propertyMeta, fieldElement, entityMeta);
        doPrimitive(propertyMeta, fieldElement, entityMeta);
        doWrapperTypeName(propertyMeta, fieldElement, entityMeta);
        return propertyMeta;
    }

    protected void doId(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Id id = fieldElement.getAnnotation(Id.class);
        if (id == null) {
            return;
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(DomaMessageCode.DOMA4036, env, fieldElement);
        }
        propertyMeta.setId(true);
        GeneratedValue generatedValue = fieldElement
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            return;
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(DomaMessageCode.DOMA4037, env, fieldElement);
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

    protected void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
    }

    protected void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        SequenceGenerator generator = fieldElement
                .getAnnotation(SequenceGenerator.class);
        if (generator == null) {
            throw new AptException(DomaMessageCode.DOMA4034, env, fieldElement);
        }
        StringBuilder buf = new StringBuilder();
        if (!generator.catalog().isEmpty()) {
            buf.append(generator.catalog());
            buf.append(".");
        }
        if (!generator.schema().isEmpty()) {
            buf.append(generator.schema());
            buf.append(".");
        }
        TypeMirror idGeneratorImplementerType = getIdGeneratorImplementerType(generator);
        buf.append(generator.sequence());
        SequenceIdGeneratorMeta idGeneratorMeta = new SequenceIdGeneratorMeta(
                buf.toString(), generator.initialValue(), generator
                        .allocationSize(), TypeUtil.getTypeName(
                        idGeneratorImplementerType, env));
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected TypeMirror getIdGeneratorImplementerType(
            SequenceGenerator generator) {
        try {
            generator.implementer();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    protected void doTableIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        TableGenerator generator = fieldElement
                .getAnnotation(TableGenerator.class);
        if (generator == null) {
            throw new AptException(DomaMessageCode.DOMA4035, env, fieldElement);
        }
        StringBuilder buf = new StringBuilder();
        if (!generator.catalog().isEmpty()) {
            buf.append(generator.catalog());
            buf.append(".");
        }
        if (!generator.schema().isEmpty()) {
            buf.append(generator.schema());
            buf.append(".");
        }
        TypeMirror idGeneratorImplementerType = getIdGeneratorImplementerType(generator);
        buf.append(generator.table());
        TableIdGeneratorMeta idGeneratorMeta = new TableIdGeneratorMeta(buf
                .toString(), generator.pkColumnName(), generator
                .valueColumnName(), generator.pkColumnValue(), generator
                .initialValue(), generator.allocationSize(), TypeUtil
                .getTypeName(idGeneratorImplementerType, env));
        propertyMeta.setIdGeneratorMeta(idGeneratorMeta);
    }

    protected TypeMirror getIdGeneratorImplementerType(TableGenerator generator) {
        try {
            generator.implementer();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    protected void doName(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        String name = fieldElement.getSimpleName().toString();
        if (name.startsWith("__")) {
            throw new AptException(DomaMessageCode.DOMA4025, env, fieldElement,
                    "__");
        }
        propertyMeta.setName(name);
    }

    protected void doTransient(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Transient trnsient = fieldElement.getAnnotation(Transient.class);
        propertyMeta.setTrnsient(trnsient != null);
    }

    protected void doVersion(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        Version version = fieldElement.getAnnotation(Version.class);
        if (version != null) {
            if (entityMeta.hasVersionPropertyMeta()) {
                throw new AptException(DomaMessageCode.DOMA4024, env,
                        fieldElement);
            }
            TypeMirror wrapperType = TypeUtil.toWrapperTypeIfPrimitive(
                    fieldElement.asType(), env);
            if (!TypeUtil.isAssignable(wrapperType, Number.class, env)) {
                throw new AptException(DomaMessageCode.DOMA4093, env,
                        fieldElement);
            }
            propertyMeta.setVersion(true);
        }
    }

    protected void doColumnMeta(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        ColumnMeta columnMeta = new ColumnMeta();
        Column column = fieldElement.getAnnotation(Column.class);
        if (column != null) {
            if (propertyMeta.isTrnsient()) {
                throw new AptException(DomaMessageCode.DOMA4087, env,
                        fieldElement);
            }
            if (propertyMeta.isId() || propertyMeta.isVersion()) {
                if (!column.insertable()) {
                    throw new AptException(DomaMessageCode.DOMA4088, env,
                            fieldElement);
                }
                if (!column.updatable()) {
                    throw new AptException(DomaMessageCode.DOMA4089, env,
                            fieldElement);
                }
            }
            if (!column.name().isEmpty()) {
                columnMeta.setName(column.name());
            }
            columnMeta.setInsertable(column.insertable());
            columnMeta.setUpdatable(column.updatable());
        }
        propertyMeta.setColumnMeta(columnMeta);
    }

    protected void doPrimitive(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        TypeMirror fieldType = fieldElement.asType();
        propertyMeta.setPrimitive(fieldType.getKind().isPrimitive());
    }

    protected void doWrapperTypeName(EntityPropertyMeta propertyMeta,
            VariableElement fieldElement, EntityMeta entityMeta) {
        if (propertyMeta.isTrnsient()) {
            return;
        }
        TypeMirror fieldType = fieldElement.asType();
        DeclaredType wrappedType = TypeUtil.toDeclaredType(TypeUtil
                .toWrapperTypeIfPrimitive(fieldType, env), env);
        DeclaredType wrapperType = WrapperResolver.getWrapperType(wrappedType,
                env);
        if (wrapperType == null) {
            throw new AptException(DomaMessageCode.DOMA4096, env, fieldElement,
                    wrappedType);
        }
        propertyMeta.setWrapperTypeName(TypeUtil.getTypeName(wrapperType, env));
    }

    protected boolean isNumber(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, Number.class, env);
    }

}
