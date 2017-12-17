package org.seasar.doma.internal.apt.decl;

import javax.lang.model.element.VariableElement;

public class FieldDeclaration {

    private final VariableElement element;

    private final TypeDeclaration typeDeclaration;

    FieldDeclaration(VariableElement element, TypeDeclaration typeDeclaration) {
        this.element = element;
        this.typeDeclaration = typeDeclaration;
    }

    public VariableElement getElement() {
        return element;
    }

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

}
