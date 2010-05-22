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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.mirror.DomainMirror;
import org.seasar.doma.internal.apt.mirror.EnumDomainMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.internal.message.Message;

public class DomainMetaFactory {

    private final ProcessingEnvironment env;

    public DomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public DomainMeta createDomainMeta(TypeElement classElement) {
        assertNotNull(classElement);
        DomainMirror domainMirror = DomainMirror.newInstance(classElement, env);
        if (domainMirror == null) {
            throw new AptIllegalStateException("domainMirror");
        }
        DomainMeta domainMeta = new DomainMeta(classElement, classElement
                .asType());
        domainMeta.setDomainMirror(domainMirror);
        doWrapperType(classElement, domainMeta);
        validateClass(classElement, domainMeta);
        validateConstructor(classElement, domainMeta);
        validateAccessorMethod(classElement, domainMeta);
        return domainMeta;
    }

    protected void doWrapperType(TypeElement classElement, DomainMeta domainMeta) {
        BasicType basicType = BasicType.newInstance(domainMeta.getValueType(),
                env);
        if (basicType == null) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(Message.DOMA4102, env, classElement,
                    domainMirror.getAnnotationMirror(), domainMirror
                            .getValueType(), domainMirror.getValueTypeValue());
        }
        if (basicType.isEnum()) {
            EnumDomainMirror enumDomainMirror = EnumDomainMirror.newInstance(
                    classElement, env);
            if (enumDomainMirror != null) {
                throw new AptException(Message.DOMA4178, env, classElement,
                        enumDomainMirror.getAnnotationMirror(),
                        enumDomainMirror.getValueType(), enumDomainMirror
                                .getValueTypeValue());
            }
        }
        domainMeta.setWrapperType(basicType.getWrapperType());
    }

    protected void validateClass(TypeElement classElement, DomainMeta domainMeta) {
        if (!classElement.getKind().isClass()) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(Message.DOMA4105, env, classElement,
                    domainMirror.getAnnotationMirror());
        }
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new AptException(Message.DOMA4132, env, classElement);
        }
        if (classElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4106, env, classElement);
        }
        if (!classElement.getTypeParameters().isEmpty()) {
            throw new AptException(Message.DOMA4107, env, classElement);
        }
    }

    protected void validateConstructor(TypeElement classElement,
            DomainMeta domainMeta) {
        for (ExecutableElement constructor : ElementFilter
                .constructorsIn(classElement.getEnclosedElements())) {
            if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }
            List<? extends VariableElement> parameters = constructor
                    .getParameters();
            if (parameters.size() != 1) {
                continue;
            }
            TypeMirror parameterType = env.getTypeUtils().erasure(
                    parameters.get(0).asType());
            if (env.getTypeUtils().isSameType(parameterType,
                    domainMeta.getValueType())) {
                return;
            }
        }
        throw new AptException(Message.DOMA4103, env, classElement, domainMeta
                .getValueType());
    }

    protected void validateAccessorMethod(TypeElement classElement,
            DomainMeta domainMeta) {
        for (TypeElement t = classElement; t != null
                && t.asType().getKind() != TypeKind.NONE; t = TypeMirrorUtil
                .toTypeElement(t.getSuperclass(), env)) {
            for (ExecutableElement method : ElementFilter.methodsIn(t
                    .getEnclosedElements())) {
                if (!method.getSimpleName().contentEquals(
                        domainMeta.getAccessorMethod())) {
                    continue;
                }
                if (method.getModifiers().contains(Modifier.PRIVATE)) {
                    continue;
                }
                if (!method.getParameters().isEmpty()) {
                    continue;
                }
                TypeMirror returnType = env.getTypeUtils().erasure(
                        method.getReturnType());
                if (env.getTypeUtils().isAssignable(returnType,
                        domainMeta.getValueType())) {
                    return;
                }
            }
        }
        throw new AptException(Message.DOMA4104, env, classElement, domainMeta
                .getValueType(), domainMeta.getAccessorMethod());
    }
}
