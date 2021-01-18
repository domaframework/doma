package org.seasar.doma.internal.apt.annot;

import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.decl.MethodDeclaration;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;

import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ScopeClass {
  final TypeDeclaration type;

  public ScopeClass(TypeDeclaration type) {
    this.type = type;
  }

  public ClassName className() {
    String binaryName = ClassNames.normalizeBinaryName(type.toString());
    return new ClassName(binaryName);
  }

  public TypeMirror asType() {
    return type.getType();
  }

  public List<MethodDeclaration> scopeMethods(ClassName metamodelName) {
    return type.getScopeMethods(metamodelName);
  }

  public String scopeField() {
    String name = className().toString().replace(".", "_");
    return "__scope__" + name;
  }

  @Override
  public String toString() {
    return className().toString();
  }
}
