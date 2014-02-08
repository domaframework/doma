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

import java.sql.NClob;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.mirror.NClobFactoryMirror;

/**
 * @author taedium
 * 
 */
public class NClobCreateQueryMetaFactory extends
        AbstractCreateQueryMetaFactory<NClobCreateQueryMeta> {

    public NClobCreateQueryMetaFactory(ProcessingEnvironment env) {
        super(env, NClob.class);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        NClobFactoryMirror nClobFactoryMirror = NClobFactoryMirror.newInstance(
                method, env);
        if (nClobFactoryMirror == null) {
            return null;
        }
        NClobCreateQueryMeta queryMeta = new NClobCreateQueryMeta(method);
        queryMeta.setNClobFactoryMirror(nClobFactoryMirror);
        queryMeta.setQueryKind(QueryKind.NCLOB_FACTORY);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

}
