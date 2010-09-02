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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.mirror.ProcedureMirror;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class AutoProcedureQueryMetaFactory extends
        AutoModuleQueryMetaFactory<AutoProcedureQueryMeta> {

    public AutoProcedureQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        ProcedureMirror procedureMirror = ProcedureMirror.newInstance(method,
                env);
        if (procedureMirror == null) {
            return null;
        }
        AutoProcedureQueryMeta queryMeta = new AutoProcedureQueryMeta(method);
        queryMeta.setQueryKind(QueryKind.AUTO_PROCEDURE);
        queryMeta.setProcedureMirror(procedureMirror);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    @Override
    protected void doReturnType(AutoProcedureQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta resultMeta = createReturnMeta(method);
        if (!resultMeta.isPrimitiveVoid()) {
            throw new AptException(Message.DOMA4064, env,
                    resultMeta.getElement());
        }
        queryMeta.setReturnMeta(resultMeta);
    }
}
