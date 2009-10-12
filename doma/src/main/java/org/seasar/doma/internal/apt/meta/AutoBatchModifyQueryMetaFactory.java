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
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.message.DomaMessageCode;

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
        AutoBatchModifyQueryMeta queryMeta = createAutoBatchModifyQueryMeta(
                method, daoMeta);
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
            queryMeta.setIncludedPropertyNames(insert.include());
            queryMeta.setExcludedPropertyNames(insert.exclude());
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_INSERT);
        }
        BatchUpdate update = method.getAnnotation(BatchUpdate.class);
        if (update != null && !update.sqlFile()) {
            queryMeta.setQueryTimeout(update.queryTimeout());
            queryMeta.setVersionIncluded(update.includeVersion());
            queryMeta.setIncludedPropertyNames(update.include());
            queryMeta.setExcludedPropertyNames(update.exclude());
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
        QueryReturnMeta returnMeta = createReturnMeta(method);
        if (!returnMeta.isPrimitiveIntArray()) {
            throw new AptException(DomaMessageCode.DOMA4040, env, returnMeta
                    .getElement());
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(AutoBatchModifyQueryMeta queryMeta,
            final ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> parameters = method.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new AptException(DomaMessageCode.DOMA4002, env, method);
        }
        final QueryParameterMeta parameterMeta = createParameterMeta(parameters
                .get(0));
        ListType listType = parameterMeta.getDataType().accept(
                new SimpleDataTypeVisitor<ListType, Void, RuntimeException>() {

                    @Override
                    protected ListType defaultAction(DataType dataType, Void p)
                            throws RuntimeException {
                        throw new AptException(DomaMessageCode.DOMA4042, env,
                                method);
                    }

                    @Override
                    public ListType visitListType(ListType dataType, Void p)
                            throws RuntimeException {
                        return dataType;
                    }

                }, null);
        EntityType entityType = listType
                .getElementType()
                .accept(
                        new SimpleDataTypeVisitor<EntityType, Void, RuntimeException>() {

                            @Override
                            protected EntityType defaultAction(
                                    DataType dataType, Void p)
                                    throws RuntimeException {
                                throw new AptException(
                                        DomaMessageCode.DOMA4043, env, method);
                            }

                            @Override
                            public EntityType visitEntityType(
                                    EntityType dataType, Void p)
                                    throws RuntimeException {
                                return dataType;
                            }

                        }, null);
        queryMeta.setEntityType(entityType);
        queryMeta.setEntitiesParameterName(parameterMeta.getName());
        queryMeta.addParameterMeta(parameterMeta);
        if (parameterMeta.isBindable()) {
            queryMeta.addBindableParameterType(parameterMeta.getName(),
                    entityType.getTypeMirror());
        }
        validateEntityPropertyNames(entityType.getTypeMirror(), method, queryMeta);
    }

}
