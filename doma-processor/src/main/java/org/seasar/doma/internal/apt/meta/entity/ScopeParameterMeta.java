package org.seasar.doma.internal.apt.meta.entity;

import java.util.Objects;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ScopeParameterMeta implements CharSequence {

  private final VariableElement parameter;
  private final TypeMirror type;
  private final String value;

  public ScopeParameterMeta(VariableElement parameter, TypeMirror type, TypeMirror componentType) {
    this.parameter = Objects.requireNonNull(parameter);
    this.type = Objects.requireNonNull(type);
    if (componentType == null) {
      value = type + " " + parameter.getSimpleName();
    } else {
      value = componentType + "... " + parameter.getSimpleName();
    }
  }

  public Name getName() {
    return parameter.getSimpleName();
  }

  public TypeMirror getType() {
    return type;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return value.subSequence(start, end);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return value;
  }
}
