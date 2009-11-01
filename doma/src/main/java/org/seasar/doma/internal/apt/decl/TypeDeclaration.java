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
package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeUtil;

public class TypeDeclaration {

    protected static final Map<String, Integer> NUMBER_PRIORITY_MAP = new HashMap<String, Integer>();
    static {
        NUMBER_PRIORITY_MAP.put(BigDecimal.class.getName(), 80);
        NUMBER_PRIORITY_MAP.put(BigInteger.class.getName(), 70);
        NUMBER_PRIORITY_MAP.put(double.class.getName(), 60);
        NUMBER_PRIORITY_MAP.put(Double.class.getName(), 60);
        NUMBER_PRIORITY_MAP.put(float.class.getName(), 50);
        NUMBER_PRIORITY_MAP.put(Float.class.getName(), 50);
        NUMBER_PRIORITY_MAP.put(long.class.getName(), 40);
        NUMBER_PRIORITY_MAP.put(Long.class.getName(), 40);
        NUMBER_PRIORITY_MAP.put(int.class.getName(), 30);
        NUMBER_PRIORITY_MAP.put(Integer.class.getName(), 30);
        NUMBER_PRIORITY_MAP.put(short.class.getName(), 20);
        NUMBER_PRIORITY_MAP.put(Short.class.getName(), 20);
        NUMBER_PRIORITY_MAP.put(byte.class.getName(), 10);
        NUMBER_PRIORITY_MAP.put(Byte.class.getName(), 10);
    }

    protected TypeElement typeElement;

    protected TypeMirror type;

    protected Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap = new HashMap<String, List<TypeParameterDeclaration>>();

    protected ProcessingEnvironment env;

    protected int numberPriority;

    protected TypeDeclaration() {
    }

    public TypeMirror getType() {
        return type;
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return env;
    }

    public String getQualifiedName() {
        if (typeElement == null) {
            return type.toString();
        }
        return typeElement.getQualifiedName().toString();
    }

    public boolean isUnknownType() {
        return type.getKind() == TypeKind.NONE;
    }

    public boolean isNullType() {
        return type.getKind() == TypeKind.NULL;
    }

    public boolean isBooleanType() {
        return type.getKind() == TypeKind.BOOLEAN
                || TypeUtil.isSameType(type, Boolean.class, env);
    }

    public boolean isTextType() {
        return type.getKind() == TypeKind.CHAR
                || TypeUtil.isSameType(type, String.class, env)
                || TypeUtil.isSameType(type, Character.class, env);
    }

    public boolean isNumberType() {
        switch (type.getKind()) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
            return true;
        }

        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return false;
        }
        return NUMBER_PRIORITY_MAP.containsKey(typeElement.getQualifiedName()
                .toString());
    }

    public int getNumberPriority() {
        return numberPriority;
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
                if (parameters.size() != parameterTypeDeclarations.size()) {
                    continue;
                }
                Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                        .iterator();
                Iterator<? extends VariableElement> valueElementIterator = parameters
                        .iterator();
                while (typeDeclIterator.hasNext()
                        && valueElementIterator.hasNext()) {
                    TypeMirror t1 = TypeUtil.boxIfPrimitive(typeDeclIterator
                            .next().getType(), env);
                    TypeMirror t2 = TypeUtil.boxIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    if (!TypeUtil.isAssignable(t1, t2, env)) {
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
                if (field.getModifiers().contains(Modifier.STATIC)) {
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
        throw new AptIllegalStateException(name);
    }

    public List<MethodDeclaration> getMethodDeclarations(String name,
            List<TypeDeclaration> parameterTypeDeclarations) {
        return getMethodDeclarationsInternal(name, parameterTypeDeclarations);
    }

    public List<MethodDeclaration> getStaticMethodDeclarations(String name,
            List<TypeDeclaration> parameterTypeDeclarations) {
        List<MethodDeclaration> results = new ArrayList<MethodDeclaration>();
        for (MethodDeclaration methodDeclaration : getMethodDeclarationsInternal(
                name, parameterTypeDeclarations)) {
            if (methodDeclaration.isStatic()) {
                results.add(methodDeclaration);
            }
        }
        return results;
    }

    protected List<MethodDeclaration> getMethodDeclarationsInternal(
            String name, List<TypeDeclaration> parameterTypeDeclarations) {
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
                Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                        .iterator();
                Iterator<? extends VariableElement> valueElementIterator = parameters
                        .iterator();
                while (typeDeclIterator.hasNext()
                        && valueElementIterator.hasNext()) {
                    TypeMirror t1 = TypeUtil.boxIfPrimitive(typeDeclIterator
                            .next().getType(), env);
                    TypeMirror t2 = TypeUtil.boxIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    if (!TypeUtil.isAssignable(t1, t2, env)) {
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
                } else if (env.getElementUtils().hides(overrider.getElement(),
                        overridden.getElement())) {
                    it.remove();
                }
            }
        }
        return candidate;
    }

    public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
        assertNotNull(other);
        assertTrue(isTextType());
        assertTrue(other.isTextType());
        TypeMirror type = TypeUtil.getTypeMirror(String.class, env);
        return newTypeDeclaration(type, env);
    }

    public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
        assertNotNull(other);
        assertTrue(isNumberType());
        assertTrue(other.isNumberType());
        TypeMirror type = this.numberPriority >= other.numberPriority ? this.type
                : other.type;
        return newTypeDeclaration(type, env);
    }

    public boolean isSameType(TypeDeclaration other) {
        if (TypeUtil.isSameType(this.type, other.type, env)) {
            return true;
        }
        if (this.isNumberType()) {
            if (other.isNumberType()) {
                return this.numberPriority == other.numberPriority;
            }
        }
        return false;
    }

    public static TypeDeclaration newTypeDeclaration(Class<?> clazz,
            ProcessingEnvironment env) {
        assertNotNull(clazz);
        return newTypeDeclaration(TypeUtil.getTypeMirror(clazz, env), env);
    }

    public static TypeDeclaration newTypeDeclaration(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        Map<String, List<TypeParameterDeclaration>> map = new HashMap<String, List<TypeParameterDeclaration>>();
        gatherTypeParameterDeclarations(type, map, env);
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.type = type;
        typeDeclaration.typeElement = typeElement;
        typeDeclaration.typeParameterDeclarationsMap = map;
        typeDeclaration.env = env;
        typeDeclaration.numberPriority = determineNumberPriority(typeElement,
                type);
        return typeDeclaration;
    }

    protected static int determineNumberPriority(TypeElement typeElement,
            TypeMirror type) {
        if (typeElement != null) {
            Integer result = NUMBER_PRIORITY_MAP.get(typeElement
                    .getQualifiedName().toString());
            if (result != null) {
                return result.intValue();
            }
        }
        Integer result = NUMBER_PRIORITY_MAP.get(type.getKind().name()
                .toLowerCase());
        if (result != null) {
            return result.intValue();
        }
        return 0;
    }

    public static TypeDeclaration newUnknownTypeDeclaration(
            ProcessingEnvironment env) {
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.type = env.getTypeUtils().getNoType(TypeKind.NONE);
        typeDeclaration.typeParameterDeclarationsMap = Collections.emptyMap();
        typeDeclaration.env = env;
        return typeDeclaration;
    }

    public static TypeDeclaration newBooleanTypeDeclaration(
            ProcessingEnvironment env) {
        assertNotNull(env);
        TypeMirror type = TypeUtil.getTypeMirror(boolean.class, env);
        return newTypeDeclaration(type, env);
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
