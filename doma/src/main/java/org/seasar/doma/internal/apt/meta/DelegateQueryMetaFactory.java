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

import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Delegate;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.mirror.DelegateMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

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
        DelegateMirror delegateMirror = DelegateMirror.newInstance(method, env);
        if (delegateMirror == null) {
            return null;
        }
        DelegateQueryMeta queryMeta = new DelegateQueryMeta(method);
        queryMeta.setDelegateMirror(delegateMirror);
        queryMeta.setQueryKind(QueryKind.DELEGATE);
        return queryMeta;
    }

    protected TypeMirror getTargetType(Delegate delegate) {
        try {
            delegate.to();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable.");
    }

    @Override
    protected void doParameters(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        for (VariableElement parameter : method.getParameters()) {
            QueryParameterMeta parameterMeta = createParameterMeta(parameter);
            queryMeta.addParameterMeta(parameterMeta);
            if (parameterMeta.isBindable()) {
                queryMeta.addBindableParameterType(parameterMeta.getName(),
                        parameterMeta.getType());
            }
        }
    }

    @Override
    protected void doReturnType(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        QueryReturnMeta resultMeta = createReturnMeta(method);
        queryMeta.setReturnMeta(resultMeta);
    }

    protected void doDelegate(DelegateQueryMeta queryMeta,
            ExecutableElement method, DaoMeta daoMeta) {
        TypeMirror delegateTypeMirror = queryMeta.getDelegateMirror()
                .getToValue();
        if (delegateTypeMirror == null) {
            throw new AptIllegalStateException(method.toString());
        }
        TypeElement delegateTypeElement = TypeMirrorUtil.toTypeElement(
                delegateTypeMirror, env);
        if (delegateTypeElement == null) {
            throw new AptIllegalStateException(method.toString());
        }
        ExecutableElement constructor = getSuitableConstructor(
                delegateTypeElement, daoMeta);
        if (constructor == null) {
            DelegateMirror delegateMirror = queryMeta.getDelegateMirror();
            throw new AptException(Message.DOMA4080, env, method,
                    delegateMirror.getAnnotationMirror(),
                    delegateMirror.getTo(),
                    delegateTypeElement.getQualifiedName());
        }
        if (constructor.getParameters().size() == 2) {
            queryMeta.setDaoAware(true);
        }
        if (!hasDelegatableMethod(method, delegateTypeElement)) {
            DelegateMirror delegateMirror = queryMeta.getDelegateMirror();
            throw new AptException(Message.DOMA4081, env, method,
                    delegateMirror.getAnnotationMirror(),
                    delegateMirror.getTo(),
                    delegateTypeElement.getQualifiedName());
        }
    }

    protected ExecutableElement getSuitableConstructor(
            TypeElement targetElement, DaoMeta daoMeta) {
        ExecutableElement candidate = null;
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(targetElement.getEnclosedElements())) {
            if (constructor.getModifiers().contains(Modifier.PUBLIC)) {
                List<? extends VariableElement> parameters = constructor
                        .getParameters();
                if (parameters.size() == 2) {
                    VariableElement first = parameters.get(0);
                    VariableElement second = parameters.get(1);
                    if (isConfig(first.asType())
                            && TypeMirrorUtil.isAssignable(second.asType(),
                                    daoMeta.getDaoType(), env)) {
                        candidate = constructor;
                        break;
                    }
                } else if (parameters.size() == 1) {
                    if (isConfig(parameters.get(0).asType())) {
                        candidate = constructor;
                    }
                }
            }
        }
        return candidate;
    }

    protected boolean hasDelegatableMethod(ExecutableElement srcMethod,
            TypeElement targetElement) {
        for (ExecutableElement destMethod : ElementFilter
                .methodsIn(targetElement.getEnclosedElements())) {
            if (destMethod.getModifiers().contains(Modifier.PUBLIC)) {
                if (isSameSignature(srcMethod, destMethod)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isSameSignature(ExecutableElement srcMethod,
            ExecutableElement destMethod) {
        if (!srcMethod.getSimpleName().equals(destMethod.getSimpleName())) {
            return false;
        }

        if (!TypeMirrorUtil.isSameType(srcMethod.getReturnType(),
                destMethod.getReturnType(), env)) {
            return false;
        }

        List<? extends TypeParameterElement> srcTypeParams = srcMethod
                .getTypeParameters();
        List<? extends TypeParameterElement> destTypeParams = destMethod
                .getTypeParameters();
        if (srcTypeParams.size() != destTypeParams.size()) {
            return false;
        }
        for (Iterator<? extends TypeParameterElement> srcIt = srcTypeParams
                .iterator(), destIt = destTypeParams.iterator(); srcIt
                .hasNext() && destIt.hasNext();) {
            if (!TypeMirrorUtil.isSameType(srcIt.next().asType(), destIt.next()
                    .asType(), env)) {
                return false;
            }
        }

        List<? extends VariableElement> srcParams = srcMethod.getParameters();
        List<? extends VariableElement> destParams = destMethod.getParameters();
        if (srcParams.size() != destParams.size()) {
            return false;
        }
        for (Iterator<? extends VariableElement> srcIt = srcParams.iterator(), destIt = destParams
                .iterator(); srcIt.hasNext() && destIt.hasNext();) {
            if (!TypeMirrorUtil.isSameType(srcIt.next().asType(), destIt.next()
                    .asType(), env)) {
                return false;
            }
        }

        List<? extends TypeMirror> srcThrownTypes = srcMethod.getThrownTypes();
        List<? extends TypeMirror> destThrownTypes = destMethod
                .getThrownTypes();
        if (srcThrownTypes.size() != destThrownTypes.size()) {
            return false;
        }
        for (Iterator<? extends TypeMirror> srcIt = srcThrownTypes.iterator(), destIt = destThrownTypes
                .iterator(); srcIt.hasNext() && destIt.hasNext();) {
            if (!TypeMirrorUtil.isSameType(srcIt.next(), destIt.next(), env)) {
                return false;
            }
        }
        return true;
    }
}
