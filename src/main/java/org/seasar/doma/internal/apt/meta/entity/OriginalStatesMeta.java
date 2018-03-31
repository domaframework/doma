package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/** @author taedium */
public class OriginalStatesMeta {

  private final TypeElement typeElement;

  private final VariableElement fieldElement;

  private final TypeElement fieldEnclosingElement;

  public OriginalStatesMeta(
      TypeElement typeElement, VariableElement fieldElement, TypeElement fieldEnclosingElement) {
    assertNotNull(typeElement, fieldElement, fieldEnclosingElement);
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
