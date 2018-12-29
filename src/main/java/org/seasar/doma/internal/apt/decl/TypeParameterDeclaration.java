package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

public class TypeParameterDeclaration {

  protected TypeMirror formalType;

  protected TypeMirror actualType;

  protected ProcessingEnvironment env;

  protected TypeParameterDeclaration() {}

  public TypeMirror getFormalType() {
    return formalType;
  }

  public TypeMirror getActualType() {
    return actualType;
  }

  public static TypeParameterDeclaration newInstance(
      TypeMirror formalType, TypeMirror actualType, ProcessingEnvironment env) {
    assertNotNull(formalType, actualType, env);
    TypeParameterDeclaration typeParameterDeclaration = new TypeParameterDeclaration();
    typeParameterDeclaration.formalType = formalType;
    typeParameterDeclaration.actualType = actualType;
    typeParameterDeclaration.env = env;
    return typeParameterDeclaration;
  }
}
