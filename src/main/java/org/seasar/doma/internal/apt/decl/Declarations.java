package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.util.Zip;

public class Declarations {

  private final Context ctx;

  public Declarations(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public TypeDeclaration newTypeDeclaration(Class<?> clazz) {
    assertNotNull(clazz);
    return newTypeDeclaration(ctx.getTypes().getType(clazz));
  }

  public TypeDeclaration newTypeDeclaration(TypeMirror type) {
    assertNotNull(type);
    var typeParameterDeclarations = createTypeParameterDeclarations(type);
    var supertypeDeclarations =
        ctx.getTypes()
            .supertypes(type)
            .stream()
            .map(this::newTypeDeclaration)
            .collect(Collectors.toList());
    return new TypeDeclaration(ctx, type, typeParameterDeclarations, supertypeDeclarations);
  }

  public TypeDeclaration newUnknownTypeDeclaration() {
    TypeMirror type = ctx.getTypes().getNoType(TypeKind.NONE);
    return new TypeDeclaration(ctx, type, Collections.emptyList(), Collections.emptyList());
  }

  public TypeDeclaration newBooleanTypeDeclaration() {
    var type = ctx.getTypes().getType(boolean.class);
    return newTypeDeclaration(type);
  }

  public List<TypeParameterDeclaration> createTypeParameterDeclarations(TypeMirror type) {
    assertNotNull(type);
    var typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return Collections.emptyList();
    }
    var formalParams = typeElement.getTypeParameters().stream().map(Element::asType);
    var declaredType = ctx.getTypes().toDeclaredType(type);
    Stream<TypeMirror> actualParams =
        declaredType.getTypeArguments().stream().map(Function.identity());
    return Zip.stream(formalParams, actualParams)
        .map(p -> newTypeParameterDeclaration(p.fst, p.snd))
        .collect(Collectors.toList());
  }

  public FieldDeclaration newFieldDeclaration(
      VariableElement fieldElement, List<TypeParameterDeclaration> typeParameterDeclarations) {
    assertNotNull(fieldElement, typeParameterDeclarations);
    assertTrue(
        fieldElement.getKind() == ElementKind.FIELD
            || fieldElement.getKind() == ElementKind.ENUM_CONSTANT,
        fieldElement.getKind().toString());
    var typeMirror = resolveTypeParameter(fieldElement.asType(), typeParameterDeclarations);
    var typeDeclaration = newTypeDeclaration(typeMirror);
    return new FieldDeclaration(fieldElement, typeDeclaration);
  }

  public ConstructorDeclaration newConstructorDeclaration(ExecutableElement constructorElement) {
    assertNotNull(constructorElement);
    assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
    return new ConstructorDeclaration(constructorElement);
  }

  public MethodDeclaration newMethodDeclaration(
      ExecutableElement methodElement, List<TypeParameterDeclaration> typeParameterDeclarations) {
    assertNotNull(methodElement, typeParameterDeclarations);
    assertTrue(methodElement.getKind() == ElementKind.METHOD);
    var returnTypeMirror =
        resolveTypeParameter(methodElement.getReturnType(), typeParameterDeclarations);
    var returnTypeDeclaration = newTypeDeclaration(returnTypeMirror);
    return new MethodDeclaration(methodElement, returnTypeDeclaration);
  }

  public TypeParameterDeclaration newTypeParameterDeclaration(
      TypeMirror formalType, TypeMirror actualType) {
    assertNotNull(formalType, actualType);
    return new TypeParameterDeclaration(formalType, actualType);
  }

  private TypeMirror resolveTypeParameter(
      TypeMirror formalType, List<TypeParameterDeclaration> typeParameterDeclarations) {
    for (var typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.getFormalType())) {
        return typeParameterDecl.getActualType();
      }
    }
    return formalType;
  }
}
