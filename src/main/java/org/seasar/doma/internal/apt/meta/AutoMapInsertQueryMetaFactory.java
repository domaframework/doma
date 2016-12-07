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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.mirror.MapInsertMirror;

import org.seasar.doma.message.Message;

/**
 * @author bakenezumi
 * 
 */
public class AutoMapInsertQueryMetaFactory extends AbstractQueryMetaFactory<AutoMapInsertQueryMeta> {

    public AutoMapInsertQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        AutoMapInsertQueryMeta queryMeta = createAutoMapInsertQueryMeta(method,
                daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected AutoMapInsertQueryMeta createAutoMapInsertQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        AutoMapInsertQueryMeta queryMeta = new AutoMapInsertQueryMeta(method,
                daoMeta.getDaoElement());
        MapInsertMirror mapInsertMirror = MapInsertMirror.newInstance(method, env);
        if (mapInsertMirror != null) {
            queryMeta.setMapInsertMirror(mapInsertMirror);
            queryMeta.setQueryKind(QueryKind.AUTO_MAPINSERT);
            return queryMeta;
        }
        return null;
    }

    @Override
    protected void doReturnType(AutoMapInsertQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta returnMeta = createReturnMeta(queryMeta);
        if (!returnMeta.isPrimitiveInt()) {
            throw new AptException(Message.DOMA4001, env,
                    returnMeta.getMethodElement(), new Object[] {
                            daoMeta.getDaoElement().getQualifiedName(),
                            method.getSimpleName() });
        }
        queryMeta.setReturnMeta(returnMeta);
    }

    @Override
    protected void doParameters(AutoMapInsertQueryMeta queryMeta,
            final ExecutableElement method, DaoMeta daoMeta) {
        List<? extends VariableElement> parameters = method.getParameters();
        int size = parameters.size();
        if (size != 1) {
            throw new AptException(Message.DOMA4002, env, method, new Object[] {
                    daoMeta.getDaoElement().getQualifiedName(),
                    method.getSimpleName() });
        }
        final QueryParameterMeta parameterMeta = createParameterMeta(
                parameters.get(0), queryMeta);
            MapCtType mapCtType = parameterMeta
                .getCtType()
                .accept(new SimpleCtTypeVisitor<MapCtType, Void, RuntimeException>() {

                    @Override
                    protected MapCtType defaultAction(CtType type, Void p)
                            throws RuntimeException {
                        throw new AptException(Message.DOMA4004, env,
                                parameterMeta.getElement(), new Object[] {
                                        daoMeta.getDaoElement()
                                                .getQualifiedName(),
                                        method.getSimpleName() });
                    }

                    @Override
                    public MapCtType visitAnyCtType(AnyCtType ctType,
                            Void p) throws RuntimeException {
                        final MapCtType mapCtType = MapCtType.newInstance(ctType.getTypeMirror(), env);
                        if (mapCtType != null) {
                            return mapCtType;
                        } else {
                            return defaultAction(ctType, p);
                        }
                    }

                }, null);
        queryMeta.addParameterMeta(parameterMeta);
        if (parameterMeta.isBindable()) {
            queryMeta.addBindableParameterCtType(parameterMeta.getName(),
                    parameterMeta.getCtType());
        }
        MapInsertMirror mapInsertMirror = queryMeta.getMapInsertMirror();
    }
}
