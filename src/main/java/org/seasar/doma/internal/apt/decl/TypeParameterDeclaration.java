package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class TypeParameterDeclaration {

  protected TypeMirror formalType;

  protected TypeMirror actualType;

  protected Context ctx;

  protected TypeParameterDeclaration() {}

  public TypeMirror getFormalType() {
    return formalType;
  }

  public TypeMirror getActualType() {
    return actualType;
  }

  public static TypeParameterDeclaration newInstance(
      TypeMirror formalType, TypeMirror actualType, Context ctx) {
    assertNotNull(formalType, actualType, ctx);
    TypeParameterDeclaration typeParameterDeclaration = new TypeParameterDeclaration();
    typeParameterDeclaration.formalType = formalType;
    typeParameterDeclaration.actualType = actualType;
    typeParameterDeclaration.ctx = ctx;
    return typeParameterDeclaration;
  }
}
