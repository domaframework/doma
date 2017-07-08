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

import java.io.File;

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.reflection.ScriptReflection;
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileScriptQueryMetaFactory
        extends AbstractSqlFileQueryMetaFactory<SqlFileScriptQueryMeta> {

    public SqlFileScriptQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
        super(ctx, methodElement);
    }

    @Override
    public QueryMeta createQueryMeta() {
        SqlFileScriptQueryMeta queryMeta = createSqlFileScriptQueryMeta();
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta);
        doReturnType(queryMeta);
        doParameters(queryMeta);
        doThrowTypes(queryMeta);
        doSqlFiles(queryMeta, false, false);
        return queryMeta;
    }

    private SqlFileScriptQueryMeta createSqlFileScriptQueryMeta() {
        SqlFileScriptQueryMeta queryMeta = new SqlFileScriptQueryMeta(methodElement);
        ScriptReflection scriptReflection = ctx.getReflections().newScriptReflection(methodElement);
        if (scriptReflection == null) {
            return null;
        }
        queryMeta.setScriptReflection(scriptReflection);
        queryMeta.setQueryKind(QueryKind.SQLFILE_SCRIPT);
        return queryMeta;
    }

    @Override
    protected void doReturnType(SqlFileScriptQueryMeta queryMeta) {
        QueryReturnMeta returnMeta = createReturnMeta();
        if (!returnMeta.isPrimitiveVoid()) {
            throw new AptException(Message.DOMA4172, returnMeta.getMethodElement());
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(SqlFileScriptQueryMeta queryMeta) {
        if (!methodElement.getParameters().isEmpty()) {
            throw new AptException(Message.DOMA4173, methodElement);
        }
    }

    @Override
    protected void doSqlFiles(SqlFileScriptQueryMeta queryMeta, boolean expandable,
            boolean populatable) {
        String filePath = ScriptFileUtil.buildPath(getDaoElement().getQualifiedName().toString(),
                queryMeta.getName());
        File file = getFile(queryMeta, filePath);
        File[] siblingfiles = getSiblingFiles(queryMeta, file);
        String methodName = queryMeta.getName();
        for (File siblingfile : siblingfiles) {
            if (ScriptFileUtil.isScriptFile(siblingfile, methodName)) {
                String fileName = siblingfile.getName();
                queryMeta.addFileName(fileName);
            }
        }
    }
}
