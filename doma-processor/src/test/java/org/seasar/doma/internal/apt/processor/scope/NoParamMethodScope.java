package org.seasar.doma.internal.apt.processor.scope;

import java.util.function.Consumer;
import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

public class NoParamMethodScope {
  @Scope
  public Consumer<WhereDeclaration> test() {
    return w -> {};
  }
}
