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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileSelectQueryMeta> {

    public SqlFileSelectQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileSelectQueryMeta queryMeta = createSqlFileSelectQueryMeta(method,
                daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFile(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected SqlFileSelectQueryMeta createSqlFileSelectQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        Select select = method.getAnnotation(Select.class);
        if (select == null) {
            return null;
        }
        SqlFileSelectQueryMeta queryMeta = new SqlFileSelectQueryMeta();
        queryMeta.setQueryTimeout(select.queryTimeout());
        queryMeta.setFetchSize(select.fetchSize());
        queryMeta.setMaxRows(select.maxRows());
        queryMeta.setIterated(select.iterate());
        queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    @Override
    protected void doReturnType(SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryResultMeta resultMeta = new QueryResultMeta();
        queryMeta.setQueryResultMeta(resultMeta);
        TypeMirror returnType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), method.getReturnType());
        if (queryMeta.isIterated()) {
            resultMeta.setTypeName(TypeUtil.getTypeName(returnType, env));
            IterationCallbackMeta callbackMeta = queryMeta
                    .getIterationCallbackMeta();
            if (returnType == null
                    || callbackMeta == null
                    || callbackMeta.getResultType() == null
                    || !env.getTypeUtils().isSameType(
                            TypeUtil.toWrapperTypeIfPrimitive(returnType, env),
                            callbackMeta.getResultType())) {
                throw new AptException(DomaMessageCode.DOMA4055, env, method,
                        returnType, callbackMeta.getResultType());
            }
        } else if (isCollection(returnType)) {
            resultMeta.setCollection(true);
            resultMeta.setTypeName(TypeUtil.getTypeName(returnType, env));
            DeclaredType listTyep = TypeUtil.toDeclaredType(returnType, env);
            List<? extends TypeMirror> args = listTyep.getTypeArguments();
            if (args.isEmpty()) {
                throw new AptException(DomaMessageCode.DOMA4006, env, method);
            }
            TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), args.get(0));
            resultMeta.setElementTypeName(TypeUtil
                    .getTypeName(elementType, env));
            if (isEntity(elementType, daoMeta)) {
                resultMeta.setEntity(true);
            } else {
                TypeMirror wrapperType = DomaTypes.getWrapperType(elementType,
                        env);
                if (wrapperType == null) {
                    throw new AptException(DomaMessageCode.DOMA4007, env,
                            method, elementType);
                }
                resultMeta.setWrapperTypeName(TypeUtil.getTypeName(wrapperType,
                        env));
            }
        } else {
            resultMeta.setTypeName(TypeUtil.getTypeName(returnType, env));
            if (isEntity(returnType, daoMeta)) {
                resultMeta.setEntity(true);
            } else {
                TypeMirror wrapperType = DomaTypes.getWrapperType(returnType,
                        env);
                if (wrapperType == null) {
                    throw new AptException(DomaMessageCode.DOMA4008, env,
                            method, returnType);
                }
                resultMeta.setWrapperTypeName(TypeUtil.getTypeName(wrapperType,
                        env));
            }
        }
    }

    @Override
    protected void doParameters(SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement param : method.getParameters()) {
            QueryParameterMeta queryParameterMeta = new QueryParameterMeta();
            TypeMirror parameterType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            String parameterName = ElementUtil.getParameterName(param);
            String parameterTypeName = TypeUtil.getTypeName(parameterType,
                    daoMeta.getTypeParameterMap(), env);
            queryParameterMeta.setName(parameterName);
            queryParameterMeta.setTypeName(parameterTypeName);
            queryParameterMeta.setTypeMirror(parameterType);
            TypeElement typeElement = TypeUtil
                    .toTypeElement(parameterType, env);
            if (typeElement != null) {
                queryParameterMeta.setQualifiedName(typeElement.getQualifiedName()
                        .toString());
            }
            if (isOptions(parameterType, queryMeta.getOptionsClass())) {
                if (queryMeta.getOptionsName() != null) {
                    throw new AptException(DomaMessageCode.DOMA4053, env,
                            method);
                }
                queryMeta.setOptionsName(parameterName);
                queryMeta.setOptionsTypeName(parameterTypeName);
            } else if (isIterationCallback(parameterType)) {
                if (queryMeta.getIterationCallbackMeta() != null) {
                    throw new AptException(DomaMessageCode.DOMA4054, env,
                            method);
                }
                IterationCallbackMeta callbackMeta = new IterationCallbackMeta();
                callbackMeta.setQueryParameterMeta(queryParameterMeta);
                doIterationCallbackParameter(parameterType, callbackMeta,
                        method, daoMeta);
                queryMeta.setIterationCallbackMeta(callbackMeta);
            } else if (isCollection(parameterType)) {
                DeclaredType listTyep = TypeUtil.toDeclaredType(parameterType,
                        env);
                List<? extends TypeMirror> args = listTyep.getTypeArguments();
                if (args.isEmpty()) {
                    throw new AptException(DomaMessageCode.DOMA4027, env,
                            method);
                }
                TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                        .getTypeParameterMap(), args.get(0));
                if (!isDomain(elementType)) {
                    throw new AptException(DomaMessageCode.DOMA4028, env,
                            method);
                }
            } else if (!isEntity(parameterType, daoMeta)) {
                queryParameterMeta.setNullable(true);
            }
            queryMeta.addQueryParameterMetas(queryParameterMeta);
            queryMeta.addExpressionParameterType(parameterName, parameterType);
        }
        if (queryMeta.isIterated()
                && queryMeta.getIterationCallbackMeta() == null) {
            throw new AptException(DomaMessageCode.DOMA4056, env, method);
        }
        if (!queryMeta.isIterated()
                && queryMeta.getIterationCallbackMeta() != null) {
            throw new AptException(DomaMessageCode.DOMA4057, env, method);
        }
    }

    protected void doIterationCallbackParameter(TypeMirror parameterType,
            IterationCallbackMeta callbackMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        if (callbackMeta.getResultTypeName() != null) {
            return;
        }
        TypeElement typeElement = TypeUtil.toTypeElement(parameterType, env);
        if (typeElement == null) {
            return;
        }
        if (typeElement.getQualifiedName().contentEquals(
                IterationCallback.class.getName())) {
            DeclaredType declaredType = TypeUtil.toDeclaredType(parameterType,
                    env);
            if (declaredType == null) {
                throw new AptIllegalStateException();
            }
            List<? extends TypeMirror> actualArgs = declaredType
                    .getTypeArguments();
            assertEquals(2, actualArgs.size());
            TypeMirror resultType = actualArgs.get(0);
            callbackMeta.setResultType(resultType);
            callbackMeta.setResultTypeName(TypeUtil
                    .getTypeName(resultType, env));
            TypeMirror targetType = actualArgs.get(1);
            callbackMeta.setTargetType(targetType);
            callbackMeta.setTargetTypeName(TypeUtil
                    .getTypeName(targetType, env));
            if (isEntity(targetType, daoMeta)) {
                callbackMeta.setEntityTarget(true);
            } else if (DomaTypes.isSupportedType(targetType, env)) {
                TypeMirror targetWrapperType = DomaTypes.getWrapperType(
                        targetType, env);
                callbackMeta.setTargetWrapperType(targetWrapperType);
                callbackMeta.setTargetWrapperTypeName(TypeUtil.getTypeName(
                        targetWrapperType, env));
            } else {
                throw new AptException(DomaMessageCode.DOMA4058, env, method,
                        targetType);
            }
            return;
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                parameterType)) {
            doIterationCallbackParameter(supertype, callbackMeta, method,
                    daoMeta);
        }
    }
}
