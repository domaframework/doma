package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class MethodDeclaration {

  protected ExecutableElement element;

  protected List<TypeParameterDeclaration> typeParameterDeclarations;

  protected Context ctx;

  protected MethodDeclaration() {}

  public ExecutableElement getElement() {
    return element;
  }

  public TypeDeclaration getReturnTypeDeclaration() {
    TypeMirror returnType = resolveTypeParameter(element.getReturnType());
    return TypeDeclaration.newTypeDeclaration(returnType, ctx);
  }

  public boolean isStatic() {
    return element.getModifiers().contains(Modifier.STATIC);
  }

  protected TypeMirror resolveTypeParameter(TypeMirror formalType) {
    for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.getFormalType())) {
        return typeParameterDecl.getActualType();
      }
    }
    return formalType;
  }

  public static MethodDeclaration newInstance(
      ExecutableElement methodElement,
      List<TypeParameterDeclaration> typeParameterDeclarations,
      Context ctx) {
    assertNotNull(methodElement, typeParameterDeclarations, ctx);
    assertTrue(methodElement.getKind() == ElementKind.METHOD);
    MethodDeclaration methodDeclaration = new MethodDeclaration();
    methodDeclaration.element = methodElement;
    methodDeclaration.typeParameterDeclarations = typeParameterDeclarations;
    methodDeclaration.ctx = ctx;
    return methodDeclaration;
  }
}
