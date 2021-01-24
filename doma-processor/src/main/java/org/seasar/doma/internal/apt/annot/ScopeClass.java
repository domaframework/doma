package org.seasar.doma.internal.apt.annot;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;

public class ScopeClass {
  final TypeDeclaration type;
  final List<ExecutableElement> methods;

  public ScopeClass(TypeDeclaration type, List<ExecutableElement> methods) {
    this.type = type;
    this.methods = methods;
  }

  public ClassName className() {
    String binaryName = ClassNames.normalizeBinaryName(type.toString());
    return new ClassName(binaryName);
  }

  public TypeMirror asType() {
    return type.getType();
  }

  public List<ExecutableElement> scopeMethods() {
    return methods;
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
