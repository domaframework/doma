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

import java.io.File;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.reflection.Reflections;
import org.seasar.doma.internal.apt.reflection.ScriptReflection;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileScriptQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

    public SqlFileScriptQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileScriptQueryMeta queryMeta = createSqlFileScriptQueryMeta(method,
                daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFiles(queryMeta, method, daoMeta, false, false);
        return queryMeta;
    }

    protected SqlFileScriptQueryMeta createSqlFileScriptQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SqlFileScriptQueryMeta queryMeta = new SqlFileScriptQueryMeta(method,
                daoMeta.getDaoElement());
        ScriptReflection scriptReflection = new Reflections(env)
                .newScriptReflection(method);
        if (scriptReflection == null) {
            return null;
        }
        queryMeta.setScriptReflection(scriptReflection);
        queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
        return queryMeta;
    }

    @Override
    protected void doReturnType(SqlFileScriptQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
        if (!returnMeta.isPrimitiveVoid()) {
            throw new AptException(Message.DOMA4172, env,
                    returnMeta.getMethodElement(), new Object[] {
                            daoMeta.getDaoElement().getQualifiedName(),
                            method.getSimpleName() });
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(SqlFileScriptQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        if (!method.getParameters().isEmpty()) {
            throw new AptException(Message.DOMA4173, env, method, new Object[] {
                    daoMeta.getDaoElement().getQualifiedName(),
                    method.getSimpleName() });
        }
    }

    @Override
    protected void doSqlFiles(SqlFileScriptQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta, boolean expandable,
            boolean populatable) {
        String filePath = ScriptFileUtil.buildPath(daoMeta.getDaoElement()
                .getQualifiedName().toString(), queryMeta.getName());
        File file = getFile(queryMeta, method, filePath);
        File[] siblingfiles = getSiblingFiles(queryMeta, method, file);
        String methodName = queryMeta.getName();
        for (File siblingfile : siblingfiles) {
            if (ScriptFileUtil.isScriptFile(siblingfile, methodName)) {
                String fileName = siblingfile.getName();
                queryMeta.addFileName(fileName);
            }
        }
    }
}
