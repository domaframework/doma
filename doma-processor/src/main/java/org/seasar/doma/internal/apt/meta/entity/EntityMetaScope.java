package org.seasar.doma.internal.apt.meta.entity;

import org.seasar.doma.internal.ClassName;

public class EntityMetaScope {
  final ClassName scopeClass;

  public EntityMetaScope(ClassName scopeClass) {
    this.scopeClass = scopeClass;
  }

  public ClassName scopeClass() {
    return scopeClass;
  }

  public String scopeField() {
    String name = scopeClass.getSimpleName();
    return "_scope_" + name;
  }
}
