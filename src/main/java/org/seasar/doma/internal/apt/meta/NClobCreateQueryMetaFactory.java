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

import java.sql.NClob;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.NClobFactoryReflection;

/**
 * @author taedium
 * 
 */
public class NClobCreateQueryMetaFactory extends
        AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

    public NClobCreateQueryMetaFactory(Context ctx) {
        super(ctx, NClob.class);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        NClobFactoryReflection nClobFactoryReflection = ctx
                .getReflections()
                .newNClobFactoryReflection(method);
        if (nClobFactoryReflection == null) {
            return null;
        }
        NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(method,
                daoMeta.getDaoElement());
        queryMeta.setNClobFactoryReflection(nClobFactoryReflection);
        queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

}
