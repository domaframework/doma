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
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.Function;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.BasicType;
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
        QueryReturnMeta returnMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(returnMeta);
        ResultParameterMeta resultParameterMeta = createCallableSqlResultParameterMeta(returnMeta);
        queryMeta.setResultParameterMeta(resultParameterMeta);
    }

    protected ResultParameterMeta createCallableSqlResultParameterMeta(
            QueryReturnMeta returnMeta) {
        ListType listType = returnMeta.getCollectionType();
        if (listType != null) {
            EntityType entityType = listType.getEntityType();
            if (entityType != null) {
                return new EntityListResultParameterMeta(entityType);
            }
            DomainType domainType = listType.getDomainType();
            if (domainType != null) {
                return new DomainListResultParameterMeta(domainType);
            }
            BasicType basicType = listType.getValueType();
            if (basicType != null) {
                return new BasicListResultParameterMeta(basicType);
            }
            throw new AptException(DomaMessageCode.DOMA4065, env, returnMeta
                    .getElement(), listType.getType());
        }
        DomainType domainType = returnMeta.getDomainType();
        if (domainType != null) {
            return new DomainResultParameterMeta(domainType);
        }
        BasicType basicType = returnMeta.getValueType();
        if (basicType != null) {
            return new BasicResultParameterMeta(basicType);
        }
        throw new AptException(DomaMessageCode.DOMA4063, env, returnMeta
                .getElement(), returnMeta.getType());
    }
}
