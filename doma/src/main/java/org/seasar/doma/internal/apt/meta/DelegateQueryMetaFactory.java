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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Delegate;
import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;

/**
 * @author taedium
 * 
 */
public class DelegateQueryMetaFactory extends
        AbstractQueryMetaFactory<DelegateQueryMeta> {

    public DelegateQueryMetaFactory(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public QueryMeta createQueryMeta(ExecutableElement method, DaoMeta daoMeta) {
        assertNotNull(method, daoMeta);
        DelegateQueryMeta queryMeta = createDelegateQueryMeta(method, daoMeta);
        if (queryMeta == null) {
            return null;
        }
        doTypeParameters(queryMeta, method, daoMeta);
        doParameters(queryMeta, method, daoMeta);
        doReturnType(queryMeta, method, daoMeta);
        doThrowTypes(queryMeta, method, daoMeta);
        doDelegate(queryMeta, method, daoMeta);
        return queryMeta;
    }

    protected DelegateQueryMeta createDelegateQueryMeta(
            ExecutableElement method, DaoMeta daoMeta) {
        Delegate delegate = method.getAnnotation(Delegate.class);
        if (delegate == null) {
            return null;
        }
        DelegateQueryMeta queryMeta = new DelegateQueryMeta();
        queryMeta.setQueryKind(QueryKind.DELEGATE);
        queryMeta.setTargetType(getTargetType(delegate));
        queryMeta.setName(method.getSimpleName().toString());
        queryMeta.setExecutableElement(method);
        return queryMeta;
    }

    protected TypeMirror getTargetType(Delegate delegate) {
        try {
            delegate.to();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    @Override
    protected void doParameters(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement param : method.getParameters()) {
            TypeMirror paramType = TypeUtil.resolveTypeParameter(daoMeta
                    .getTypeParameterMap(), param.asType());
            String parameterName = ElementUtil.getParameterName(param);
            String parameterTypeName = TypeUtil.getTypeName(paramType, daoMeta
                    .getTypeParameterMap(), env);
            queryMeta.addMethodParameter(parameterName, parameterTypeName);
        }
    }

    @Override
    protected void doReturnType(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror returnType = TypeUtil.resolveTypeParameter(daoMeta
                .getTypeParameterMap(), method.getReturnType());
        queryMeta.setReturnTypeName(TypeUtil.getTypeName(returnType, daoMeta
                .getTypeParameterMap(), env));
    }

    protected void doDelegate(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeElement delegateTypeElement = TypeUtil.toTypeElement(queryMeta
                .getTargetType(), env);
        if (delegateTypeElement == null) {
            throw new AptIllegalStateException();
        }
        if (!hasSuitableConstructor(delegateTypeElement)) {
            throw new AptException(DomaMessageCode.DOMA4080, env, method,
                    delegateTypeElement.getQualifiedName());
        }
        if (!hasDelegatableMethod(method, delegateTypeElement)) {
            throw new AptException(DomaMessageCode.DOMA4081, env, method,
                    delegateTypeElement.getQualifiedName());
        }
    }

    protected boolean hasSuitableConstructor(TypeElement targetElement) {
        for (ExecutableElement e : ElementFilter.constructorsIn(targetElement
                .getEnclosedElements())) {
            if (e.getModifiers().contains(Modifier.PUBLIC)) {
                if (e.getParameters().size() == 1) {
                    VariableElement variableElement = e.getParameters().get(0);
                    if (isConfig(variableElement.asType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean hasDelegatableMethod(ExecutableElement method,
            TypeElement targetElement) {
        ExecutableType srcMethod = ElementUtil.toExecutableType(method, env);
        for (ExecutableElement e : ElementFilter.methodsIn(targetElement
                .getEnclosedElements())) {
            if (e.getModifiers().contains(Modifier.PUBLIC)) {
                ExecutableType destMethod = ElementUtil
                        .toExecutableType(e, env);
                if (env.getTypeUtils().isSubsignature(destMethod, srcMethod)) {
                    return true;
                }
            }
        }
        return false;
    }
}
