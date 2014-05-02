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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.mirror.DeleteMirror;
import org.seasar.doma.internal.apt.mirror.InsertMirror;
import org.seasar.doma.internal.apt.mirror.ModifyMirror;
import org.seasar.doma.internal.apt.mirror.UpdateMirror;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileModifyQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileModifyQueryMeta> {

    public SqlFileModifyQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileModifyQueryMeta queryMeta = createSqlFileModifyQueryMeta(method,
                daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFiles(queryMeta, method, daoMeta, false);
        return queryMeta;
    }

    protected SqlFileModifyQueryMeta createSqlFileModifyQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SqlFileModifyQueryMeta queryMeta = new SqlFileModifyQueryMeta(method);
        ModifyMirror modifyMirror = InsertMirror.newInstance(method, env);
        if (modifyMirror != null && modifyMirror.getSqlFileValue()) {
            queryMeta.setModifyMirror(modifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_INSERT);
            return queryMeta;
        }
        modifyMirror = UpdateMirror.newInstance(method, env);
        if (modifyMirror != null && modifyMirror.getSqlFileValue()) {
            queryMeta.setModifyMirror(modifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_UPDATE);
            return queryMeta;
        }
        modifyMirror = DeleteMirror.newInstance(method, env);
        if (modifyMirror != null && modifyMirror.getSqlFileValue()) {
            queryMeta.setModifyMirror(modifyMirror);
            queryMeta.setQueryKind(QueryKind.SQLFILE_DELETE);
            return queryMeta;
        }
        return null;
    }

    @Override
    protected void doReturnType(SqlFileModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(method);
        EntityCtType entityCtType = queryMeta.getEntityCtType();
        if (entityCtType != null && entityCtType.isImmutable()) {
            if (!returnMeta.isResult(entityCtType)) {
                throw new AptException(Message.DOMA4222, env,
                        returnMeta.getElement());
            }
        } else {
            if (!returnMeta.isPrimitiveInt()) {
                throw new AptException(Message.DOMA4001, env,
                        returnMeta.getElement());
            }
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(final SqlFileModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
            }
            if (queryMeta.getEntityCtType() != null) {
                continue;
            }
            parameterMeta.getCtType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitEntityCtType(EntityCtType ctType,
                                Void p) throws RuntimeException {
                            queryMeta.setEntityCtType(ctType);
                            queryMeta.setEntityParameterName(parameterMeta
                                    .getName());
                            return null;
                        }
                    }, null);
        }
    }
}
