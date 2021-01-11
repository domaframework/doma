package org.seasar.doma.internal.apt.annot;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;

public class ScopeClass {
  final TypeMirror type;

  public ScopeClass(TypeMirror type) {
    this.type = type;
  }

  public ClassName className() {
    String binaryName = ClassNames.normalizeBinaryName(type.toString());
    return new ClassName(binaryName);
  }

  public TypeMirror asType() {
    return type;
  }

  @Override
  public String toString() {
    return className().toString();
  }
}
