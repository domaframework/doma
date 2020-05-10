package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;

public class DeleteDeclaration {

  private final DeleteContext context;

  public DeleteDeclaration(DeleteContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public DeleteContext getContext() {
    return context;
  }

  public void where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    WhereDeclaration declaration = new WhereDeclaration(context);
    block.accept(declaration);
  }
}
