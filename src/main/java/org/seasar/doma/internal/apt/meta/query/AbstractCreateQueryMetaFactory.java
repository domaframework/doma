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
package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMetaFactory<M extends AbstractCreateQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected final Class<?> returnClass;

    protected AbstractCreateQueryMetaFactory(Context ctx, ExecutableElement methodElement,
            Class<?> returnClass) {
        super(ctx, methodElement);
        assertNotNull(returnClass);
        this.returnClass = returnClass;
    }

    @Override
    protected void doReturnType(M queryMeta) {
        QueryReturnMeta resultMeta = createReturnMeta();
        queryMeta.setReturnMeta(resultMeta);
        if (!returnClass.getName().equals(resultMeta.getCtType().getQualifiedName())) {
            throw new AptException(Message.DOMA4097, methodElement,
                    new Object[] { returnClass.getName() });
        }
    }

    @Override
    protected void doParameters(M queryMeta) {
        List<? extends VariableElement> params = methodElement.getParameters();
        int size = params.size();
        if (size != 0) {
            throw new AptException(Message.DOMA4078, methodElement);
        }
    }

}
