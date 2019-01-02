package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;

public class TypeParameterDeclaration {

  private final TypeMirror formalType;

  private final TypeMirror actualType;

  TypeParameterDeclaration(TypeMirror formalType, TypeMirror actualType) {
    assertNotNull(formalType, actualType);
    this.formalType = formalType;
    this.actualType = actualType;
  }

  public TypeMirror getFormalType() {
    return formalType;
  }

  public TypeMirror getActualType() {
    return actualType;
  }
}
