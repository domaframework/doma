package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.type.TypeMirror;

/** @author taedium */
public interface CtType {

  TypeMirror getType();

  String getTypeName();

  String getQualifiedName();

  boolean isRawType();

  boolean hasWildcardType();

  boolean hasTypevarType();

  <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH;
}
