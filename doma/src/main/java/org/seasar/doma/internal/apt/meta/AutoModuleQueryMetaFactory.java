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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    public AutoModuleQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
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
            CallableSqlParameterMeta parameterMeta = createParameterMeta(
                    queryMeta, param, method, daoMeta);
            parameterMeta.setName(name);
            parameterMeta.setTypeName(typeName);
            queryMeta.addCallableSqlParameterMeta(parameterMeta);
            queryMeta.addMethodParameterName(name, typeName);
            queryMeta.addExpressionParameterType(name, parameterType);
        }
    }

    protected CallableSqlParameterMeta createParameterMeta(
            AutoModuleQueryMeta queryMeta, VariableElement param,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror paramType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), param.asType());
        if (param.getAnnotation(ResultSet.class) != null) {
            if (isList(paramType)) {
                DeclaredType listTyep = TypeUtil.toDeclaredType(paramType, env);
                List<? extends TypeMirror> args = listTyep.getTypeArguments();
                if (args.isEmpty()) {
                    throw new AptException(DomaMessageCode.DOMA4041, env,
                            method);
                }
                TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), args.get(0));
                String elementTypeName = TypeUtil.getTypeName(elementType,
                        daoMeta.getTypeParameterMap(), env);
                if (isEntity(elementType, daoMeta)) {
                    return new EntityListParameterMeta(elementTypeName);
                }
                if (isDomain(elementType)) {
                    return new DomainListParameterMeta(elementTypeName);
                }
                throw new AptException(DomaMessageCode.DOMA4061, env, param);
            }
            throw new AptException(DomaMessageCode.DOMA4062, env, param);
        }
        if (!isDomain(paramType)) {
            throw new AptException(DomaMessageCode.DOMA4060, env, param);
        }
        if (param.getAnnotation(Out.class) != null) {
            return new OutParameterMeta();
        }
        if (param.getAnnotation(InOut.class) != null) {
            return new InOutParameterMeta();
        }
        if (param.getAnnotation(In.class) != null) {
            return new InParameterMeta();
        }
        throw new AptException(DomaMessageCode.DOMA4066, env, param);
    }
}
