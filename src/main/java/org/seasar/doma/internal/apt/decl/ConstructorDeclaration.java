package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class ConstructorDeclaration {

  protected ExecutableElement element;

  protected List<TypeParameterDeclaration> typeParameterDeclarations;

  protected Context ctx;

  protected ConstructorDeclaration() {}

  public ExecutableElement getElement() {
    return element;
  }

  public TypeDeclaration getTypeDeclaration() {
    TypeMirror returnType = resolveTypeParameter(element.asType());
    return TypeDeclaration.newTypeDeclaration(returnType, ctx);
  }

  protected TypeMirror resolveTypeParameter(TypeMirror formalType) {
    for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.getFormalType())) {
        return typeParameterDecl.getActualType();
      }
    }
    return formalType;
  }

  public static ConstructorDeclaration newInstance(
      ExecutableElement constructorElement,
      List<TypeParameterDeclaration> typeParameterDeclarations,
      Context ctx) {
    assertNotNull(constructorElement, typeParameterDeclarations, ctx);
    assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
    ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
    constructorDeclaration.element = constructorElement;
    constructorDeclaration.typeParameterDeclarations = typeParameterDeclarations;
    constructorDeclaration.ctx = ctx;
    return constructorDeclaration;
  }
}
