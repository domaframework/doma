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

import java.util.LinkedHashMap;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.BatchSqlValidator;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.SqlValidator;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.reflection.BatchModifyReflection;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileBatchModifyQueryMeta> {

    public SqlFileBatchModifyQueryMetaFactory(Context ctx) {
        super(ctx);
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
        doSqlFiles(queryMeta, method, daoMeta, false, queryMeta.isPopulatable());
        return queryMeta;
    }

    private SqlFileBatchModifyQueryMeta createSqlFileBatchModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SqlFileBatchModifyQueryMeta queryMeta = new SqlFileBatchModifyQueryMeta(
                method, daoMeta.getDaoElement());
        BatchModifyReflection batchModifyReflection = ctx.getReflections()
                .newBatchInsertReflection(method);
        if (batchModifyReflection != null && batchModifyReflection.getSqlFileValue()) {
            queryMeta.setBatchModifyReflection(batchModifyReflection);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_INSERT);
            return queryMeta;
        }
        batchModifyReflection = ctx.getReflections()
                .newBatchUpdateReflection(method);
        if (batchModifyReflection != null && batchModifyReflection.getSqlFileValue()) {
            queryMeta.setBatchModifyReflection(batchModifyReflection);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_UPDATE);
            return queryMeta;
        }
        batchModifyReflection = ctx.getReflections()
                .newBatchDeleteReflection(method);
        if (batchModifyReflection != null && batchModifyReflection.getSqlFileValue()) {
            queryMeta.setBatchModifyReflection(batchModifyReflection);
            queryMeta.setQueryKind(QueryKind.SQLFILE_BATCH_DELETE);
            return queryMeta;
        }
        return null;
    }

    @Override
    protected void doReturnType(SqlFileBatchModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(method);
        EntityCtType entityCtType = queryMeta.getEntityType();
        if (entityCtType != null && entityCtType.isImmutable()) {
            if (!returnMeta.isBatchResult(entityCtType)) {
                throw new AptException(Message.DOMA4223,
                        returnMeta.getMethodElement());
            }
        } else {
            if (!returnMeta.isPrimitiveIntArray()) {
                throw new AptException(Message.DOMA4040,
                        returnMeta.getMethodElement());
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
            throw new AptException(Message.DOMA4002, method);
        }
        QueryParameterMeta parameterMeta = createParameterMeta(
                parameters.get(0));
        IterableCtType iterableCtType = parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                    @Override
                    protected IterableCtType defaultAction(CtType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4042, method);
                    }

                    @Override
                    public IterableCtType visitIterableCtType(
                            IterableCtType ctType, Void p)
                            throws RuntimeException {
                        return ctType;
                    }

                }, null);
        CtType elementCtType = iterableCtType.getElementCtType();
        queryMeta.setElementCtType(elementCtType);
        queryMeta.setElementsParameterName(parameterMeta.getName());
        elementCtType.accept(
                new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                    @Override
                    public Void visitEntityCtType(EntityCtType ctType, Void p)
                            throws RuntimeException {
                        queryMeta.setEntityType(ctType);
                        return null;
                    }

                }, null);
        queryMeta.addParameterMeta(parameterMeta);
        if (parameterMeta.isBindable()) {
            queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                    parameterMeta.getCtType());
        }
    }

    @Override
    protected SqlValidator createSqlValidator(ExecutableElement method,
            LinkedHashMap<String, TypeMirror> parameterTypeMap,
            String sqlFilePath, boolean expandable, boolean populatable) {
        return new BatchSqlValidator(ctx, method, parameterTypeMap,
                sqlFilePath, expandable, populatable);
    }

}
