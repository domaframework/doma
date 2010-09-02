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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.mirror.EnumDomainMirror;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.message.Message;

public class EnumDomainMetaFactory implements
        TypeElementMetaFactory<EnumDomainMeta> {

    private final ProcessingEnvironment env;

    public EnumDomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    @Override
    public EnumDomainMeta createTypeElementMeta(TypeElement classElement) {
        assertNotNull(classElement);
        EnumDomainMirror enumDomainMirror = EnumDomainMirror.newInstance(
                classElement, env);
        if (enumDomainMirror == null) {
            throw new AptIllegalStateException("enumDomainMirror");
        }
        EnumDomainMeta enumDomainMeta = new EnumDomainMeta(classElement,
                classElement.asType());
        enumDomainMeta.setEnumDomainMirror(enumDomainMirror);
        doWrapperType(classElement, enumDomainMeta);
        validateClass(classElement, enumDomainMeta);
        validateFactoryMethod(classElement, enumDomainMeta);
        validateAccessorMethod(classElement, enumDomainMeta);
        return enumDomainMeta;
    }

    protected void doWrapperType(TypeElement classElement,
            EnumDomainMeta enumDomainMeta) {
        BasicType basicType = BasicType.newInstance(
                enumDomainMeta.getValueType(), env);
        if (basicType == null) {
            EnumDomainMirror enumDomainMirror = enumDomainMeta
                    .getEnumDomainMirror();
            throw new AptException(Message.DOMA4102, env, classElement,
                    enumDomainMirror.getAnnotationMirror(),
                    enumDomainMirror.getValueType(),
                    enumDomainMirror.getValueTypeValue());
        }
        if (basicType.isEnum()) {
            EnumDomainMirror enumDomainMirror = EnumDomainMirror.newInstance(
                    classElement, env);
            if (enumDomainMirror != null) {
                throw new AptException(Message.DOMA4178, env, classElement,
                        enumDomainMirror.getAnnotationMirror(),
                        enumDomainMirror.getValueType(),
                        enumDomainMirror.getValueTypeValue());
            }
        }
        enumDomainMeta.setWrapperType(basicType.getWrapperType());
    }

    protected void validateClass(TypeElement classElement,
            EnumDomainMeta enumDomainMeta) {
        if (classElement.getKind() != ElementKind.ENUM) {
            EnumDomainMirror enumDomainMirror = enumDomainMeta
                    .getEnumDomainMirror();
            throw new AptException(Message.DOMA4174, env, classElement,
                    enumDomainMirror.getAnnotationMirror());
        }
        if (classElement.getNestingKind().isNested()) {
            throw new AptException(Message.DOMA4180, env, classElement);
        }
    }

    protected void validateFactoryMethod(TypeElement classElement,
            EnumDomainMeta enumDomainMeta) {
        for (ExecutableElement method : ElementFilter.methodsIn(classElement
                .getEnclosedElements())) {
            if (!method.getSimpleName().contentEquals(
                    enumDomainMeta.getFactoryMethod())) {
                continue;
            }
            if (method.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }
            if (!method.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            if (method.getParameters().size() != 1) {
                continue;
            }
            TypeMirror parameterType = method.getParameters().get(0).asType();
            if (!env.getTypeUtils().isAssignable(enumDomainMeta.getValueType(),
                    parameterType)) {
                continue;
            }
            TypeMirror returnType = env.getTypeUtils().erasure(
                    method.getReturnType());
            if (env.getTypeUtils().isAssignable(returnType,
                    enumDomainMeta.getType())) {
                return;
            }
        }
        throw new AptException(Message.DOMA4177, env, classElement,
                enumDomainMeta.getFactoryMethod(), classElement.asType(),
                enumDomainMeta.getValueType(),
                enumDomainMeta.getFactoryMethod());
    }

    protected void validateAccessorMethod(TypeElement classElement,
            EnumDomainMeta enumDomainMeta) {
        for (ExecutableElement method : ElementFilter.methodsIn(classElement
                .getEnclosedElements())) {
            if (!method.getSimpleName().contentEquals(
                    enumDomainMeta.getAccessorMethod())) {
                continue;
            }
            if (method.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }
            if (method.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            if (!method.getParameters().isEmpty()) {
                continue;
            }
            TypeMirror returnType = env.getTypeUtils().erasure(
                    method.getReturnType());
            if (env.getTypeUtils().isAssignable(returnType,
                    enumDomainMeta.getValueType())) {
                return;
            }
        }
        throw new AptException(Message.DOMA4176, env, classElement,
                enumDomainMeta.getAccessorMethod(),
                enumDomainMeta.getValueType());
    }
}
