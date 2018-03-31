package org.seasar.doma.internal.apt.decl;

import javax.lang.model.element.ExecutableElement;

public class ConstructorDeclaration {

  private final ExecutableElement element;

  ConstructorDeclaration(ExecutableElement element) {
    this.element = element;
  }

  public ExecutableElement getElement() {
    return element;
  }
}
