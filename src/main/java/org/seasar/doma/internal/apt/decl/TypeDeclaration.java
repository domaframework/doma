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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;

public class TypeDeclaration {

    protected Context ctx;

    protected TypeElement typeElement;

    protected TypeMirror type;

    protected Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap = new HashMap<String, List<TypeParameterDeclaration>>();

    protected int numberPriority;

    public TypeDeclaration(Context ctx, TypeMirror typeMirror,
            TypeElement typeElement,
            Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap,
            int numberPriority) {
        this.ctx = ctx;
        this.type = typeMirror;
        this.typeElement = typeElement;
        this.typeParameterDeclarationsMap = typeParameterDeclarationsMap;
        this.numberPriority = numberPriority;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getBinaryName() {
        if (typeElement == null) {
            return type.toString();
        }
        return ctx.getElements().getBinaryName(typeElement).toString();
    }

    public boolean isUnknownType() {
        return type.getKind() == TypeKind.NONE;
    }

    public boolean isNullType() {
        return type.getKind() == TypeKind.NULL;
    }

    public boolean isBooleanType() {
        return type.getKind() == TypeKind.BOOLEAN
                || ctx.getTypes().isSameType(type, Boolean.class);
    }

    public boolean isTextType() {
        return type.getKind() == TypeKind.CHAR
                || ctx.getTypes().isSameType(type, String.class)
                || ctx.getTypes().isSameType(type, Character.class);
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
        default:
            return ctx.getTypes().isAssignable(type, Number.class);
        }
    }

    public boolean is(Class<?> clazz) {
        return ctx.getTypes().isSameType(type, clazz);
    }

    public int getNumberPriority() {
        return numberPriority;
    }

    public List<TypeParameterDeclaration> getTypeParameterDeclarations() {
        Optional<List<TypeParameterDeclaration>> typeParameterDeclarations = typeParameterDeclarationsMap
                .values().stream().findFirst();
        return typeParameterDeclarations.orElse(Collections.emptyList());
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
            TypeElement typeElement = ctx.getElements()
                    .getTypeElement(typeQualifiedName);

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
                    TypeMirror t1 = ctx.getTypes()
                            .boxIfPrimitive(typeDeclIterator.next().getType());
                    TypeMirror t2 = ctx.getTypes().boxIfPrimitive(
                            valueElementIterator.next().asType());
                    if (!ctx.getTypes().isAssignable(t1, t2)) {
                        continue outer;
                    }
                }
                ConstructorDeclaration constructorDeclaration = ctx
                        .getDeclarations().newConstructorDeclaration(
                                constructor, typeParameterDeclarations);
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
                TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator
                        .next().getType());
                TypeMirror t2 = ctx.getTypes()
                        .boxIfPrimitive(valueElementIterator.next().asType());
                if (!ctx.getTypes().isSameType(t1, t2)) {
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
            TypeElement typeElement = ctx.getElements()
                    .getTypeElement(typeQualifiedName);
            for (VariableElement field : ElementFilter.fieldsIn(typeElement
                    .getEnclosedElements())) {
                if (statik && !field.getModifiers().contains(Modifier.STATIC)) {
                    continue;
                }
                if (!field.getSimpleName().contentEquals(name)) {
                    continue;
                }
                FieldDeclaration fieldDeclaration = ctx.getDeclarations()
                        .newFieldDeclaration(field, typeParameterDeclarations);
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
                if (ctx.getElements().hides(hider.getElement(),
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
            TypeElement typeElement = ctx.getElements()
                    .getTypeElement(binaryName);
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
                    TypeMirror t1 = ctx.getTypes()
                            .boxIfPrimitive(typeDeclIterator.next().getType());
                    TypeMirror t2 = ctx.getTypes().boxIfPrimitive(
                            valueElementIterator.next().asType());
                    if (!ctx.getTypes().isAssignable(t1, t2)) {
                        continue outer;
                    }
                }
                MethodDeclaration methodDeclaration = ctx
                        .getDeclarations().newMethodDeclaration(method,
                                typeParameterDeclarations);
                results.add(methodDeclaration);
            }
        }
        return results;
    }

    protected void removeOverriddenMethodDeclarations(
            List<MethodDeclaration> candidates) {
        List<MethodDeclaration> overriders = new LinkedList<MethodDeclaration>(
                candidates);
        for (Iterator<MethodDeclaration> it = candidates.iterator(); it
                .hasNext();) {
            MethodDeclaration overridden = it.next();
            for (MethodDeclaration overrider : overriders) {
                TypeElement overriderTypeElement = ctx.getElements()
                        .toTypeElement(
                                overrider.getElement().getEnclosingElement());
                if (overriderTypeElement == null) {
                    continue;
                }
                if (ctx.getElements().overrides(overrider.getElement(),
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
                if (ctx.getTypes().isAssignable(subtype, supertype)) {
                    if (ctx.getElements().hides(hider.getElement(),
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
                TypeMirror t1 = ctx.getTypes()
                        .boxIfPrimitive(typeDeclIterator
                                .next().getType());
                TypeMirror t2 = ctx.getTypes()
                        .boxIfPrimitive(valueElementIterator.next().asType());
                if (!ctx.getTypes().isAssignable(t1, t2)) {
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
        TypeMirror type = ctx.getTypes().getTypeMirror(String.class);
        return ctx.getDeclarations().newTypeDeclaration(type);
    }

    public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
        assertNotNull(other);
        assertTrue(isNumberType());
        assertTrue(other.isNumberType());
        TypeMirror type = this.numberPriority >= other.numberPriority ? this.type
                : other.type;
        return ctx.getDeclarations().newTypeDeclaration(type);
    }

    public boolean isSameType(TypeDeclaration other) {
        if (ctx.getTypes().isSameType(this.type, other.type)) {
            return true;
        }
        if (this.isNumberType()) {
            if (other.isNumberType()) {
                return this.numberPriority == other.numberPriority;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return type.toString();
    }

}
