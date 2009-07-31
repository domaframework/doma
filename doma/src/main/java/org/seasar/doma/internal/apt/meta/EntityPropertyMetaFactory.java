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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
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
import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.NumberDomain;
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
            ExecutableElement method, EntityMeta entityMeta) {
        assertNotNull(method, entityMeta);
        EntityPropertyMeta propertyMeta = new EntityPropertyMeta();
        propertyMeta.setExecutableElement(method);
        doName(propertyMeta, method, entityMeta);
        doId(propertyMeta, method, entityMeta);
        doTransient(propertyMeta, method, entityMeta);
        doVersion(propertyMeta, method, entityMeta);
        doColumnMeta(propertyMeta, method, entityMeta);
        doTypeParameters(propertyMeta, method, entityMeta);
        doReturnType(propertyMeta, method, entityMeta);
        doParameters(propertyMeta, method, entityMeta);
        doThrowTypes(propertyMeta, method, entityMeta);
        return propertyMeta;
    }

    protected void doId(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        Id id = method.getAnnotation(Id.class);
        if (id == null) {
            return;
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(DomaMessageCode.DOMA4036, env, method);
        }
        propertyMeta.setId(true);
        GeneratedValue generatedValue = method
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            return;
        }
        if (entityMeta.hasGeneratedIdPropertyMeta()) {
            throw new AptException(DomaMessageCode.DOMA4037, env, method);
        }
        switch (generatedValue.strategy()) {
        case IDENTITY:
            doIdentityIdGeneratorMeta(propertyMeta, method, entityMeta);
            break;
        case SEQUENCE:
            doSequenceIdGeneratorMeta(propertyMeta, method, entityMeta);
            break;
        case TABLE:
            doTableIdGeneratorMeta(propertyMeta, method, entityMeta);
            break;
        case ASSIGNED:
            break;
        default:
            assertUnreachable();
            break;
        }
    }

    protected void doIdentityIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        propertyMeta.setIdGeneratorMeta(new IdentityIdGeneratorMeta());
    }

    protected void doSequenceIdGeneratorMeta(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        SequenceGenerator generator = method
                .getAnnotation(SequenceGenerator.class);
        if (generator == null) {
            throw new AptException(DomaMessageCode.DOMA4034, env, method);
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
                        .allocationSize(), TypeUtil
                        .getTypeName(idGeneratorImplementerType, entityMeta
                                .getTypeParameterMap(), env));
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
            ExecutableElement method, EntityMeta entityMeta) {
        TableGenerator generator = method.getAnnotation(TableGenerator.class);
        if (generator == null) {
            throw new AptException(DomaMessageCode.DOMA4035, env, method);
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
                .getTypeName(idGeneratorImplementerType, entityMeta
                        .getTypeParameterMap(), env));
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
            ExecutableElement method, EntityMeta entityMeta) {
        String name = method.getSimpleName().toString();
        if (name.startsWith("__")) {
            throw new AptException(DomaMessageCode.DOMA4025, env, method, "__");
        }
        propertyMeta.setName(name);
    }

    protected void doTransient(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        Transient trnsient = method.getAnnotation(Transient.class);
        propertyMeta.setTrnsient(trnsient != null);
    }

    protected void doVersion(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        Version version = method.getAnnotation(Version.class);
        if (version != null) {
            if (entityMeta.hasVersionPropertyMeta()) {
                throw new AptException(DomaMessageCode.DOMA4024, env, method);
            }
            propertyMeta.setVersion(true);
        }
    }

    protected void doColumnMeta(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        ColumnMeta columnMeta = new ColumnMeta();
        Column column = method.getAnnotation(Column.class);
        if (column != null) {
            if (!column.name().isEmpty()) {
                columnMeta.setName(column.name());
            }
            columnMeta.setInsertable(column.insertable());
            columnMeta.setUpdatable(column.updatable());
        }
        propertyMeta.setColumnMeta(columnMeta);
    }

    protected void doTypeParameters(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        for (TypeParameterElement element : method.getTypeParameters()) {
            String name = TypeUtil.getTypeName(element.asType(), entityMeta
                    .getTypeParameterMap(), env);
            propertyMeta.addTypeParameterName(name);
        }
    }

    protected void doReturnType(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        TypeMirror returnType = TypeUtil.resolveTypeParameter(entityMeta
                .getTypeParameterMap(), method.getReturnType());
        if (isList(returnType)) {
            DeclaredType listTyep = TypeUtil.toDeclaredType(returnType, env);
            List<? extends TypeMirror> args = listTyep.getTypeArguments();
            if (args.isEmpty()) {
                throw new AptException(DomaMessageCode.DOMA4029, env, method);
            }
            TypeMirror elementType = TypeUtil.resolveTypeParameter(entityMeta
                    .getTypeParameterMap(), args.get(0));
            if (!isDomain(elementType)) {
                throw new AptException(DomaMessageCode.DOMA4030, env, method);
            }
            if (!propertyMeta.isTrnsient()) {
                throw new AptException(DomaMessageCode.DOMA4031, env, method);
            }
            propertyMeta.setListReturnType(true);
            String elementTypeName = TypeUtil
                    .getTypeName(elementType, entityMeta.getTypeParameterMap(), env);
            propertyMeta.setReturnElementTypeName(elementTypeName);
            propertyMeta.setReturnTypeName(ArrayList.class.getName() + "<"
                    + elementTypeName + ">");
            propertyMeta.setParameterizedReturnType(true);
            return;
        } else if (!isDomain(returnType) || isAbstract(returnType)) {
            throw new AptException(DomaMessageCode.DOMA4022, env, method);
        }
        if (propertyMeta.isVersion()
                && (!isNumberDomain(returnType) || isAbstract(returnType))) {
            throw new AptException(DomaMessageCode.DOMA4032, env, method);
        }
        if (propertyMeta.getIdGeneratorMeta() != null
                && (!isNumberDomain(returnType) || isAbstract(returnType))) {
            throw new AptException(DomaMessageCode.DOMA4033, env, method);
        }
        propertyMeta
                .setReturnTypeName(TypeUtil.getTypeName(returnType, entityMeta
                        .getTypeParameterMap(), env));
        TypeElement returnElement = TypeUtil.toTypeElement(returnType, env);
        if (returnElement != null
                && !returnElement.getTypeParameters().isEmpty()) {
            propertyMeta.setParameterizedReturnType(true);
        }
    }

    protected void doParameters(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        List<? extends VariableElement> params = method.getParameters();
        if (!params.isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4023, env, method);
        }
    }

    protected void doThrowTypes(EntityPropertyMeta propertyMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        for (TypeMirror thrownType : method.getThrownTypes()) {
            String typeName = TypeUtil.getTypeName(thrownType, entityMeta
                    .getTypeParameterMap(), env);
            propertyMeta.addThrownTypeName(typeName);
        }
    }

    protected boolean isDomain(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, Domain.class, env);
    }

    protected boolean isNumberDomain(TypeMirror typeMirror) {
        return TypeUtil.isAssignable(typeMirror, NumberDomain.class, env);
    }

    protected boolean isAbstract(TypeMirror typeMirror) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        return typeElement != null
                && typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    protected boolean isList(TypeMirror typeMirror) {
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        if (typeElement != null) {
            return typeElement.getQualifiedName().contentEquals(List.class
                    .getName());
        }
        return false;
    }

}
