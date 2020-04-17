package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;

public class ConstructorDeclaration {

  private final ExecutableElement element;

  ConstructorDeclaration(ExecutableElement element) {
    assertNotNull(element);
    this.element = element;
  }

  public ExecutableElement getElement() {
    return element;
  }
}
