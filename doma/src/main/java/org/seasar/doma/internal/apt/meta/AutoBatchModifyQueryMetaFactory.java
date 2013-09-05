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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
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
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected AutoBatchModifyQueryMeta createAutoBatchModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        AutoBatchModifyQueryMeta queryMeta = new AutoBatchModifyQueryMeta(
                method);
        BatchModifyMirror batchModifyMirror = BatchInsertMirror.newInstance(
                method, env);
        if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_INSERT);
            return queryMeta;
        }
        batchModifyMirror = BatchUpdateMirror.newInstance(method, env);
        if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_UPDATE);
            return queryMeta;
        }
        batchModifyMirror = BatchDeleteMirror.newInstance(method, env);
        if (batchModifyMirror != null && !batchModifyMirror.getSqlFileValue()) {
            queryMeta.setBatchModifyMirror(batchModifyMirror);
            queryMeta.setQueryKind(QueryKind.AUTO_BATCH_DELETE);
            return queryMeta;
        }
        return null;
    }

    @Override
    protected void doReturnType(AutoBatchModifyQueryMeta queryMeta,
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
    protected void doParameters(AutoBatchModifyQueryMeta queryMeta,
            final ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> parameters = method.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new AptException(Message.DOMA4002, env, method);
        }
        final QueryParameterMeta parameterMeta = createParameterMeta(parameters
                .get(0));
        IterableType iterableType = parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<IterableType, Void, RuntimeException>() {

                    @Override
                    protected IterableType defaultAction(DataType dataType,
                            Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4042, env, method);
                    }

                    @Override
                    public IterableType visitIterableType(
                            IterableType dataType, Void p)
                            throws RuntimeException {
                        return dataType;
                    }

                }, null);
        EntityType entityType = iterableType
                .getElementType()
                .accept(new SimpleDataTypeVisitor<EntityType, Void, RuntimeException>() {

                    @Override
                    protected EntityType defaultAction(DataType dataType, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4043, env, method);
                    }

                    @Override
                    public EntityType visitEntityType(EntityType dataType,
                            Void p) throws RuntimeException {
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
        BatchModifyMirror batchModifyMirror = queryMeta.getBatchModifyMirror();
        validateEntityPropertyNames(entityType.getTypeMirror(), method,
                batchModifyMirror.getAnnotationMirror(),
                batchModifyMirror.getInclude(), batchModifyMirror.getExclude());
    }

}
