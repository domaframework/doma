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

import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.BatchSqlValidator;
import org.seasar.doma.internal.apt.SqlValidator;
import org.seasar.doma.internal.apt.mirror.BatchDeleteMirror;
import org.seasar.doma.internal.apt.mirror.BatchInsertMirror;
import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.internal.apt.mirror.BatchUpdateMirror;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.message.Message;

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
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFiles(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected SqlFileBatchModifyQueryMeta createSqlFileBatchModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SqlFileBatchModifyQueryMeta queryMeta = new SqlFileBatchModifyQueryMeta(
                method);
        BatchModifyMirror batchModifyMirror = BatchInsertMirror.newInstance(
                method, env);
        if (batchModifyMirror != null && batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_INSERT);
            return queryMeta;
        }
        batchModifyMirror = BatchUpdateMirror.newInstance(method, env);
        if (batchModifyMirror != null && batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_UPDATE);
            return queryMeta;
        }
        batchModifyMirror = BatchDeleteMirror.newInstance(method, env);
        if (batchModifyMirror != null && batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_DELETE);
            return queryMeta;
        }
        return null;
    }

    @Override
    protected void doReturnType(SqlFileBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(method);
        EntityType entityType = queryMeta.getEntityType();
        if (entityType != null && entityType.isImmutable()) {
            if (!returnMeta.isBatchResult(entityType)) {
                throw new AptException(Message.DOMA4223, env,
                        returnMeta.getElement());
            }
        } else {
            if (!returnMeta.isPrimitiveIntArray()) {
                throw new AptException(Message.DOMA4040, env,
                        returnMeta.getElement());
            }
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(final SqlFileBatchModifyQueryMeta queryMeta,
            final ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> parameters = method.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new AptException(Message.DOMA4002, env, method);
        }
        QueryParameterMeta parameterMeta = createParameterMeta(parameters
                .get(0));
        IterableType iterableType = parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<IterableType, Void, RuntimeException>() {

                    @Override
                    protected IterableType defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4042, env, method);
                    }

                    @Override
                    public IterableType visitIterableType(
                            IterableType dataType, Void p)
                            throws RuntimeException {
                        return dataType;
                    }

                }, null);
        DataType elementType = iterableType.getElementType();
        queryMeta.setElementType(elementType);
        queryMeta.setElementsParameterName(parameterMeta.getName());
        elementType.accept(
                new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    public Void visitEntityType(EntityType dataType, Void p)
                            throws RuntimeException {
                        queryMeta.setEntityType(dataType);
                        return null;
                    }

                }, null);
        queryMeta.addParameterMeta(parameterMeta);
        if (parameterMeta.isBindable()) {
            queryMeta.addBindableParameterType(parameterMeta.getName(),
                    elementType.getTypeMirror());
        }
    }

    @Override
    protected SqlValidator createSqlValidator(ExecutableElement method,
            Map<String, TypeMirror> parameterTypeMap, String sqlFilePath) {
        return new BatchSqlValidator(env, method, parameterTypeMap, sqlFilePath);
    }

}
