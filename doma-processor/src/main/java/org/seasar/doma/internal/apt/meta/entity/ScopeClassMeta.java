package org.seasar.doma.internal.apt.meta.entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.lang.model.element.TypeElement;

public class ScopeClassMeta {

  private final TypeElement typeElement;
  private final List<ScopeMethodMeta> methods;
  private final String identifier;

  public ScopeClassMeta(TypeElement typeElement, List<ScopeMethodMeta> methods) {
    this.typeElement = Objects.requireNonNull(typeElement);
    this.methods = Collections.unmodifiableList(Objects.requireNonNull(methods));
    this.identifier = "__scope__" + typeElement.getQualifiedName().toString().replace(".", "_");
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public List<ScopeMethodMeta> getMethods() {
    return methods;
  }

  public String getIdentifier() {
    return identifier;
  }
}
