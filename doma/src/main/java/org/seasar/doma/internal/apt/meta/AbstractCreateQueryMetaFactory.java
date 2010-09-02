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
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMetaFactory<M extends AbstractCreateQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected final Class<?> returnClass;

    protected AbstractCreateQueryMetaFactory(ProcessingEnvironment env,
            Class<?> returnClass) {
        super(env);
        assertNotNull(returnClass);
        this.returnClass = returnClass;
    }

    @Override
    protected void doReturnType(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        QueryReturnMeta resultMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(resultMeta);
        if (!returnClass.getName().equals(
                resultMeta.getDataType().getQualifiedName())) {
            throw new AptException(Message.DOMA4097, env, method,
                    returnClass.getName());
        }
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        List<? extends VariableElement> params = method.getParameters();
        int size = params.size();
        if (size != 0) {
            throw new AptException(Message.DOMA4078, env, method);
        }
    }

}
