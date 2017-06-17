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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

/**
 * @author nakamura
 *
 */
public class Declarations {

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

    private final Context ctx;

    public Declarations(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public TypeDeclaration newTypeDeclaration(Class<?> clazz) {
        assertNotNull(clazz);
        return newTypeDeclaration(ctx.getTypes().getTypeMirror(clazz));
    }

    public TypeDeclaration newTypeDeclaration(TypeMirror type) {
        assertNotNull(type);
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        Map<String, List<TypeParameterDeclaration>> map = new LinkedHashMap<String, List<TypeParameterDeclaration>>();
        gatherTypeParameterDeclarations(type, map);
        int numberPriority = determineNumberPriority(typeElement, type);
        return new TypeDeclaration(ctx, type, typeElement, map, numberPriority);
    }

    public TypeDeclaration newUnknownTypeDeclaration() {
        TypeMirror type = ctx.getTypes().getNoType(TypeKind.NONE);
        Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap = Collections
                .emptyMap();
        return new TypeDeclaration(ctx, type, null,
                typeParameterDeclarationsMap, 0);
    }

    public TypeDeclaration newBooleanTypeDeclaration() {
        TypeMirror type = ctx.getTypes().getTypeMirror(boolean.class);
        return newTypeDeclaration(type);
    }

    protected void gatherTypeParameterDeclarations(TypeMirror type,
            Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
            return;
        }
        typeParameterDeclarationsMap.put(
                ctx.getElements().getBinaryName(typeElement).toString(),
                createTypeParameterDeclarations(typeElement, type));
        for (TypeMirror superType : ctx.getTypes().directSupertypes(type)) {
            TypeElement superElement = ctx.getTypes().toTypeElement(superType
                    );
            if (superElement == null) {
                continue;
            }
            String superBinaryName = ctx.getElements()
                    .getBinaryName(superElement).toString();
            if (typeParameterDeclarationsMap.containsKey(superBinaryName)) {
                continue;
            }
            typeParameterDeclarationsMap.put(superBinaryName,
                    createTypeParameterDeclarations(superElement, superType));
            gatherTypeParameterDeclarations(superType,
                    typeParameterDeclarationsMap);
        }
    }

    public List<TypeParameterDeclaration> createTypeParameterDeclarations(
            TypeElement typeElement, TypeMirror type) {
        assertNotNull(typeElement, type);
        List<TypeParameterDeclaration> list = new ArrayList<TypeParameterDeclaration>();
        Iterator<? extends TypeParameterElement> formalParams = typeElement
                .getTypeParameters().iterator();
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        Iterator<? extends TypeMirror> actualParams = declaredType
                .getTypeArguments().iterator();
        for (; formalParams.hasNext() && actualParams.hasNext();) {
            TypeMirror formalType = formalParams.next().asType();
            TypeMirror actualType = actualParams.next();
            TypeParameterDeclaration typeParameterDeclaration = newTypeParameterDeclaration(
                    formalType, actualType);
            list.add(typeParameterDeclaration);
        }
        return Collections.unmodifiableList(list);
    }

    protected int determineNumberPriority(TypeElement typeElement,
            TypeMirror type) {
        if (typeElement != null) {
            Integer result = NUMBER_PRIORITY_MAP
                    .get(ctx.getElements().getBinaryName(typeElement)
                            .toString());
            if (result != null) {
                return result.intValue();
            }
        }
        Integer result = NUMBER_PRIORITY_MAP
                .get(type.getKind().name().toLowerCase());
        if (result != null) {
            return result.intValue();
        }
        return 0;
    }

    public FieldDeclaration newFieldDeclaration(
            VariableElement fieldElement,
            List<TypeParameterDeclaration> typeParameterDeclarations) {
        assertNotNull(fieldElement, typeParameterDeclarations);
        assertTrue(
                fieldElement.getKind() == ElementKind.FIELD
                        || fieldElement.getKind() == ElementKind.ENUM_CONSTANT,
                fieldElement.getKind().toString());
        TypeMirror typeMirror = resolveTypeParameter(fieldElement.asType(),
                typeParameterDeclarations);
        TypeDeclaration typeDeclaration = newTypeDeclaration(typeMirror);
        return new FieldDeclaration(fieldElement, typeDeclaration);
    }

    public ConstructorDeclaration newConstructorDeclaration(
            ExecutableElement constructorElement,
            List<TypeParameterDeclaration> typeParameterDeclarations) {
        assertNotNull(constructorElement, typeParameterDeclarations);
        assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
        TypeMirror typeMirror = resolveTypeParameter(
                constructorElement.asType(), typeParameterDeclarations);
        TypeDeclaration typeDeclaration = newTypeDeclaration(typeMirror);
        return new ConstructorDeclaration(constructorElement, typeDeclaration);
    }

    public MethodDeclaration newMethodDeclaration(
            ExecutableElement methodElement,
            List<TypeParameterDeclaration> typeParameterDeclarations) {
        assertNotNull(methodElement, typeParameterDeclarations);
        assertTrue(methodElement.getKind() == ElementKind.METHOD);
        TypeMirror returnTypeMirror = resolveTypeParameter(
                methodElement.getReturnType(),
                typeParameterDeclarations);
        TypeDeclaration returnTypeDeclaration = newTypeDeclaration(
                returnTypeMirror);
        return new MethodDeclaration(methodElement, returnTypeDeclaration);
    }

    public  TypeParameterDeclaration newTypeParameterDeclaration(TypeMirror formalType,
            TypeMirror actualType) {
        assertNotNull(formalType, actualType);
        return new TypeParameterDeclaration(formalType, actualType);
    }

    protected TypeMirror resolveTypeParameter(TypeMirror formalType,
            List<TypeParameterDeclaration> typeParameterDeclarations) {
        for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
            if (formalType.equals(typeParameterDecl.getFormalType())) {
                return typeParameterDecl.getActualType();
            }
        }
        return formalType;
    }

}
