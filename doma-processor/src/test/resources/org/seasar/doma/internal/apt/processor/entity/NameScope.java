package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import java.util.function.Consumer;

class NameScope {

  public Consumer<WhereDeclaration> nameEq(MultiScopeEntity_ e, String name) {
    return w -> {
      w.eq(e.name, name);
    };
  }
}