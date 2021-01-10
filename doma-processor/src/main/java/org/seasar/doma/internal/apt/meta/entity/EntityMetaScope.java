package org.seasar.doma.internal.apt.meta.entity;

import org.seasar.doma.internal.apt.annot.ScopeClass;

public class EntityMetaScope {
  final ScopeClass scopeClass;

  public EntityMetaScope(ScopeClass scopeClass) {
    this.scopeClass = scopeClass;
  }

  public ScopeClass scopeClass() {
    return scopeClass;
  }

  public String scopeField() {
    String name = scopeClass().className().getSimpleName();
    return "_scope_" + name;
  }
}
