package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import java.util.function.Consumer;

class NameScope {

  @ScopeMethod
  public Consumer<WhereDeclaration> nameEq(MultiScopeEntity_ e, String name) {
    return w -> {
      w.eq(e.name, name);
    };
  }

  public Consumer<WhereDeclaration> ignoredMethod(MultiScopeEntity_ e, String name) {
    return w -> {
      w.eq(e.name, name);
    };
  }
}