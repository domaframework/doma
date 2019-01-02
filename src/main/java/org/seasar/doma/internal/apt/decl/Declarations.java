package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.*;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class Declarations {

  private final Context ctx;

  public Declarations(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public TypeDeclaration newUnknownTypeDeclaration() {
    TypeMirror type = ctx.getTypes().getNoType(TypeKind.NONE);
    return newTypeDeclaration(type);
  }

  public TypeDeclaration newBooleanTypeDeclaration() {
    TypeMirror type = ctx.getTypes().getTypeMirror(boolean.class);
    return newTypeDeclaration(type);
  }

  public TypeDeclaration newTypeDeclaration(Class<?> clazz) {
    assertNotNull(clazz);
    TypeMirror type = ctx.getTypes().getTypeMirror(clazz);
    return newTypeDeclaration(type);
  }

  public TypeDeclaration newTypeDeclaration(TypeMirror type) {
    assertNotNull(type);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    Map<String, List<TypeParameterDeclaration>> map = new LinkedHashMap<>();
    gatherTypeParameterDeclarations(type, map);
    return new TypeDeclaration(ctx, type, typeElement, map);
  }

  private void gatherTypeParameterDeclarations(
      TypeMirror type, Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return;
    }
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    typeParameterDeclarationsMap.put(
        binaryName, createTypeParameterDeclarations(typeElement, type));
    for (TypeMirror superType : ctx.getTypes().directSupertypes(type)) {
      TypeElement superElement = ctx.getTypes().toTypeElement(superType);
      if (superElement == null) {
        continue;
      }
      String superBinaryName = ctx.getElements().getBinaryNameAsString(superElement);
      if (typeParameterDeclarationsMap.containsKey(superBinaryName)) {
        continue;
      }
      typeParameterDeclarationsMap.put(
          superBinaryName, createTypeParameterDeclarations(superElement, superType));
      gatherTypeParameterDeclarations(superType, typeParameterDeclarationsMap);
    }
  }

  private List<TypeParameterDeclaration> createTypeParameterDeclarations(
      TypeElement typeElement, TypeMirror type) {
    assertNotNull(typeElement, type);
    List<TypeParameterDeclaration> list = new ArrayList<>();
    Iterator<? extends TypeParameterElement> formalParams =
        typeElement.getTypeParameters().iterator();
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    Iterator<? extends TypeMirror> actualParams = declaredType.getTypeArguments().iterator();
    for (; formalParams.hasNext() && actualParams.hasNext(); ) {
      TypeMirror formalType = formalParams.next().asType();
      TypeMirror actualType = actualParams.next();
      TypeParameterDeclaration typeParameterDeclaration =
          ctx.getDeclarations().newTypeParameterDeclaration(formalType, actualType);
      list.add(typeParameterDeclaration);
    }
    return Collections.unmodifiableList(list);
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

  ConstructorDeclaration newConstructorDeclaration(
      ExecutableElement constructorElement,
      List<TypeParameterDeclaration> typeParameterDeclarations) {
    assertNotNull(constructorElement, typeParameterDeclarations);
    assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
    ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(constructorElement);
    return constructorDeclaration;
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

  private TypeParameterDeclaration newTypeParameterDeclaration(
      TypeMirror formalType, TypeMirror actualType) {
    assertNotNull(formalType, actualType);
    TypeParameterDeclaration typeParameterDeclaration =
        new TypeParameterDeclaration(formalType, actualType);
    return typeParameterDeclaration;
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
