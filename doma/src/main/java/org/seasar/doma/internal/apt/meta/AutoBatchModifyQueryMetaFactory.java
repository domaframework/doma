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

import static org.seasar.doma.internal.util.Assertions.*;

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
import org.seasar.doma.internal.apt.Models;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class AutoBatchModifyQueryMetaFactory extends
        AbstractQueryMetaFactory<AutoBatchModifyQueryMeta> {

    public AutoBatchModifyQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoBatchModifyQueryMeta queryMeta = createAutoBatchModifyQueryMeta(method, daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected AutoBatchModifyQueryMeta createAutoBatchModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        AutoBatchModifyQueryMeta queryMeta = new AutoBatchModifyQueryMeta();
        BatchInsert insert = method.getAnnotation(BatchInsert.class);
        if (insert != null && !insert.sqlFile()) {
            queryMeta.setQueryTimeout(insert.queryTimeout());
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_INSERT);
        }
        BatchUpdate update = method.getAnnotation(BatchUpdate.class);
        if (update != null && !update.sqlFile()) {
            queryMeta.setQueryTimeout(update.queryTimeout());
            queryMeta.setVersionIncluded(update.includesVersion());
            queryMeta.setOptimisticLockExceptionSuppressed(update
                    .suppressOptimisticLockException());
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_UPDATE);
        }
        BatchDelete delete = method.getAnnotation(BatchDelete.class);
        if (delete != null && !delete.sqlFile()) {
            queryMeta.setQueryTimeout(delete.queryTimeout());
            queryMeta.setVersionIgnored(delete.ignoreVersion());
            queryMeta.setOptimisticLockExceptionSuppressed(delete
                    .suppressOptimisticLockException());
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_DELETE);
        }
        if (queryMeta.getQueryKind() == null) {
            return null;
        }
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    @Override
    protected void doReturnType(AutoBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = method.getReturnType();
        if (!isPrimitiveIntArray(returnType)) {
            throw new AptException(MessageCode.DOMA4040, env, method);
        }
        queryMeta.setReturnTypeName(Models.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env));
    }

    @Override
    protected void doParameters(AutoBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> params = method.getParameters();
        int size = params.size();
        if (size != 1) {
            throw new AptException(MessageCode.DOMA4002, env, method);
        }
        VariableElement entityList = params.get(0);
        TypeMirror entityListType = Models.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), entityList.asType());
        if (!isList(entityListType)) {
            throw new AptException(MessageCode.DOMA4042, env, method);
        }
        DeclaredType listTyep = Models.toDeclaredType(entityListType, env);
        List<? extends TypeMirror> args = listTyep.getTypeArguments();
        if (args.isEmpty()) {
            throw new AptException(MessageCode.DOMA4041, env, method);
        }
        TypeMirror elementType = Models.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), args.get(0));
        if (!isEntity(elementType, daoMeta)) {
            throw new AptException(MessageCode.DOMA4043, env, method);
        }
        String entityListName = Models.getParameterName(entityList);
        String entityListTypeName = Models.getTypeName(entityListType, daoMeta
                .getTypeParameterMap(), env);
        queryMeta.setEntityListName(entityListName);
        queryMeta.setEntityListTypeName(entityListTypeName);
        queryMeta.setElementTypeName(Models.getTypeName(elementType, daoMeta
                .getTypeParameterMap(), env));
        queryMeta.addMethodParameter(entityListName, entityListTypeName);
    }

}
