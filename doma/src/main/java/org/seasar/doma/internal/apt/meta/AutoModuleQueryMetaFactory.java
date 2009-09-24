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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    public AutoModuleQueryMetaFactory(ProcessingEnvironment env,
            DomainMetaFactory domainMetaFactory) {
        super(env, domainMetaFactory);
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (VariableElement param : method.getParameters()) {
            TypeMirror parameterType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            String typeName = TypeUtil.getTypeName(parameterType, daoMeta
                    .getTypeParameterMap(), env);
            String name = ElementUtil.getParameterName(param);
            CallableSqlParameterMeta callableSqlParameterMeta = createParameterMeta(
                    queryMeta, param, daoMeta);
            callableSqlParameterMeta.setName(name);
            callableSqlParameterMeta.setTypeName(typeName);
            queryMeta.addCallableSqlParameterMeta(callableSqlParameterMeta);

            QueryParameterMeta queryParameterMeta = new QueryParameterMeta();
            queryParameterMeta.setName(callableSqlParameterMeta.getName());
            queryParameterMeta.setTypeName(callableSqlParameterMeta
                    .getTypeName());
            queryParameterMeta.setNullable(callableSqlParameterMeta
                    .isNullable());
            queryParameterMeta.setTypeMirror(parameterType);
            TypeElement typeElement = TypeUtil
                    .toTypeElement(parameterType, env);
            if (typeElement != null) {
                queryParameterMeta.setQualifiedName(typeElement
                        .getQualifiedName().toString());
            }
            queryMeta.addQueryParameterMetas(queryParameterMeta);

            queryMeta.addExpressionParameterType(name, parameterType);
        }
    }

    protected CallableSqlParameterMeta createParameterMeta(
            AutoModuleQueryMeta queryMeta, VariableElement param,
            DaoMeta daoMeta) {
        TypeMirror paramType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), param.asType());
        if (param.getAnnotation(ResultSet.class) != null) {
            if (isCollection(paramType)) {
                DeclaredType listTyep = TypeUtil.toDeclaredType(paramType, env);
                if (listTyep.getTypeArguments().isEmpty()) {
                    throw new AptException(DomaMessageCode.DOMA4041, env, param);
                }
                TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), listTyep.getTypeArguments()
                        .get(0));
                String elementTypeName = TypeUtil.getTypeName(elementType,
                        daoMeta.getTypeParameterMap(), env);
                if (isEntity(elementType, daoMeta)) {
                    return new EntityListParameterMeta(elementTypeName);
                }
                TypeMirror wrapperType = DomaTypes.getWrapperType(elementType,
                        env);
                if (wrapperType == null) {
                    throw new AptException(DomaMessageCode.DOMA4061, env,
                            param, elementType);
                }
                return new ValueListParameterMeta(elementTypeName, TypeUtil
                        .getTypeName(wrapperType, env));
            }
            throw new AptException(DomaMessageCode.DOMA4062, env, param);
        }
        if (param.getAnnotation(Out.class) != null
                || param.getAnnotation(InOut.class) != null) {
            if (!TypeUtil.isSameType(paramType, Reference.class, env)) {
                throw new AptException(DomaMessageCode.DOMA4098, env, param);
            }
            DeclaredType declaredType = TypeUtil.toDeclaredType(paramType, env);
            if (declaredType == null) {
                throw new AptIllegalStateException();
            }
            if (declaredType.getTypeArguments().isEmpty()) {
                throw new AptException(DomaMessageCode.DOMA4099, env, param);
            }
            TypeMirror argumentType = declaredType.getTypeArguments().get(0);
            TypeMirror wrapperType = DomaTypes
                    .getWrapperType(argumentType, env);
            if (wrapperType == null) {
                throw new AptException(DomaMessageCode.DOMA4100, env, param,
                        argumentType);
            }
            if (param.getAnnotation(Out.class) != null) {
                return new OutParameterMeta(TypeUtil.getTypeName(argumentType,
                        env), TypeUtil.getTypeName(wrapperType, env));
            }
            return new InOutParameterMeta(TypeUtil.getTypeName(argumentType,
                    env), TypeUtil.getTypeName(wrapperType, env));
        }
        if (param.getAnnotation(In.class) != null) {
            TypeMirror wrapperType = DomaTypes.getWrapperType(paramType, env);
            if (wrapperType == null) {
                throw new AptException(DomaMessageCode.DOMA4101, env, param,
                        paramType);
            }
            return new InParameterMeta(TypeUtil.getTypeName(wrapperType, env));
        }
        throw new AptException(DomaMessageCode.DOMA4066, env, param);
    }
}
