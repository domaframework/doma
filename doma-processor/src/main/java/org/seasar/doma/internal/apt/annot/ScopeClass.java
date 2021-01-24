package org.seasar.doma.internal.apt.annot;

import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
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

  public List<ExecutableElement> scopeMethods(ClassName metamodelName) {
    return methods.stream()
        .filter(m -> isScopeMethod(m, metamodelName))
        .collect(Collectors.toList());
  }

  public String scopeField() {
    String name = className().toString().replace(".", "_");
    return "__scope__" + name;
  }

  private boolean isScopeMethod(ExecutableElement m, ClassName metamodel) {
    if (m.getModifiers().contains(Modifier.STATIC)) {
      return false;
    }

    if (!m.getModifiers().contains(Modifier.PUBLIC)) {
      return false;
    }

    if (m.getReturnType().getKind() == TypeKind.VOID) {
      return false;
    }

    if (m.getParameters().size() < 1) {
      return false;
    }

    VariableElement firstParameter = m.getParameters().get(0);
    // Note; Here, type checking cannot be performed correctly because it is before the Metamodel is
    // generated.
    return firstParameter.asType().toString().equals(metamodel.getSimpleName());
  }

  @Override
  public String toString() {
    return className().toString();
  }
}
