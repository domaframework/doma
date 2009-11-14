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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.mirror.DeleteMirror;
import org.seasar.doma.internal.apt.mirror.InsertMirror;
import org.seasar.doma.internal.apt.mirror.ModifyMirror;
import org.seasar.doma.internal.apt.mirror.UpdateMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.ListType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.message.DomaMessageCode;

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
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doSqlFiles(queryMeta, method, daoMeta);
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
        if (!returnMeta.isPrimitiveInt()) {
            throw new AptException(DomaMessageCode.DOMA4001, env, returnMeta
                    .getElement());
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(SqlFileModifyQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            parameterMeta.getDataType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        protected Void defaultAction(DataType type, Void p)
                                throws RuntimeException {
                            throw new AptException(DomaMessageCode.DOMA4008,
                                    env, parameterMeta.getElement(),
                                    parameterMeta.getType());
                        }

                        @Override
                        public Void visitBasicType(BasicType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }

                        @Override
                        public Void visitDomainType(DomainType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }

                        @Override
                        public Void visitEntityType(EntityType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }

                        @Override
                        public Void visitListType(ListType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }
                    }, null);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterType(parameterMeta.getName(),
                        parameterMeta.getType());
            }
        }
    }
}
