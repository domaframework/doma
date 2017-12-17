package org.seasar.doma.internal.apt.decl;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class MethodDeclaration {

    private final ExecutableElement element;

    private final TypeDeclaration returnTypeDeclaration;

    MethodDeclaration(ExecutableElement element, TypeDeclaration returnTypeDeclaration) {
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

}
