package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class FieldDeclaration {

  protected VariableElement element;

  protected List<TypeParameterDeclaration> typeParameterDeclarations;

  protected Context ctx;

  public VariableElement getElement() {
    return element;
  }

  public TypeDeclaration getTypeDeclaration() {
    TypeMirror fieldType = resolveTypeParameter(element.asType());
    return TypeDeclaration.newTypeDeclaration(fieldType, ctx);
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
      Context ctx) {
    assertNotNull(fieldElement, typeParameterDeclarations, ctx);
    assertTrue(
        fieldElement.getKind() == ElementKind.FIELD
            || fieldElement.getKind() == ElementKind.ENUM_CONSTANT,
        fieldElement.getKind().toString());
    FieldDeclaration fieldDeclaration = new FieldDeclaration();
    fieldDeclaration.element = fieldElement;
    fieldDeclaration.typeParameterDeclarations = typeParameterDeclarations;
    fieldDeclaration.ctx = ctx;
    return fieldDeclaration;
  }
}
