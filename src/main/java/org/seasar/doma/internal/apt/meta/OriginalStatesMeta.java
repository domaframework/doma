package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/** @author taedium */
public class OriginalStatesMeta {

  protected final TypeElement typeElement;

  protected final VariableElement fieldElement;

  protected final TypeElement fieldEnclosingElement;

  public OriginalStatesMeta(
      TypeElement typeElement,
      VariableElement fieldElement,
      TypeElement fieldEnclosingElement,
      ProcessingEnvironment env) {
    assertNotNull(typeElement, fieldElement, fieldEnclosingElement, env);
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
