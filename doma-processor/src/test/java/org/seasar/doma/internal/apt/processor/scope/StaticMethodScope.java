package org.seasar.doma.internal.apt.processor.scope;

import java.util.function.Consumer;
import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

public class StaticMethodScope {

  @Scope
  static Consumer<WhereDeclaration> test(StaticMethod_ e) {
    return w -> {};
  }
}
