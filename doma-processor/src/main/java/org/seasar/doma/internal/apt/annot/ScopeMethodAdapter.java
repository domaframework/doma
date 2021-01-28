package org.seasar.doma.internal.apt.annot;

import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.decl.TypeParameterDeclaration;

public class ScopeMethodAdapter {
  final ExecutableElement method;
  final List<TypeParameterDeclaration> resolvedTypeParameters;

  public ScopeMethodAdapter(
      ExecutableElement method, List<TypeParameterDeclaration> resolvedTypeParameters) {
    this.method = method;
    this.resolvedTypeParameters = resolvedTypeParameters;
  }

  public List<? extends VariableElement> getParameters() {
    return method.getParameters();
  }

  public TypeMirror getReturnType() {
    return method.getReturnType();
  }

  public List<? extends TypeParameterElement> getTypeParameters() {
    return method.getTypeParameters();
  }

  public TypeMirror resolveParameter(VariableElement variable) {
    TypeMirror variableType = variable.asType();
    return resolvedTypeParameters.stream()
        .filter(it -> it.getFormalType().equals(variableType))
        .map(TypeParameterDeclaration::getActualType)
        .findFirst()
        .orElse(variableType);
  }

  public boolean isVarArgs() {
    return method.isVarArgs();
  }

  public String getMethodName() {
    return method.getSimpleName().toString();
  }

  public ExecutableElement toElement() {
    return method;
  }

  public Set<Modifier> getModifiers() {
    return method.getModifiers();
  }
}
