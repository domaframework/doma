package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class ConstructorDeclaration {

  protected ExecutableElement element;

  protected List<TypeParameterDeclaration> typeParameterDeclarations;

  protected ProcessingEnvironment env;

  protected ConstructorDeclaration() {}

  public ExecutableElement getElement() {
    return element;
  }

  public TypeDeclaration getTypeDeclaration() {
    TypeMirror returnType = resolveTypeParameter(element.asType());
    return TypeDeclaration.newTypeDeclaration(returnType, env);
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
      ProcessingEnvironment env) {
    assertNotNull(constructorElement, typeParameterDeclarations, env);
    assertTrue(constructorElement.getKind() == ElementKind.CONSTRUCTOR);
    ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
    constructorDeclaration.element = constructorElement;
    constructorDeclaration.typeParameterDeclarations = typeParameterDeclarations;
    constructorDeclaration.env = env;
    return constructorDeclaration;
  }
}
