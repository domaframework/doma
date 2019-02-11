package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

public interface CtType {

  TypeMirror getType();

  String getQualifiedName();

  boolean isPrimitive();

  boolean isEnum();

  boolean isNone();

  boolean isWildcard();

  boolean isTypevar();

  boolean isArray();

  boolean isSameType(CtType ctType);

  <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH;
}
