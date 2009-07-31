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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Function;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMetaFactory extends
        AutoModuleQueryMetaFactory<AutoFunctionQueryMeta> {

    public AutoFunctionQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoFunctionQueryMeta queryMeta = new AutoFunctionQueryMeta();
        Function function = method.getAnnotation(Function.class);
        if (function == null) {
            return null;
        }
        queryMeta.setQueryTimeout(function.queryTimeout());
        queryMeta.setQueryKind(QueryKind.AUTO_FUNCTION);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        doFunctionName(function, queryMeta, method, daoMeta);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected void doFunctionName(Function function,
            AutoFunctionQueryMeta queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        StringBuilder buf = new StringBuilder();
        if (!function.catalog().isEmpty()) {
            buf.append(function.catalog());
            buf.append(".");
        }
        if (!function.schema().isEmpty()) {
            buf.append(function.schema());
            buf.append(".");
        }
        if (!function.name().isEmpty()) {
            buf.append(function.name());
        } else {
            buf.append(queryMeta.getName());
        }
        queryMeta.setFunctionName(buf.toString());
    }

    @Override
    protected void doReturnType(AutoFunctionQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        String typeName = TypeUtil.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env);
        queryMeta.setReturnTypeName(typeName);
        ResultParameterMeta resultParameterMeta = createResultParameterMeta(queryMeta, returnType, method, daoMeta);
        queryMeta.setResultParameterMeta(resultParameterMeta);
    }

    protected ResultParameterMeta createResultParameterMeta(
            AutoFunctionQueryMeta queryMeta, TypeMirror returnType,
            ExecutableElement method, DaoMeta daoMeta) {
        if (isList(returnType)) {
            DeclaredType listTyep = TypeUtil.toDeclaredType(returnType, env);
            List<? extends TypeMirror> args = listTyep.getTypeArguments();
            if (args.isEmpty()) {
                throw new AptException(DomaMessageCode.DOMA4029, env, method);
            }
            TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), args.get(0));
            String elementTypeName = TypeUtil.getTypeName(elementType, daoMeta
                    .getTypeParameterMap(), env);
            if (isEntity(elementType, daoMeta)) {
                return new EntityListResultParameterMeta(elementTypeName);
            }
            if (isDomain(elementType)) {
                return new DomainListResultParameterMeta(elementTypeName);
            }
            throw new AptException(DomaMessageCode.DOMA4065, env, method);
        }
        if (isDomain(returnType)) {
            return new DomainResultParameterMeta(queryMeta.getReturnTypeName());
        }
        throw new AptException(DomaMessageCode.DOMA4063, env, method);
    }
}
