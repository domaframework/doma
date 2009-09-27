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
package org.seasar.doma.internal.apt.declaration;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.ElementUtil;
import org.seasar.doma.internal.apt.TypeUtil;

public class TypeDeclaration {

    protected TypeElement typeElement;

    protected TypeMirror type;

    protected Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap;

    protected ProcessingEnvironment env;

    protected TypeDeclaration() {
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public TypeMirror getType() {
        return type;
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return env;
    }

    public String getQualifiedName() {
        return typeElement.getQualifiedName().toString();
    }

    public boolean isUnknownType() {
        return type.getKind() == TypeKind.NONE;
    }

    public boolean isNullType() {
        return TypeUtil.isSameType(type, Void.class, env);
    }

    public boolean isBooleanType() {
        return TypeUtil.isSameType(type, Boolean.class, env);
    }

    public boolean isNumberType() {
        return TypeUtil.isSameType(type, BigDecimal.class, env)
                || TypeUtil.isSameType(type, BigInteger.class, env)
                || TypeUtil.isSameType(type, Double.class, env)
                || TypeUtil.isSameType(type, Float.class, env)
                || TypeUtil.isSameType(type, Long.class, env)
                || TypeUtil.isSameType(type, Integer.class, env)
                || TypeUtil.isSameType(type, Short.class, env)
                || TypeUtil.isSameType(type, Byte.class, env);
    }

    public ConstructorDeclaration getConstructorDeclarations(
            List<TypeDeclaration> parameterTypeDeclarations) {
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = ElementUtil.getTypeElement(
                    typeQualifiedName, env);

            outer: for (ExecutableElement constructor : ElementFilter
                    .constructorsIn(typeElement.getEnclosedElements())) {
                if (!constructor.getModifiers().contains(Modifier.PUBLIC)) {
                    continue;
                }
                List<? extends VariableElement> parameters = constructor
                        .getParameters();
                if (constructor.getParameters().size() != parameterTypeDeclarations
                        .size()) {
                    continue;
                }
                Iterator<? extends VariableElement> valueElementIterator = parameters
                        .iterator();
                Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                        .iterator();
                for (; valueElementIterator.hasNext()
                        && typeDeclIterator.hasNext();) {
                    TypeMirror t1 = TypeUtil.toWrapperTypeIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    TypeMirror t2 = typeDeclIterator.next().getType();
                    if (!TypeUtil.isSameType(t1, t2, env)) {
                        continue outer;
                    }
                }
                return ConstructorDeclaration.newInstance(constructor,
                        typeParameterDeclarations, env);
            }
        }
        return null;
    }

    public FieldDeclaration getFieldDeclaration(String name) {
        List<FieldDeclaration> candidate = new LinkedList<FieldDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = ElementUtil.getTypeElement(
                    typeQualifiedName, env);
            for (VariableElement field : ElementFilter.fieldsIn(typeElement
                    .getEnclosedElements())) {
                if (field.getModifiers().contains(Modifier.PRIVATE)
                        || field.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
                if (!field.getSimpleName().contentEquals(name)) {
                    continue;
                }
                FieldDeclaration fieldDeclaration = FieldDeclaration
                        .newInstance(field, typeParameterDeclarations, env);
                candidate.add(fieldDeclaration);
            }
        }

        List<FieldDeclaration> hiders = new LinkedList<FieldDeclaration>(
                candidate);
        for (Iterator<FieldDeclaration> it = candidate.iterator(); it.hasNext();) {
            FieldDeclaration hidden = it.next();
            for (FieldDeclaration hider : hiders) {
                if (env.getElementUtils().hides(hider.getElement(),
                        hidden.getElement())) {
                    it.remove();
                }
            }
        }
        if (candidate.size() == 0) {
            return null;
        }
        if (candidate.size() == 1) {
            return candidate.get(0);
        }
        throw new AptIllegalStateException();
    }

    public List<MethodDeclaration> getMethodDeclarations(String name,
            List<TypeDeclaration> parameterTypeDeclarations) {
        List<MethodDeclaration> candidate = new LinkedList<MethodDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = ElementUtil.getTypeElement(
                    typeQualifiedName, env);

            outer: for (ExecutableElement method : ElementFilter
                    .methodsIn(typeElement.getEnclosedElements())) {
                if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                    continue;
                }
                if (!method.getSimpleName().contentEquals(name)) {
                    continue;
                }
                if (method.getReturnType().getKind() == TypeKind.VOID) {
                    continue;
                }
                List<? extends VariableElement> parameters = method
                        .getParameters();
                if (method.getParameters().size() != parameterTypeDeclarations
                        .size()) {
                    continue;
                }
                Iterator<? extends VariableElement> valueElementIterator = parameters
                        .iterator();
                Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                        .iterator();
                for (; valueElementIterator.hasNext()
                        && typeDeclIterator.hasNext();) {
                    TypeMirror t1 = TypeUtil.toWrapperTypeIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    TypeMirror t2 = typeDeclIterator.next().getType();
                    if (!TypeUtil.isSameType(t1, t2, env)) {
                        continue outer;
                    }
                }
                MethodDeclaration methodDeclaration = MethodDeclaration
                        .newInstance(method, typeParameterDeclarations, env);
                candidate.add(methodDeclaration);
            }
        }

        List<MethodDeclaration> overriders = new LinkedList<MethodDeclaration>(
                candidate);
        for (Iterator<MethodDeclaration> it = candidate.iterator(); it
                .hasNext();) {
            MethodDeclaration overridden = it.next();
            for (MethodDeclaration overrider : overriders) {
                if (env.getElementUtils().overrides(overrider.getElement(),
                        overridden.getElement(), typeElement)) {
                    it.remove();
                }
            }
        }
        return candidate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TypeDeclaration other = (TypeDeclaration) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!TypeUtil.isSameType(type, other.type, env)) {
            return false;
        }
        return true;
    }

    public static TypeDeclaration newInstance(TypeElement typeElement,
            ProcessingEnvironment env) {
        assertNotNull(typeElement, env);
        Map<String, List<TypeParameterDeclaration>> map = new HashMap<String, List<TypeParameterDeclaration>>();
        gatherTypeParameterDeclarations(typeElement.asType(), map, env);
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.typeElement = typeElement;
        typeDeclaration.type = typeElement.asType();
        typeDeclaration.typeParameterDeclarationsMap = map;
        typeDeclaration.env = env;
        return typeDeclaration;
    }

    public static TypeDeclaration newUnknownInstance(ProcessingEnvironment env) {
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.type = env.getTypeUtils().getNoType(TypeKind.NONE);
        typeDeclaration.typeParameterDeclarationsMap = Collections.emptyMap();
        typeDeclaration.env = env;
        return typeDeclaration;
    }

    public static TypeDeclaration newBooleanInstance(ProcessingEnvironment env) {
        assertNotNull(env);
        TypeElement typeElement = ElementUtil
                .getTypeElement(Boolean.class, env);
        return newInstance(typeElement, env);
    }

    public static TypeDeclaration newNumberInstance(
            TypeDeclaration leftOperand, TypeDeclaration rightOperand,
            ProcessingEnvironment env) {
        assertNotNull(leftOperand, rightOperand, env);
        TypeMirror t1 = leftOperand.getType();
        TypeMirror t2 = rightOperand.getType();
        for (Class<?> clazz : Arrays.<Class<?>> asList(Integer.class,
                Long.class, BigDecimal.class, BigInteger.class, Double.class,
                Float.class, Short.class, Byte.class)) {
            if (TypeUtil.isSameType(t1, clazz, env)
                    || TypeUtil.isSameType(t2, clazz, env)) {
                TypeDeclaration typeDeclaration = newInstance(ElementUtil
                        .getTypeElement(clazz, env), env);
                if (typeDeclaration != null) {
                    return typeDeclaration;
                }
            }
        }
        throw new AptIllegalStateException();
    }

    protected static void gatherTypeParameterDeclarations(
            TypeMirror type,
            Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap,
            ProcessingEnvironment env) {
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return;
        }
        typeParameterDeclarationsMap.put(typeElement.getQualifiedName()
                .toString(), createTypeParameterDeclarations(typeElement, type,
                env));
        for (TypeMirror superType : env.getTypeUtils().directSupertypes(type)) {
            TypeElement superElement = TypeUtil.toTypeElement(superType, env);
            if (superElement == null) {
                continue;
            }
            if (typeParameterDeclarationsMap.containsKey(superElement
                    .getQualifiedName().toString())) {
                continue;
            }
            typeParameterDeclarationsMap.put(superElement.getQualifiedName()
                    .toString(), createTypeParameterDeclarations(superElement,
                    superType, env));
            gatherTypeParameterDeclarations(superType,
                    typeParameterDeclarationsMap, env);
        }
    }

    public static List<TypeParameterDeclaration> createTypeParameterDeclarations(
            TypeElement typeElement, TypeMirror type, ProcessingEnvironment env) {
        assertNotNull(typeElement, type, env);
        List<TypeParameterDeclaration> list = new ArrayList<TypeParameterDeclaration>();
        Iterator<? extends TypeParameterElement> formalParams = typeElement
                .getTypeParameters().iterator();
        DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
        Iterator<? extends TypeMirror> actualParams = declaredType
                .getTypeArguments().iterator();
        for (; formalParams.hasNext() && actualParams.hasNext();) {
            TypeMirror formalType = formalParams.next().asType();
            TypeMirror actualType = actualParams.next();
            TypeParameterDeclaration typeParameterDeclaration = TypeParameterDeclaration
                    .newInstance(formalType, actualType, env);
            list.add(typeParameterDeclaration);
        }
        return Collections.unmodifiableList(list);
    }

}
