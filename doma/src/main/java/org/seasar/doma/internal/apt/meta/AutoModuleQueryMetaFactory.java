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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.mirror.ResultSetMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.MapType;
import org.seasar.doma.internal.apt.type.ReferenceType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AutoModuleQueryMetaFactory<M extends AutoModuleQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected AutoModuleQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);

            CallableSqlParameterMeta callableSqlParameterMeta = createParameterMeta(parameterMeta);
            queryMeta.addCallableSqlParameterMeta(callableSqlParameterMeta);

            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterType(parameterMeta.getName(),
                        parameterMeta.getType());
            }
        }
    }

    protected CallableSqlParameterMeta createParameterMeta(
            final QueryParameterMeta parameterMeta) {
        ResultSetMirror resultSetMirror = ResultSetMirror.newInstance(
                parameterMeta.getElement(), env);
        if (resultSetMirror != null) {
            return createResultSetParameterMeta(parameterMeta, resultSetMirror);
        }
        if (parameterMeta.isAnnotated(In.class)) {
            return createInParameterMeta(parameterMeta);
        }
        if (parameterMeta.isAnnotated(Out.class)) {
            return createOutParameterMeta(parameterMeta);
        }
        if (parameterMeta.isAnnotated(InOut.class)) {
            return createInOutParameterMeta(parameterMeta);
        }
        throw new AptException(Message.DOMA4066, env,
                parameterMeta.getElement());
    }

    protected CallableSqlParameterMeta createResultSetParameterMeta(
            final QueryParameterMeta parameterMeta,
            final ResultSetMirror resultSetMirror) {
        IterableType iterableType = parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<IterableType, Void, RuntimeException>() {

                    @Override
                    protected IterableType defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4062, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public IterableType visitIterableType(
                            IterableType dataType, Void p)
                            throws RuntimeException {
                        if (!dataType.isList()) {
                            defaultAction(dataType, p);
                        }
                        return dataType;
                    }

                }, null);
        return iterableType
                .getElementType()
                .accept(new SimpleDataTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            DataType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4186, env,
                                parameterMeta.getElement(), type.getTypeName());
                    }

                    @Override
                    public CallableSqlParameterMeta visitEntityType(
                            EntityType dataType, Void p)
                            throws RuntimeException {
                        if (dataType.isAbstract()) {
                            throw new AptException(Message.DOMA4157, env,
                                    parameterMeta.getElement(), dataType
                                            .getTypeName());
                        }
                        return new EntityListParameterMeta(parameterMeta
                                .getName(), dataType, resultSetMirror
                                .getEnsureResultMappingValue());
                    }

                    @Override
                    public CallableSqlParameterMeta visitMapType(
                            MapType dataType, Void p) throws RuntimeException {
                        return new MapListParameterMeta(
                                parameterMeta.getName(), dataType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicType(
                            BasicType dataType, Void p) throws RuntimeException {
                        return new BasicListParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainType(
                            DomainType dataType, Void p)
                            throws RuntimeException {
                        return new DomainListParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createInParameterMeta(
            final QueryParameterMeta parameterMeta) {
        return parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            DataType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4101, env,
                                parameterMeta.getElement(), parameterMeta
                                        .getType());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicType(
                            BasicType dataType, Void p) throws RuntimeException {
                        return new BasicInParameterMeta(
                                parameterMeta.getName(), dataType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainType(
                            DomainType dataType, Void p)
                            throws RuntimeException {
                        return new DomainInParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceType referenceType = parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<ReferenceType, Void, RuntimeException>() {

                    @Override
                    protected ReferenceType defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4098, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public ReferenceType visitReferenceType(
                            ReferenceType dataType, Void p)
                            throws RuntimeException {
                        return dataType;
                    }

                }, null);
        return referenceType
                .getReferentType()
                .accept(new SimpleDataTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            DataType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4100, env,
                                parameterMeta.getElement(), referenceType
                                        .getReferentTypeMirror());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicType(
                            BasicType dataType, Void p) throws RuntimeException {
                        return new BasicOutParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainType(
                            DomainType dataType, Void p)
                            throws RuntimeException {
                        return new DomainOutParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createInOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceType referenceType = parameterMeta
                .getDataType()
                .accept(new SimpleDataTypeVisitor<ReferenceType, Void, RuntimeException>() {

                    @Override
                    protected ReferenceType defaultAction(DataType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4111, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public ReferenceType visitReferenceType(
                            ReferenceType dataType, Void p)
                            throws RuntimeException {
                        return dataType;
                    }

                }, null);
        return referenceType
                .getReferentType()
                .accept(new SimpleDataTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            DataType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4100, env,
                                parameterMeta.getElement(), referenceType
                                        .getReferentTypeMirror());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicType(
                            BasicType dataType, Void p) throws RuntimeException {
                        return new BasicInOutParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainType(
                            DomainType dataType, Void p)
                            throws RuntimeException {
                        return new DomainInOutParameterMeta(parameterMeta
                                .getName(), dataType);
                    }

                }, null);
    }

}
