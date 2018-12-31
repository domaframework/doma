package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.Context;

public class OriginalStatesMeta {

  protected final TypeElement typeElement;

  protected final VariableElement fieldElement;

  protected final TypeElement fieldEnclosingElement;

  public OriginalStatesMeta(
      TypeElement typeElement,
      VariableElement fieldElement,
      TypeElement fieldEnclosingElement,
      Context ctx) {
    assertNotNull(typeElement, fieldElement, fieldEnclosingElement, ctx);
    this.typeElement = typeElement;
    this.fieldElement = fieldElement;
    this.fieldEnclosingElement = fieldEnclosingElement;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public VariableElement getFieldElement() {
    return fieldElement;
  }

  public TypeElement getFieldEnclosingElement() {
    return fieldEnclosingElement;
  }
}
