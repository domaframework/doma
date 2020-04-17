package org.seasar.doma.internal.apt.decl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.util.Zip;

public class Declarations {

  private final Context ctx;

  public Declarations(Context ctx) {
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

  private TypeMirror resolveTypeParameter(
      TypeMirror formalType, List<TypeParameterDeclaration> typeParameterDeclarations) {
    for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.getFormalType())) {
        return typeParameterDecl.getActualType();
      }
    }
    return formalType;
  }
}
