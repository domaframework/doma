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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.internal.message.DomaMessageCode;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMetaFactory<M extends AbstractCreateQueryMeta>
        extends AbstractQueryMetaFactory<M> {

    protected final Class<?> returnClass;

    public AbstractCreateQueryMetaFactory(ProcessingEnvironment env,
            Class<?> domainValueClass) {
        super(env);
        assertNotNull(domainValueClass);
        this.returnClass = domainValueClass;
    }

    @Override
    protected void doReturnType(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        QueryReturnMeta resultMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(resultMeta);
        if (!resultMeta.getTypeName().equals(returnClass.getName())) {
            throw new AptException(DomaMessageCode.DOMA4097, env, method,
                    returnClass.getName());
        }

    }

    protected TypeMirror getDomainValueType(TypeMirror domainType) {
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(
                domainType)) {
            TypeElement typeElement = TypeUtil.toTypeElement(supertype, env);
            if (typeElement == null) {
                continue;
            }
            if (typeElement.getQualifiedName().contentEquals(
                    Wrapper.class.getName())) {
                DeclaredType declaredType = TypeUtil.toDeclaredType(supertype,
                        env);
                if (declaredType == null) {
                    continue;
                }
                List<? extends TypeMirror> args = declaredType
                        .getTypeArguments();
                return args.get(0);
            }
            TypeMirror domainValueType = getDomainValueType(supertype);
            if (domainValueType != null) {
                return domainValueType;
            }
        }
        return null;
    }

    @Override
    protected void doParameters(M queryMeta, ExecutableElement method,
            DaoMeta daoMeta) {
        List<? extends VariableElement> params = method.getParameters();
        int size = params.size();
        if (size != 0) {
            throw new AptException(DomaMessageCode.DOMA4078, env, method);
        }
    }

}
