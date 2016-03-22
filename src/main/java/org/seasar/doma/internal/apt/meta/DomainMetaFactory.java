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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.mirror.DomainMirror;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.message.Message;

public class DomainMetaFactory implements TypeElementMetaFactory<DomainMeta> {

    private final ProcessingEnvironment env;

    public DomainMetaFactory(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    @Override
    public DomainMeta createTypeElementMeta(TypeElement classElement) {
        assertNotNull(classElement);
        DomainMirror domainMirror = DomainMirror.newInstance(classElement, env);
        if (domainMirror == null) {
            throw new AptIllegalStateException("domainMirror");
        }
        DomainMeta domainMeta = new DomainMeta(classElement,
                classElement.asType());
        domainMeta.setDomainMirror(domainMirror);
        doWrapperCtType(classElement, domainMeta);
        validateAcceptNull(classElement, domainMeta);
        validateClass(classElement, domainMeta);
        if (domainMeta.providesConstructor()) {
            validateConstructor(classElement, domainMeta);
        } else {
            validateFactoryMethod(classElement, domainMeta);
        }
        validateAccessorMethod(classElement, domainMeta);
        return domainMeta;
    }

    protected void doWrapperCtType(TypeElement classElement,
            DomainMeta domainMeta) {
        BasicCtType basicCtType = BasicCtType.newInstance(
                domainMeta.getValueType(), env);
        if (basicCtType == null) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(Message.DOMA4102, env, classElement,
                    domainMirror.getAnnotationMirror(),
                    domainMirror.getValueType(),
                    domainMirror.getValueTypeValue());
        }
        domainMeta.setBasicCtType(basicCtType);
        domainMeta.setWrapperCtType(basicCtType.getWrapperCtType());
    }

    protected void validateClass(TypeElement classElement, DomainMeta domainMeta) {
        if (classElement.getKind() == ElementKind.CLASS) {
            if (domainMeta.providesConstructor()
                    && classElement.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new AptException(Message.DOMA4132, env, classElement);
            }
            if (classElement.getNestingKind().isNested()) {
                validateEnclosingElement(classElement);
            }
        } else if (classElement.getKind() == ElementKind.ENUM) {
            if (domainMeta.providesConstructor()) {
                DomainMirror domainMirror = domainMeta.getDomainMirror();
                throw new AptException(Message.DOMA4184, env, classElement,
                        domainMirror.getAnnotationMirror(),
                        domainMirror.getFactoryMethod());
            }
            if (classElement.getNestingKind().isNested()) {
                validateEnclosingElement(classElement);
            }
        } else if (classElement.getKind() == ElementKind.INTERFACE) {
            if (domainMeta.providesConstructor()) {
                throw new AptException(Message.DOMA4268, env, classElement);
            }
            if (classElement.getNestingKind().isNested()) {
                validateEnclosingElement(classElement);
            }
        } else {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(Message.DOMA4105, env, classElement,
                    domainMirror.getAnnotationMirror());
        }
    }

    protected void validateEnclosingElement(Element element) {
        TypeElement typeElement = ElementUtil.toTypeElement(element, env);
        if (typeElement == null) {
            return;
        }
        String simpleName = typeElement.getSimpleName().toString();
        if (simpleName.contains("$")
                || simpleName
                        .contains(Constants.BINARY_NAME_ENCLOSING_DELIMITER)) {
            throw new AptException(Message.DOMA4277, env, typeElement,
                    typeElement.getQualifiedName());
        }
        NestingKind nestingKind = typeElement.getNestingKind();
        if (nestingKind == NestingKind.TOP_LEVEL) {
            return;
        } else if (nestingKind == NestingKind.MEMBER) {
            Set<Modifier> modifiers = typeElement.getModifiers();
            if (modifiers.containsAll(Arrays.asList(Modifier.STATIC,
                    Modifier.PUBLIC))) {
                validateEnclosingElement(typeElement.getEnclosingElement());
            } else {
                throw new AptException(Message.DOMA4275, env, typeElement,
                        typeElement.getQualifiedName());
            }
        } else {
            throw new AptException(Message.DOMA4276, env, typeElement,
                    typeElement.getQualifiedName());
        }
    }

    protected void validateAcceptNull(TypeElement classElement,
            DomainMeta domainMeta) {
        if (domainMeta.getBasicCtType().isPrimitive()
                && domainMeta.getAcceptNull()) {
            DomainMirror domainMirror = domainMeta.getDomainMirror();
            throw new AptException(Message.DOMA4251, env, classElement,
                    domainMirror.getAnnotationMirror(),
                    domainMirror.getAcceptNull());
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
        throw new AptException(Message.DOMA4103, env, classElement,
                domainMeta.getValueType());
    }

    protected void validateFactoryMethod(TypeElement classElement,
            DomainMeta domainMeta) {
        outer: for (ExecutableElement method : ElementFilter
                .methodsIn(classElement.getEnclosedElements())) {
            if (!method.getSimpleName().contentEquals(
                    domainMeta.getFactoryMethod())) {
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
            if (!env.getTypeUtils().isAssignable(domainMeta.getValueType(),
                    parameterType)) {
                continue;
            }
            TypeMirror returnType = env.getTypeUtils().erasure(
                    method.getReturnType());
            if (!env.getTypeUtils().isAssignable(returnType,
                    domainMeta.getType())) {
                continue;
            }
            List<? extends TypeParameterElement> classTypeParams = classElement
                    .getTypeParameters();
            List<? extends TypeParameterElement> methodTypeParams = method
                    .getTypeParameters();
            if (classTypeParams.size() != methodTypeParams.size()) {
                continue;
            }
            for (Iterator<? extends TypeParameterElement> cit = classTypeParams
                    .iterator(), mit = methodTypeParams.iterator(); cit
                    .hasNext() && mit.hasNext();) {
                TypeParameterElement classTypeParam = cit.next();
                TypeParameterElement methodTypeParam = mit.next();
                if (!TypeMirrorUtil.isSameType(classTypeParam.asType(),
                        methodTypeParam.asType(), env)) {
                    continue outer;
                }
            }
            return;
        }
        throw new AptException(Message.DOMA4106, env, classElement,
                domainMeta.getFactoryMethod(), classElement.asType(),
                domainMeta.getValueType(), domainMeta.getFactoryMethod());
    }

    protected void validateAccessorMethod(TypeElement classElement,
            DomainMeta domainMeta) {
        TypeElement typeElement = classElement;
        TypeMirror typeMirror = classElement.asType();
        for (; typeElement != null && typeMirror.getKind() != TypeKind.NONE;) {
            for (ExecutableElement method : ElementFilter.methodsIn(typeElement
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
                TypeMirror returnType = method.getReturnType();
                if (env.getTypeUtils().isAssignable(
                        env.getTypeUtils().erasure(returnType),
                        domainMeta.getValueType())) {
                    return;
                }
                TypeVariable typeVariable = TypeMirrorUtil.toTypeVariable(
                        returnType, env);
                if (typeVariable != null) {
                    TypeMirror inferredReturnType = inferType(typeVariable,
                            typeElement, typeMirror);
                    if (inferredReturnType != null) {
                        if (env.getTypeUtils().isAssignable(inferredReturnType,
                                domainMeta.getValueType())) {
                            return;
                        }
                    }
                }
            }
            typeMirror = typeElement.getSuperclass();
            typeElement = TypeMirrorUtil.toTypeElement(typeMirror, env);
        }
        throw new AptException(Message.DOMA4104, env, classElement,
                domainMeta.getAccessorMethod(), domainMeta.getValueType());
    }

    protected TypeMirror erasureReturnType(TypeMirror returnType) {
        return env.getTypeUtils().erasure(returnType);
    }

    protected TypeMirror inferType(TypeVariable typeVariable,
            TypeElement classElement, TypeMirror classMirror) {
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(classMirror,
                env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        if (args.isEmpty()) {
            return null;
        }
        int argsSize = args.size();
        int index = 0;
        for (TypeParameterElement typeParam : classElement.getTypeParameters()) {
            if (index >= argsSize) {
                break;
            }
            if (TypeMirrorUtil
                    .isSameType(typeVariable, typeParam.asType(), env)) {
                return args.get(index);
            }
            index++;
        }
        return null;
    }
}
