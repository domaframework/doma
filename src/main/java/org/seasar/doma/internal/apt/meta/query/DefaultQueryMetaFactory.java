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
package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.Context;

/**
 * @author taedium
 * 
 */
public class DefaultQueryMetaFactory extends AbstractQueryMetaFactory<DefaultQueryMeta> {

    public DefaultQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
        super(ctx, methodElement);
    }

    @Override
    public QueryMeta createQueryMeta() {
        if (!methodElement.isDefault()) {
            return null;
        }
        DefaultQueryMeta queryMeta = new DefaultQueryMeta(methodElement);
        queryMeta.setQueryKind(QueryKind.DEFAULT);
        doTypeParameters(queryMeta);
        doParameters(queryMeta);
        doReturnType(queryMeta);
        doThrowTypes(queryMeta);
        return queryMeta;
    }

    @Override
    protected void doParameters(DefaultQueryMeta queryMeta) {
        for (VariableElement parameter : methodElement.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
            }
        }
    }

    @Override
    protected void doReturnType(DefaultQueryMeta queryMeta) {
        QueryReturnMeta resultMeta = createReturnMeta();
        queryMeta.setReturnMeta(resultMeta);
    }

}
