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

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.EntityType;
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
        QueryReturnMeta resultMeta = createReturnMeta(method);
        if (!resultMeta.isPrimitiveIntArray()) {
            throw new AptException(DomaMessageCode.DOMA4040, env, resultMeta
                    .getElement());
        }
        queryMeta.setReturnMeta(resultMeta);
    }

    @Override
    protected void doParameters(SqlFileBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> parameters = method.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new AptException(DomaMessageCode.DOMA4002, env, method);
        }
        QueryParameterMeta parameterMeta = createParameterMeta(parameters
                .get(0));
        ListType listType = parameterMeta.getCollectionType();
        if (listType == null) {
            throw new AptException(DomaMessageCode.DOMA4042, env, method);
        }
        EntityType entityType = listType.getEntityType();
        if (entityType == null) {
            throw new AptException(DomaMessageCode.DOMA4043, env, method);
        }
        queryMeta.setEntityType(entityType);
        queryMeta.setEntitiesParameterName(parameterMeta.getName());
        queryMeta.addParameterMetas(parameterMeta);

        queryMeta.addExpressionParameterType(parameterMeta.getName(),
                entityType.getType());
    }

}
