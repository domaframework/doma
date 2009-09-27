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

import org.seasar.doma.internal.apt.TypeUtil;

public class TypeDeclaration {

    protected TypeElement typeElement;

    protected Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap;

    protected ProcessingEnvironment env;

    protected TypeDeclaration() {
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public FieldDeclaration getFieldDeclaration(String name) {
        List<FieldDeclaration> candidate = new LinkedList<FieldDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = env.getElementUtils().getTypeElement(
                    typeQualifiedName);
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
        throw new AssertionError("net yet implemented");
    }

    public List<MethodDeclaration> getMethodDeclarations(String name,
            int parameterSize) {
        List<MethodDeclaration> candidate = new LinkedList<MethodDeclaration>();
        for (Map.Entry<String, List<TypeParameterDeclaration>> e : typeParameterDeclarationsMap
                .entrySet()) {
            String typeQualifiedName = e.getKey();
            List<TypeParameterDeclaration> typeParameterDeclarations = e
                    .getValue();
            TypeElement typeElement = env.getElementUtils().getTypeElement(
                    typeQualifiedName);
            for (ExecutableElement method : ElementFilter.methodsIn(typeElement
                    .getEnclosedElements())) {
                if (!method.getModifiers().contains(Modifier.PUBLIC)) {
                    continue;
                }
                if (!method.getSimpleName().contentEquals(name)) {
                    continue;
                }
                if (method.getReturnType().getKind() == TypeKind.VOID) {
                    continue;
                }
                if (method.getParameters().size() != parameterSize) {
                    continue;
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

    public static TypeDeclaration newInstance(TypeElement typeElement,
            ProcessingEnvironment env) {
        Map<String, List<TypeParameterDeclaration>> map = new HashMap<String, List<TypeParameterDeclaration>>();
        gatherTypeParameterDeclarations(typeElement.asType(), map, env);
        TypeDeclaration typeDeclaration = new TypeDeclaration();
        typeDeclaration.typeElement = typeElement;
        typeDeclaration.typeParameterDeclarationsMap = map;
        typeDeclaration.env = env;
        return typeDeclaration;
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
