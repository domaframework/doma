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
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.mirror.ResultSetMirror;
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
                queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                        parameterMeta.getCtType());
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
        IterableCtType iterableCtType = parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                    @Override
                    protected IterableCtType defaultAction(CtType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4062, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public IterableCtType visitIterableCtType(
                            IterableCtType ctType, Void p)
                            throws RuntimeException {
                        if (!ctType.isList()) {
                            defaultAction(ctType, p);
                        }
                        return ctType;
                    }

                }, null);
        return iterableCtType
                .getElementType()
                .accept(new SimpleCtTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            CtType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4186, env,
                                parameterMeta.getElement(), type.getTypeName());
                    }

                    @Override
                    public CallableSqlParameterMeta visitEntityCtType(
                            EntityCtType ctType, Void p)
                            throws RuntimeException {
                        if (ctType.isAbstract()) {
                            throw new AptException(Message.DOMA4157, env,
                                    parameterMeta.getElement(), ctType
                                            .getTypeName());
                        }
                        return new EntityListParameterMeta(parameterMeta
                                .getName(), ctType, resultSetMirror
                                .getEnsureResultMappingValue());
                    }

                    @Override
                    public CallableSqlParameterMeta visitMapCtType(
                            MapCtType ctType, Void p) throws RuntimeException {
                        return new MapListParameterMeta(
                                parameterMeta.getName(), ctType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicCtType(
                            BasicCtType ctType, Void p) throws RuntimeException {
                        return new BasicListParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainCtType(
                            DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return new DomainListParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createInParameterMeta(
            final QueryParameterMeta parameterMeta) {
        return parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            CtType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4101, env,
                                parameterMeta.getElement(), parameterMeta
                                        .getType());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicCtType(
                            BasicCtType ctType, Void p) throws RuntimeException {
                        return new BasicInParameterMeta(
                                parameterMeta.getName(), ctType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainCtType(
                            DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return new DomainInParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceCtType referenceCtType = parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException>() {

                    @Override
                    protected ReferenceCtType defaultAction(CtType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4098, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public ReferenceCtType visitReferenceCtType(
                            ReferenceCtType ctType, Void p)
                            throws RuntimeException {
                        return ctType;
                    }

                }, null);
        return referenceCtType
                .getReferentType()
                .accept(new SimpleCtTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            CtType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4100, env,
                                parameterMeta.getElement(), referenceCtType
                                        .getReferentTypeMirror());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicCtType(
                            BasicCtType ctType, Void p) throws RuntimeException {
                        return new BasicOutParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainCtType(
                            DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return new DomainOutParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                }, null);
    }

    protected CallableSqlParameterMeta createInOutParameterMeta(
            final QueryParameterMeta parameterMeta) {
        final ReferenceCtType referenceCtType = parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<ReferenceCtType, Void, RuntimeException>() {

                    @Override
                    protected ReferenceCtType defaultAction(CtType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4111, env,
                                parameterMeta.getElement());
                    }

                    @Override
                    public ReferenceCtType visitReferenceCtType(
                            ReferenceCtType ctType, Void p)
                            throws RuntimeException {
                        return ctType;
                    }

                }, null);
        return referenceCtType
                .getReferentType()
                .accept(new SimpleCtTypeVisitor<CallableSqlParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected CallableSqlParameterMeta defaultAction(
                            CtType type, Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4100, env,
                                parameterMeta.getElement(), referenceCtType
                                        .getReferentTypeMirror());
                    }

                    @Override
                    public CallableSqlParameterMeta visitBasicCtType(
                            BasicCtType ctType, Void p) throws RuntimeException {
                        return new BasicInOutParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                    @Override
                    public CallableSqlParameterMeta visitDomainCtType(
                            DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return new DomainInOutParameterMeta(parameterMeta
                                .getName(), ctType);
                    }

                }, null);
    }

}
