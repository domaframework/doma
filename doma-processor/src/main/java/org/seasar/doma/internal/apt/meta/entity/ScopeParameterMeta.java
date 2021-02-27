package org.seasar.doma.internal.apt.meta.entity;

import java.util.Objects;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor8;

public class ScopeParameterMeta implements CharSequence {

  private final VariableElement parameter;
  private final TypeMirror type;
  private final String typeAndName;

  public ScopeParameterMeta(VariableElement parameter, TypeMirror type, boolean isVarArgs) {
    this.parameter = Objects.requireNonNull(parameter);
    this.type = Objects.requireNonNull(type);
    TypeMirror componentType = isVarArgs ? getComponentType(type) : null;
    if (componentType == null) {
      typeAndName = type + " " + parameter.getSimpleName();
    } else {
      typeAndName = componentType + "... " + parameter.getSimpleName();
    }
  }

  private TypeMirror getComponentType(TypeMirror type) {
    return type.accept(
        new SimpleTypeVisitor8<TypeMirror, Void>() {
          @Override
          public TypeMirror visitArray(ArrayType t, Void unused) {
            return t.getComponentType();
          }
        },
        null);
  }

  public Name getName() {
    return parameter.getSimpleName();
  }

  public TypeMirror getType() {
    return type;
  }

  @Override
  public int length() {
    return typeAndName.length();
  }

  @Override
  public char charAt(int index) {
    return typeAndName.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return typeAndName.subSequence(start, end);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return typeAndName;
  }
}
