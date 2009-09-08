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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileBatchModifyQueryMeta> {

    public SqlFileBatchModifyQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileBatchModifyQueryMeta queryMeta = createSqlFileBatchModifyQueryMeta(
                method, daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFile(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected SqlFileBatchModifyQueryMeta createSqlFileBatchModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SqlFileBatchModifyQueryMeta queryMeta = new SqlFileBatchModifyQueryMeta();
        BatchInsert insert = method.getAnnotation(BatchInsert.class);
        if (insert != null && insert.sqlFile()) {
            queryMeta.setQueryTimeout(insert.queryTimeout());
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_INSERT);
        }
        BatchUpdate update = method.getAnnotation(BatchUpdate.class);
        if (update != null && update.sqlFile()) {
            queryMeta.setQueryTimeout(update.queryTimeout());
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_UPDATE);
        }
        BatchDelete delete = method.getAnnotation(BatchDelete.class);
        if (delete != null && delete.sqlFile()) {
            queryMeta.setQueryTimeout(delete.queryTimeout());
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_DELETE);
        }
        if (queryMeta.getQueryKind() == null) {
            return null;
        }
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    @Override
    protected void doReturnType(SqlFileBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        if (!isPrimitiveIntArray(returnType)) {
            throw new AptException(DomaMessageCode.DOMA4040, env, method);
        }
        queryMeta.setReturnTypeName(TypeUtil.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env));
    }

    @Override
    protected void doParameters(SqlFileBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> params = method.getParameters();
        int size = params.size();
        if (size != 1) {
            throw new AptException(DomaMessageCode.DOMA4002, env, method);
        }
        VariableElement entityList = params.get(0);
        TypeMirror entityListType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), entityList.asType());
        if (!isList(entityListType)) {
            throw new AptException(DomaMessageCode.DOMA4042, env, method);
        }
        DeclaredType listTyep = TypeUtil.toDeclaredType(entityListType, env);
        List<? extends TypeMirror> args = listTyep.getTypeArguments();
        if (args.isEmpty()) {
            throw new AptException(DomaMessageCode.DOMA4041, env, method);
        }
        TypeMirror elementType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), args.get(0));
        if (!isEntity(elementType, daoMeta)) {
            throw new AptException(DomaMessageCode.DOMA4043, env, method);
        }
        String entityListName = ElementUtil.getParameterName(entityList);
        String entityListTypeName = TypeUtil.getTypeName(entityListType,
                daoMeta.getTypeParameterMap(), env);
        queryMeta.setEntityListName(entityListName);
        queryMeta.setEntityListTypeName(entityListTypeName);
        queryMeta.setElementTypeName(TypeUtil.getTypeName(elementType, daoMeta
                .getTypeParameterMap(), env));
        queryMeta.addMethodParameterName(entityListName, entityListTypeName);
        queryMeta.addExpressionParameterType(entityListName, elementType);
    }

}
