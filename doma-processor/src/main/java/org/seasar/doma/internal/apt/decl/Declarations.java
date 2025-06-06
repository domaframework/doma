/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.decl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.util.Zip;

public class Declarations {

  private final RoundContext ctx;

  public Declarations(RoundContext ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public TypeDeclaration newUnknownTypeDeclaration() {
    TypeMirror type = ctx.getMoreTypes().getNoType(TypeKind.NONE);
    return newTypeDeclaration(type, null);
  }

  public TypeDeclaration newPrimitiveBooleanTypeDeclaration() {
    TypeMirror type = ctx.getMoreTypes().getTypeMirror(boolean.class);
    return newTypeDeclaration(type, null);
  }

  public TypeDeclaration newTypeDeclaration(Class<?> clazz) {
    assertNotNull(clazz);
    TypeMirror type = ctx.getMoreTypes().getTypeMirror(clazz);
    return newTypeDeclaration(type);
  }

  public TypeDeclaration newTypeDeclaration(TypeMirror type) {
    assertNotNull(type);
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    return newTypeDeclaration(type, typeElement);
  }

  public TypeDeclaration newTypeDeclaration(TypeElement typeElement) {
    assertNotNull(typeElement);
    return newTypeDeclaration(typeElement.asType(), typeElement);
  }

  private TypeDeclaration newTypeDeclaration(TypeMirror type, TypeElement typeElement) {
    assertNotNull(type);
    CtType ctType = ctx.getCtTypes().newCtType(type);
    Map<Name, List<TypeParameterDeclaration>> map = new LinkedHashMap<>();
    gatherTypeParameterDeclarations(type, map);
    return new TypeDeclaration(ctx, type, ctType, typeElement, map);
  }

  private void gatherTypeParameterDeclarations(
      TypeMirror type, Map<Name, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
    TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
    if (typeElement == null) {
      return;
    }
    Name canonicalName = typeElement.getQualifiedName();
    typeParameterDeclarationsMap.put(canonicalName, newTypeParameterDeclaration(typeElement, type));
    for (TypeMirror superType : ctx.getMoreTypes().directSupertypes(type)) {
      TypeElement superElement = ctx.getMoreTypes().toTypeElement(superType);
      if (superElement == null) {
        continue;
      }
      Name superCanonicalName = superElement.getQualifiedName();
      if (typeParameterDeclarationsMap.containsKey(superCanonicalName)) {
        continue;
      }
      typeParameterDeclarationsMap.put(
          superCanonicalName, newTypeParameterDeclaration(superElement, superType));
      gatherTypeParameterDeclarations(superType, typeParameterDeclarationsMap);
    }
  }

  private List<TypeParameterDeclaration> newTypeParameterDeclaration(
      TypeElement typeElement, TypeMirror type) {
    assertNotNull(typeElement, type);
    DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(type);
    return Zip.stream(typeElement.getTypeParameters(), declaredType.getTypeArguments())
        .map(p -> new TypeParameterDeclaration(p.fst.asType(), p.snd))
        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
  }

  FieldDeclaration newFieldDeclaration(
      VariableElement fieldElement, List<TypeParameterDeclaration> typeParameterDeclarations) {
    assertNotNull(fieldElement, typeParameterDeclarations);
    assertTrue(
        fieldElement.getKind() == ElementKind.FIELD
            || fieldElement.getKind() == ElementKind.ENUM_CONSTANT,
        fieldElement.getKind().toString());
    TypeMirror fieldType = resolveTypeParameter(fieldElement.asType(), typeParameterDeclarations);
    TypeDeclaration typeDeclaration = newTypeDeclaration(fieldType);
    return new FieldDeclaration(fieldElement, typeDeclaration);
  }

  ConstructorDeclaration newConstructorDeclaration(ExecutableElement constructorElement) {
    assertNotNull(constructorElement);
    assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
    return new ConstructorDeclaration(constructorElement);
  }

  MethodDeclaration newMethodDeclaration(
      ExecutableElement methodElement, List<TypeParameterDeclaration> typeParameterDeclarations) {
    assertNotNull(methodElement, typeParameterDeclarations);
    assertTrue(methodElement.getKind() == ElementKind.METHOD);
    TypeMirror returnTypeMirror =
        resolveTypeParameter(methodElement.getReturnType(), typeParameterDeclarations);
    TypeDeclaration returnTypeDeclaration = newTypeDeclaration(returnTypeMirror);
    return new MethodDeclaration(methodElement, returnTypeDeclaration);
  }

  public TypeParameterDeclaration newTypeParameterDeclarationUsingTypeParams(
      TypeMirror formalType, List<TypeParameterDeclaration> typeParameterDeclarations) {
    TypeMirror actualType = resolveTypeParameter(formalType, typeParameterDeclarations);
    return new TypeParameterDeclaration(formalType, actualType);
  }

  private TypeMirror resolveTypeParameter(
      TypeMirror formalType, List<TypeParameterDeclaration> typeParameterDeclarations) {
    for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.formalType())) {
        return typeParameterDecl.actualType();
      }
      DeclaredType declaredType = ctx.getMoreTypes().toDeclaredType(formalType);
      if (declaredType == null) {
        continue;
      }
      if (declaredType.getTypeArguments().isEmpty()) {
        continue;
      }
      List<Optional<TypeMirror>> optTypeArgs =
          declaredType.getTypeArguments().stream()
              .map(
                  arg ->
                      typeParameterDeclarations.stream()
                          .filter(declaration -> arg.equals(declaration.formalType()))
                          .map(TypeParameterDeclaration::actualType)
                          .findFirst())
              .toList();
      if (optTypeArgs.stream().allMatch(Optional::isPresent)) {
        TypeMirror[] typeArgs =
            optTypeArgs.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(TypeMirror[]::new);
        TypeElement typeElement = ctx.getMoreElements().toTypeElement(declaredType.asElement());
        if (typeElement == null) {
          continue;
        }
        return ctx.getMoreTypes().getDeclaredType(typeElement, typeArgs);
      }
    }
    return formalType;
  }
}
