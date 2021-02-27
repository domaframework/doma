package org.seasar.doma.internal.apt.meta.entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

public class ScopeMethodMeta {

  private final ExecutableElement method;
  private final List<ScopeParameterMeta> parameters;

  public ScopeMethodMeta(ExecutableElement method, List<ScopeParameterMeta> parameters) {
    this.method = Objects.requireNonNull(method);
    this.parameters = Collections.unmodifiableList(Objects.requireNonNull(parameters));
  }

  public List<? extends TypeParameterElement> getTypeParameters() {
    return method.getTypeParameters();
  }

  public TypeMirror getReturnType() {
    return method.getReturnType();
  }

  public Name getName() {
    return method.getSimpleName();
  }

  public List<ScopeParameterMeta> getParameters() {
    return parameters;
  }

  public Set<Modifier> getModifiers() {
    return method.getModifiers();
  }
}
