package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class FieldDeclaration {

  protected VariableElement element;

  protected List<TypeParameterDeclaration> typeParameterDeclarations;

  protected ProcessingEnvironment env;

  public VariableElement getElement() {
    return element;
  }

  public TypeDeclaration getTypeDeclaration() {
    TypeMirror fieldType = resolveTypeParameter(element.asType());
    return TypeDeclaration.newTypeDeclaration(fieldType, env);
  }

  protected TypeMirror resolveTypeParameter(TypeMirror formalType) {
    for (TypeParameterDeclaration typeParameterDecl : typeParameterDeclarations) {
      if (formalType.equals(typeParameterDecl.getFormalType())) {
        return typeParameterDecl.getActualType();
      }
    }
    return formalType;
  }

  public static FieldDeclaration newInstance(
      VariableElement fieldElement,
      List<TypeParameterDeclaration> typeParameterDeclarations,
      ProcessingEnvironment env) {
    assertNotNull(fieldElement, typeParameterDeclarations, env);
    assertTrue(
        fieldElement.getKind() == ElementKind.FIELD
            || fieldElement.getKind() == ElementKind.ENUM_CONSTANT,
        fieldElement.getKind().toString());
    FieldDeclaration fieldDeclaration = new FieldDeclaration();
    fieldDeclaration.element = fieldElement;
    fieldDeclaration.typeParameterDeclarations = typeParameterDeclarations;
    fieldDeclaration.env = env;
    return fieldDeclaration;
  }
}
