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
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Procedure;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

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
        AutoProcedureQueryMeta queryMeta = new AutoProcedureQueryMeta();
        Procedure procedure = method.getAnnotation(Procedure.class);
        if (procedure == null) {
            return null;
        }
        queryMeta.setQueryTimeout(procedure.queryTimeout());
        queryMeta.setQueryKind(QueryKind.AUTO_PROCEDURE);
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        doProcedureName(procedure, queryMeta, method, daoMeta);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected void doProcedureName(Procedure procedure,
            AutoProcedureQueryMeta queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        StringBuilder buf = new StringBuilder();
        if (!procedure.catalog().isEmpty()) {
            buf.append(procedure.catalog());
            buf.append(".");
        }
        if (!procedure.schema().isEmpty()) {
            buf.append(procedure.schema());
            buf.append(".");
        }
        if (!procedure.name().isEmpty()) {
            buf.append(procedure.name());
        } else {
            buf.append(queryMeta.getName());
        }
        queryMeta.setProcedureName(buf.toString());
    }

    @Override
    protected void doReturnType(AutoProcedureQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        if (!isPrimitiveVoid(returnType)) {
            throw new AptException(DomaMessageCode.DOMA4064, env, method);
        }
        QueryResultMeta resultMeta = new QueryResultMeta();
        resultMeta.setTypeName(TypeUtil.getTypeName(returnType, env));
        queryMeta.setQueryResultMeta(resultMeta);
    }
}
