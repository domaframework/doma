package org.seasar.doma.internal.apt.cttype;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public interface CtType {

  TypeMirror getTypeMirror();

  TypeElement getTypeElement();

  String getTypeName();

  String getBoxedTypeName();

  String getMetaTypeName();

  String getQualifiedName();

  String getPackageName();

  String getPackageExcludedBinaryName();

  boolean isPrimitive();

  boolean isEnum();

  <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH;
}
