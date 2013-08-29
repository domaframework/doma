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
import javax.lang.model.util.Elements;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

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

    public String getBinaryName() {
        if (typeElement == null) {
            return type.toString();
        }
        return ElementUtil.getBinaryName(typeElement, env);
    }

    public boolean isUnknownType() {
        return type.getKind() == TypeKind.NONE;
    }

    public boolean isNullType() {
        return type.getKind() == TypeKind.NULL;
    }

    public boolean isBooleanType() {
        return type.getKind() == TypeKind.BOOLEAN
                || TypeMirrorUtil.isSameType(type, Boolean.class, env);
    }

    public boolean isTextType() {
        return type.getKind() == TypeKind.CHAR
                || TypeMirrorUtil.isSameType(type, String.class, env)
                || TypeMirrorUtil.isSameType(type, Character.class, env);
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

        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return false;
        }
        return NUMBER_PRIORITY_MAP.containsKey(ElementUtil.getBinaryName(
                typeElement, env));
    }

    public int getNumberPriority() {
        return numberPriority;
    }

    public List<ConstructorDeclaration> getConstructorDeclarations(
            List<TypeDeclaration> parameterTypeDeclarations) {
        List<ConstructorDeclaration> candidates = getCandidateConstructorDeclarations(parameterTypeDeclarations);
        if (candidates.size() == 1) {
            return candidates;
        }
        ConstructorDeclaration constructorDeclaration = findSuitableConstructorDeclaration(
                parameterTypeDeclarations, candidates);
        if (constructorDeclaration != null) {
            return Collections.singletonList(constructorDeclaration);
        }
        return candidates;
    }

    protected List<ConstructorDeclaration> getCandidateConstructorDeclarations(
            List<TypeDeclaration> parameterTypeDeclarations) {
        List<ConstructorDeclaration> results = new LinkedList<ConstructorDeclaration>();
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
                    TypeMirror t1 = TypeMirrorUtil.boxIfPrimitive(
                            typeDeclIterator.next().getType(), env);
                    TypeMirror t2 = TypeMirrorUtil.boxIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    if (!TypeMirrorUtil.isAssignable(t1, t2, env)) {
                        continue outer;
                    }
                }
                ConstructorDeclaration constructorDeclaration = ConstructorDeclaration
                        .newInstance(constructor, typeParameterDeclarations,
                                env);
                results.add(constructorDeclaration);
            }
        }
        return results;
    }

    protected ConstructorDeclaration findSuitableConstructorDeclaration(
            List<TypeDeclaration> parameterTypeDeclarations,
            List<ConstructorDeclaration> candidates) {
        outer: for (ConstructorDeclaration constructorDeclaration : candidates) {
            Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                    .iterator();
            Iterator<? extends VariableElement> valueElementIterator = constructorDeclaration
                    .getElement().getParameters().iterator();
            while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
                TypeMirror t1 = TypeMirrorUtil.boxIfPrimitive(typeDeclIterator
                        .next().getType(), env);
                TypeMirror t2 = TypeMirrorUtil.boxIfPrimitive(
                        valueElementIterator.next().asType(), env);
                if (!TypeMirrorUtil.isSameType(t1, t2, env)) {
                    continue outer;
                }
            }
            return constructorDeclaration;
        }
        return null;
    }

    public FieldDeclaration getFieldDeclaration(String name) {
        return getFieldDeclarationInternal(name, false);
    }

    public FieldDeclaration getStaticFieldDeclaration(String name) {
        return getFieldDeclarationInternal(name, true);
    }

    public FieldDeclaration getFieldDeclarationInternal(String name,
            boolean statik) {
        List<FieldDeclaration> candidates = getCandidateFieldDeclaration(name,
                statik);
        removeHiddenFieldDeclarations(candidates);

        if (candidates.size() == 0) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        throw new AptIllegalStateException(name);
    }

    public List<FieldDeclaration> getCandidateFieldDeclaration(String name,
            boolean statik) {
        List<FieldDeclaration> results = new LinkedList<FieldDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = ElementUtil.getTypeElement(
                    typeQualifiedName, env);
            for (VariableElement field : ElementFilter.fieldsIn(typeElement
                    .getEnclosedElements())) {
                if (statik && !field.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
                if (!field.getSimpleName().contentEquals(name)) {
                    continue;
                }
                FieldDeclaration fieldDeclaration = FieldDeclaration
                        .newInstance(field, typeParameterDeclarations, env);
                results.add(fieldDeclaration);
            }
        }
        return results;
    }

    protected void removeHiddenFieldDeclarations(
            List<FieldDeclaration> candidates) {
        List<FieldDeclaration> hiders = new LinkedList<FieldDeclaration>(
                candidates);
        for (Iterator<FieldDeclaration> it = candidates.iterator(); it
                .hasNext();) {
            FieldDeclaration hidden = it.next();
            for (FieldDeclaration hider : hiders) {
                if (env.getElementUtils().hides(hider.getElement(),
                        hidden.getElement())) {
                    it.remove();
                }
            }
        }
    }

    public List<MethodDeclaration> getMethodDeclarations(String name,
            List<TypeDeclaration> parameterTypeDeclarations) {
        return getMethodDeclarationsInternal(name, parameterTypeDeclarations,
                false);
    }

    public List<MethodDeclaration> getStaticMethodDeclarations(String name,
            List<TypeDeclaration> parameterTypeDeclarations) {
        return getMethodDeclarationsInternal(name, parameterTypeDeclarations,
                true);
    }

    protected List<MethodDeclaration> getMethodDeclarationsInternal(
            String name, List<TypeDeclaration> parameterTypeDeclarations,
            boolean statik) {
        List<MethodDeclaration> candidates = getCandidateMethodDeclarations(
                name, parameterTypeDeclarations, statik);
        removeOverriddenMethodDeclarations(candidates);
        removeHiddenMethodDeclarations(candidates);
        if (candidates.size() == 1) {
            return candidates;
        }
        MethodDeclaration suitableMethodDeclaration = findSuitableMethodDeclaration(
                parameterTypeDeclarations, candidates);
        if (suitableMethodDeclaration != null) {
            return Collections.singletonList(suitableMethodDeclaration);
        }
        return candidates;
    }

    protected List<MethodDeclaration> getCandidateMethodDeclarations(
            String name, List<TypeDeclaration> parameterTypeDeclarations,
            boolean statik) {
        List<MethodDeclaration> results = new LinkedList<MethodDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String binaryName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = ElementUtil.getTypeElement(binaryName,
                    env);
            if (typeElement == null) {
                continue;
            }

            outer: for (ExecutableElement method : ElementFilter
                    .methodsIn(typeElement.getEnclosedElements())) {
                if (statik && !method.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
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
                    TypeMirror t1 = TypeMirrorUtil.boxIfPrimitive(
                            typeDeclIterator.next().getType(), env);
                    TypeMirror t2 = TypeMirrorUtil.boxIfPrimitive(
                            valueElementIterator.next().asType(), env);
                    if (!TypeMirrorUtil.isAssignable(t1, t2, env)) {
                        continue outer;
                    }
                }
                MethodDeclaration methodDeclaration = MethodDeclaration
                        .newInstance(method, typeParameterDeclarations, env);
                results.add(methodDeclaration);
            }
        }
        return results;
    }

    protected void removeOverriddenMethodDeclarations(
            List<MethodDeclaration> candidates) {
        List<MethodDeclaration> overriders = new LinkedList<MethodDeclaration>(
                candidates);
        Elements elements = env.getElementUtils();
        for (Iterator<MethodDeclaration> it = candidates.iterator(); it
                .hasNext();) {
            MethodDeclaration overridden = it.next();
            for (MethodDeclaration overrider : overriders) {
                TypeElement overriderTypeElement = ElementUtil.toTypeElement(
                        overrider.getElement().getEnclosingElement(), env);
                if (overriderTypeElement == null) {
                    continue;
                }
                if (elements.overrides(overrider.getElement(),
                        overridden.getElement(), overriderTypeElement)) {
                    it.remove();
                }
            }
        }
    }

    protected void removeHiddenMethodDeclarations(
            List<MethodDeclaration> candidates) {
        List<MethodDeclaration> hiders = new LinkedList<MethodDeclaration>(
                candidates);
        for (Iterator<MethodDeclaration> it = candidates.iterator(); it
                .hasNext();) {
            MethodDeclaration hidden = it.next();
            for (MethodDeclaration hider : hiders) {
                TypeMirror subtype = hider.getElement().getEnclosingElement()
                        .asType();
                TypeMirror supertype = hidden.getElement()
                        .getEnclosingElement().asType();
                if (TypeMirrorUtil.isAssignable(subtype, supertype, env)) {
                    if (env.getElementUtils().hides(hider.getElement(),
                            hidden.getElement())) {
                        it.remove();
                    }
                }
            }
        }
    }

    protected MethodDeclaration findSuitableMethodDeclaration(
            List<TypeDeclaration> parameterTypeDeclarations,
            List<MethodDeclaration> candidates) {
        outer: for (MethodDeclaration methodDeclaration : candidates) {
            Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations
                    .iterator();
            Iterator<? extends VariableElement> valueElementIterator = methodDeclaration
                    .getElement().getParameters().iterator();
            while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
                TypeMirror t1 = TypeMirrorUtil.boxIfPrimitive(typeDeclIterator
                        .next().getType(), env);
                TypeMirror t2 = TypeMirrorUtil.boxIfPrimitive(
                        valueElementIterator.next().asType(), env);
                if (!TypeMirrorUtil.isAssignable(t1, t2, env)) {
                    continue outer;
                }
            }
            return methodDeclaration;
        }
        return null;
    }

    public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
        assertNotNull(other);
        assertTrue(isTextType());
        assertTrue(other.isTextType());
        TypeMirror type = TypeMirrorUtil.getTypeMirror(String.class, env);
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
        if (TypeMirrorUtil.isSameType(this.type, other.type, env)) {
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
        return newTypeDeclaration(TypeMirrorUtil.getTypeMirror(clazz, env), env);
    }

    public static TypeDeclaration newTypeDeclaration(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        Map<String, List<TypeParameterDeclaration>> map = new HashMap<String, List<TypeParameterDeclaration>>();
        gatherTypeParameterDeclarations(type, map, env);
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.type = type;
        typeDeclaration.typeElement = typeElement;
        typeDeclaration.typeParameterDeclarationsMap = map;
        typeDeclaration.env = env;
        typeDeclaration.numberPriority = determineNumberPriority(typeElement,
                type, env);
        return typeDeclaration;
    }

    protected static int determineNumberPriority(TypeElement typeElement,
            TypeMirror type, ProcessingEnvironment env) {
        if (typeElement != null) {
            Integer result = NUMBER_PRIORITY_MAP.get(ElementUtil.getBinaryName(
                    typeElement, env));
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
        TypeMirror type = TypeMirrorUtil.getTypeMirror(boolean.class, env);
        return newTypeDeclaration(type, env);
    }

    protected static void gatherTypeParameterDeclarations(
            TypeMirror type,
            Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap,
            ProcessingEnvironment env) {
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return;
        }
        typeParameterDeclarationsMap.put(
                ElementUtil.getBinaryName(typeElement, env),
                createTypeParameterDeclarations(typeElement, type, env));
        for (TypeMirror superType : env.getTypeUtils().directSupertypes(type)) {
            TypeElement superElement = TypeMirrorUtil.toTypeElement(superType,
                    env);
            if (superElement == null) {
                continue;
            }
            String superBinaryName = ElementUtil.getBinaryName(superElement,
                    env);
            if (typeParameterDeclarationsMap.containsKey(superBinaryName)) {
                continue;
            }
            typeParameterDeclarationsMap.put(
                    superBinaryName,
                    createTypeParameterDeclarations(superElement, superType,
                            env));
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
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
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
