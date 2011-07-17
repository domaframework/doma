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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.mirror.SelectMirror;
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.IterationCallbackType;
import org.seasar.doma.internal.apt.type.MapType;
import org.seasar.doma.internal.apt.type.SelectOptionsType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMetaFactory extends
        AbstractSqlFileQueryMetaFactory<SqlFileSelectQueryMeta> {

    public SqlFileSelectQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        SqlFileSelectQueryMeta queryMeta = createSqlFileSelectQueryMeta(method,
                daoMeta);
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

    protected SqlFileSelectQueryMeta createSqlFileSelectQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        SelectMirror selectMirror = SelectMirror.newInstance(method, env);
        if (selectMirror == null) {
            return null;
        }
        SqlFileSelectQueryMeta queryMeta = new SqlFileSelectQueryMeta(method);
        queryMeta.setSelectMirror(selectMirror);
        queryMeta.setQueryKind(QueryKind.SQLFILE_SELECT);
        return queryMeta;
    }

    @Override
    protected void doReturnType(final SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        final QueryReturnMeta returnMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(returnMeta);
        if (queryMeta.getIterate()) {
            IterationCallbackType iterationCallbackType = queryMeta
                    .getIterationCallbackType();
            AnyType callbackReturnType = iterationCallbackType.getReturnType();
            if (callbackReturnType == null
                    || !env.getTypeUtils().isSameType(returnMeta.getType(),
                            callbackReturnType.getTypeMirror())) {
                throw new AptException(Message.DOMA4055, env, method,
                        returnMeta.getType(),
                        callbackReturnType.getTypeNameAsTypeParameter());
            }
            DataType callbackTargetType = iterationCallbackType.getTargetType();
            callbackTargetType.accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        protected Void defaultAction(DataType type, Void p)
                                throws RuntimeException {
                            throw new AptException(Message.DOMA4058, env,
                                    queryMeta.getExecutableElement());
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
                        public Void visitMapType(MapType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }

                        @Override
                        public Void visitEntityType(EntityType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }
                    }, null);
        } else {
            returnMeta.getDataType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        protected Void defaultAction(DataType type, Void p)
                                throws RuntimeException {
                            throw new AptException(Message.DOMA4008, env,
                                    returnMeta.getElement(),
                                    returnMeta.getType());
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
                            if (dataType.isAbstract()) {
                                throw new AptException(Message.DOMA4154, env,
                                        returnMeta.getElement(),
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                        @Override
                        public Void visitMapType(MapType dataType, Void p)
                                throws RuntimeException {
                            return null;
                        }

                        @Override
                        public Void visitIterableType(IterableType dataType,
                                Void p) throws RuntimeException {
                            if (!dataType.isList()) {
                                defaultAction(dataType, p);
                            }
                            dataType.getElementType()
                                    .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                        @Override
                                        protected Void defaultAction(
                                                DataType type, Void p)
                                                throws RuntimeException {
                                            throw new AptException(
                                                    Message.DOMA4007, env,
                                                    returnMeta.getElement(),
                                                    type.getTypeName());
                                        }

                                        @Override
                                        public Void visitBasicType(
                                                BasicType dataType, Void p)
                                                throws RuntimeException {
                                            return null;
                                        }

                                        @Override
                                        public Void visitDomainType(
                                                DomainType dataType, Void p)
                                                throws RuntimeException {
                                            return null;
                                        }

                                        @Override
                                        public Void visitMapType(
                                                MapType dataType, Void p)
                                                throws RuntimeException {
                                            return null;
                                        }

                                        @Override
                                        public Void visitEntityType(
                                                EntityType dataType, Void p)
                                                throws RuntimeException {
                                            if (dataType.isAbstract()) {
                                                throw new AptException(
                                                        Message.DOMA4155,
                                                        env,
                                                        returnMeta.getElement(),
                                                        dataType.getTypeMirror());
                                            }
                                            return null;
                                        }

                                    }, p);

                            return null;
                        }

                    }, null);
        }
    }

    @Override
    protected void doParameters(final SqlFileSelectQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            final QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            parameterMeta.getDataType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitIterationCallbackType(
                                IterationCallbackType dataType, Void p)
                                throws RuntimeException {
                            if (queryMeta.getIterationCallbackType() != null) {
                                throw new AptException(Message.DOMA4054, env,
                                        parameterMeta.getElement());
                            }
                            dataType.getTargetType()
                                    .accept(new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                                        @Override
                                        public Void visitEntityType(
                                                EntityType dataType, Void p)
                                                throws RuntimeException {
                                            if (dataType.isAbstract()) {
                                                throw new AptException(
                                                        Message.DOMA4158, env,
                                                        parameterMeta
                                                                .getElement(),
                                                        dataType.getTypeName());
                                            }
                                            return null;
                                        }

                                    }, null);
                            queryMeta.setIterationCallbackType(dataType);
                            queryMeta.setIterationCallbackPrameterName(parameterMeta
                                    .getName());
                            return null;
                        }

                        @Override
                        public Void visitSelectOptionsType(
                                SelectOptionsType dataType, Void p)
                                throws RuntimeException {
                            if (queryMeta.getSelectOptionsType() != null) {
                                throw new AptException(Message.DOMA4053, env,
                                        parameterMeta.getElement());
                            }
                            queryMeta.setSelectOptionsType(dataType);
                            queryMeta.setSelectOptionsParameterName(parameterMeta
                                    .getName());
                            return null;
                        }

                    }, null);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterType(parameterMeta.getName(),
                        parameterMeta.getType());
            }
        }
        if (queryMeta.getIterate()) {
            if (queryMeta.getIterationCallbackType() == null) {
                throw new AptException(Message.DOMA4056, env, method);
            }
        } else {
            if (queryMeta.getIterationCallbackType() != null) {
                SelectMirror selectMirror = queryMeta.getSelectMirror();
                throw new AptException(Message.DOMA4057, env, method,
                        selectMirror.getAnnotationMirror(),
                        selectMirror.getIterate());
            }
        }
    }
}
