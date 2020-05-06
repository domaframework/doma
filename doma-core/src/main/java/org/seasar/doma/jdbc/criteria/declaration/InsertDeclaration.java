package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.InsertContext;

public class InsertDeclaration {

  private final InsertContext context;

  public InsertDeclaration(InsertContext context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public InsertContext getContext() {
    return context;
  }

  public void values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    ValuesDeclaration declaration = new ValuesDeclaration(context);
    block.accept(declaration);
  }
}
