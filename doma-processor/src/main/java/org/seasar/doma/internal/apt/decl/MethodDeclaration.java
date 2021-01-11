package org.seasar.doma.internal.apt.decl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

public class MethodDeclaration {

  private final ExecutableElement element;

  private final TypeDeclaration returnTypeDeclaration;

  MethodDeclaration(ExecutableElement element, TypeDeclaration returnTypeDeclaration) {
    assertNotNull(element, returnTypeDeclaration);
    this.element = element;
    this.returnTypeDeclaration = returnTypeDeclaration;
  }

  public ExecutableElement getElement() {
    return element;
  }

  public TypeDeclaration getReturnTypeDeclaration() {
    return returnTypeDeclaration;
  }

  public boolean isStatic() {
    return element.getModifiers().contains(Modifier.STATIC);
  }

  public String name() {
    return element.getSimpleName().toString();
  }

  public List<? extends VariableElement> parameters() {
    return element.getParameters();
  }

  public boolean isVarArgs() {
    return element.isVarArgs();
  }
}
