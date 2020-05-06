package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.declaration.DeleteDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

public class NativeSqlDeleteStarting extends AbstractStatement<Integer> {

  private final DeleteDeclaration declaration;

  public NativeSqlDeleteStarting(Config config, DeleteDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public Statement<Integer> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return new NativeSqlDeleteTerminal(config, declaration);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlDeleteTerminal terminal = new NativeSqlDeleteTerminal(config, declaration);
    return terminal.createCommand();
  }
}
