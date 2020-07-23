package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;

public class UpdateDeclaration {

  private final UpdateContext context;

  public UpdateDeclaration(UpdateContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public UpdateContext getContext() {
    return context;
  }

  public void set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    SetDeclaration declaration = new SetDeclaration(context);
    block.accept(declaration);
  }

  public void where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    WhereDeclaration declaration = new WhereDeclaration(context);
    block.accept(declaration);
  }
}
