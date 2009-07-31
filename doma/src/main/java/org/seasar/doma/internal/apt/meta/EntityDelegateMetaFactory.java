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
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Delegate;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class EntityDelegateMetaFactory {

    protected final ProcessingEnvironment env;

    public EntityDelegateMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public EntityDelegateMeta createEntityDelegateMeta(
            ExecutableElement method, EntityMeta entityMeta) {
        assertNotNull(method, entityMeta);
        Delegate delegate = method.getAnnotation(Delegate.class);
        if (delegate == null) {
            return null;
        }
        EntityDelegateMeta delegateMeta = new EntityDelegateMeta();
        delegateMeta.setExecutableElement(method);
        delegateMeta.setName(method.getSimpleName().toString());
        delegateMeta.setTargetType(getTargetType(delegate));
        doTypeParameters(delegateMeta, method, entityMeta);
        doReturnType(delegateMeta, method, entityMeta);
        doParameters(delegateMeta, method, entityMeta);
        doThrowTypes(delegateMeta, method, entityMeta);
        doDelegate(delegateMeta, method, entityMeta);
        return delegateMeta;
    }

    protected TypeMirror getTargetType(Delegate delegate) {
        try {
            delegate.to();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException();
    }

    protected void doTypeParameters(EntityDelegateMeta delegateMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        for (TypeParameterElement element : method.getTypeParameters()) {
            String name = TypeUtil.getTypeName(element.asType(), entityMeta
                    .getTypeParameterMap(), env);
            delegateMeta.addTypeParameterName(name);
        }
    }

    protected void doReturnType(EntityDelegateMeta delegateMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        TypeMirror returnType = TypeUtil.resolveTypeParameter(entityMeta
                .getTypeParameterMap(), method.getReturnType());
        delegateMeta
                .setReturnTypeName(TypeUtil.getTypeName(returnType, entityMeta
                        .getTypeParameterMap(), env));
    }

    protected void doParameters(EntityDelegateMeta delegateMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        for (VariableElement param : method.getParameters()) {
            TypeMirror paramType = TypeUtil.resolveTypeParameter(entityMeta
                    .getTypeParameterMap(), param.asType());
            String parameterName = ElementUtil.getParameterName(param);
            String parameterTypeName = TypeUtil
                    .getTypeName(paramType, entityMeta.getTypeParameterMap(), env);
            delegateMeta.addMethodParameter(parameterName, parameterTypeName);
        }
    }

    protected void doThrowTypes(EntityDelegateMeta delegateMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        for (TypeMirror thrownType : method.getThrownTypes()) {
            String typeName = TypeUtil.getTypeName(thrownType, entityMeta
                    .getTypeParameterMap(), env);
            delegateMeta.addThrownTypeName(typeName);
        }
    }

    protected void doDelegate(EntityDelegateMeta delegateMeta,
            ExecutableElement method, EntityMeta entityMeta) {
        TypeElement delegateTypeElement = TypeUtil.toTypeElement(delegateMeta
                .getTargetType(), env);
        if (delegateTypeElement == null) {
            throw new AptIllegalStateException();
        }
        if (!hasSuitableConstructor(delegateTypeElement, entityMeta)) {
            throw new AptException(DomaMessageCode.DOMA4082, env, method,
                    delegateTypeElement.getQualifiedName(), entityMeta
                            .getEntityElement().getQualifiedName());
        }
        if (!hasDelegatableMethod(method, delegateTypeElement)) {
            throw new AptException(DomaMessageCode.DOMA4083, env, method,
                    delegateTypeElement.getQualifiedName());
        }
    }

    protected boolean hasSuitableConstructor(TypeElement targetElement,
            EntityMeta entityMeta) {
        for (ExecutableElement e : ElementFilter.constructorsIn(targetElement
                .getEnclosedElements())) {
            if (e.getModifiers().contains(Modifier.PUBLIC)) {
                if (e.getParameters().size() == 1) {
                    VariableElement variableElement = e.getParameters().get(0);
                    if (TypeUtil
                            .isAssignable(entityMeta.getEntityType(), variableElement
                                    .asType(), env)) {
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
