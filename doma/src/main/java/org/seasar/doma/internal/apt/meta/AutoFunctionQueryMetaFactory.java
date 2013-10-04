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

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.mirror.FunctionMirror;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMetaFactory extends
        AutoModuleQueryMetaFactory<AutoFunctionQueryMeta> {

    public AutoFunctionQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        FunctionMirror functionMirror = FunctionMirror.newInstance(method, env);
        if (functionMirror == null) {
            return null;
        }
        AutoFunctionQueryMeta queryMeta = new AutoFunctionQueryMeta(method);
        queryMeta.setFunctionMirror(functionMirror);
        queryMeta.setQueryKind(QueryKind.AUTO_FUNCTION);
        doTypeParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    @Override
    protected void doReturnType(AutoFunctionQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(returnMeta);
        ResultParameterMeta resultParameterMeta = createCallableSqlResultParameterMeta(
                queryMeta, returnMeta);
        queryMeta.setResultParameterMeta(resultParameterMeta);
    }

    protected ResultParameterMeta createCallableSqlResultParameterMeta(
            final AutoFunctionQueryMeta queryMeta,
            final QueryReturnMeta returnMeta) {
        return returnMeta
                .getDataType()
                .accept(new SimpleCtTypeVisitor<ResultParameterMeta, Void, RuntimeException>() {

                    @Override
                    protected ResultParameterMeta defaultAction(CtType type,
                            Void p) throws RuntimeException {
                        throw new AptException(Message.DOMA4063, env,
                                returnMeta.getElement(), returnMeta.getType());
                    }

                    @Override
                    public ResultParameterMeta visitBasicCtType(
                            BasicCtType ctType, Void p) throws RuntimeException {
                        return new BasicResultParameterMeta(ctType);
                    }

                    @Override
                    public ResultParameterMeta visitDomainCtType(
                            DomainCtType ctType, Void p)
                            throws RuntimeException {
                        return new DomainResultParameterMeta(ctType);
                    }

                    @Override
                    public ResultParameterMeta visitIterableCtType(
                            IterableCtType ctType, Void p)
                            throws RuntimeException {
                        if (!ctType.isList()) {
                            defaultAction(ctType, p);
                        }
                        return ctType
                                .getElementType()
                                .accept(new SimpleCtTypeVisitor<ResultParameterMeta, Void, RuntimeException>() {

                                    @Override
                                    protected ResultParameterMeta defaultAction(
                                            CtType ctType, Void p)
                                            throws RuntimeException {
                                        throw new AptException(
                                                Message.DOMA4065, env,
                                                returnMeta.getElement(),
                                                ctType.getTypeName());
                                    }

                                    @Override
                                    public ResultParameterMeta visitBasicCtType(
                                            BasicCtType ctType, Void p)
                                            throws RuntimeException {
                                        return new BasicListResultParameterMeta(
                                                ctType);
                                    }

                                    @Override
                                    public ResultParameterMeta visitDomainCtType(
                                            DomainCtType ctType, Void p)
                                            throws RuntimeException {
                                        return new DomainListResultParameterMeta(
                                                ctType);
                                    }

                                    @Override
                                    public ResultParameterMeta visitEntityCtType(
                                            EntityCtType ctType, Void p)
                                            throws RuntimeException {
                                        if (ctType.isAbstract()) {
                                            throw new AptException(
                                                    Message.DOMA4156, env,
                                                    returnMeta.getElement(),
                                                    ctType.getTypeName());
                                        }
                                        return new EntityListResultParameterMeta(
                                                ctType,
                                                queryMeta
                                                        .getEnsureResultMapping());
                                    }

                                    @Override
                                    public ResultParameterMeta visitMapCtType(
                                            MapCtType ctType, Void p)
                                            throws RuntimeException {
                                        return new MapListResultParameterMeta(
                                                ctType);
                                    }

                                }, p);
                    }

                }, null);
    }
}
