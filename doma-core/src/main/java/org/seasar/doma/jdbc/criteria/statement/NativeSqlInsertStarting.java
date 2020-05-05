package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertIntoDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.ValuesDeclaration;

public class NativeSqlInsertStarting {

  private final InsertIntoDeclaration declaration;

  public NativeSqlInsertStarting(InsertIntoDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public NativeSqlInsertTerminal values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.values(block);
    InsertContext context = declaration.getContext();
    return new NativeSqlInsertTerminal(context);
  }
}
