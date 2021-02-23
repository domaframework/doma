package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import java.util.function.Consumer;

class NameScope {

  @Scope
  public Consumer<WhereDeclaration> nameEq(Multi_ e, String name) {
    return w -> w.eq(e.name, name);
  }

  public Consumer<WhereDeclaration> ignoredMethod(Multi_ e, String name) {
    return w -> w.eq(e.name, name);
  }
}