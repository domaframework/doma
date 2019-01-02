package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public interface CtType {

  TypeMirror getType();

  TypeElement getTypeElement();

  String getTypeName();

  String getBoxedTypeName();

  String getMetaTypeName();

  String getQualifiedName();

  String getPackageName();

  boolean isPrimitive();

  boolean isEnum();

  boolean isNone();

  boolean isWildcard();

  boolean isTypevar();

  <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH;
}
