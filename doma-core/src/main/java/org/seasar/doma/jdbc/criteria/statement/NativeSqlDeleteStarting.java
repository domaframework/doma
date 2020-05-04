package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.declaration.DeleteFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

public class NativeSqlDeleteStarting<ELEMENT> extends AbstractStatement<Integer>
    implements DeleteStatement {

  private final DeleteFromDeclaration declaration;

  public NativeSqlDeleteStarting(DeleteFromDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public NativeSqlDeleteTerminal<ELEMENT> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return new NativeSqlDeleteTerminal<>(declaration);
  }

  @Override
  protected Command<Integer> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    NativeSqlDeleteTerminal<ELEMENT> terminal = new NativeSqlDeleteTerminal<>(declaration);
    return terminal.createCommand(config, commenter, sqlLogType);
  }
}
