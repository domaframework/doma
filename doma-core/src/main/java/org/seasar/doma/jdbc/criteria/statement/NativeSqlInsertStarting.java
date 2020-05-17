package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertSelectDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.ValuesDeclaration;

public class NativeSqlInsertStarting {

  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlInsertStarting(Config config, InsertDeclaration declaration) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(declaration);
    this.config = config;
    this.declaration = declaration;
  }

  public NativeSqlInsertTerminal values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.values(block);
    return new NativeSqlInsertTerminal(config, declaration);
  }

  public NativeSqlInsertTerminal select(
      Function<InsertSelectDeclaration, SubSelectContext<?>> block) {
    Objects.requireNonNull(block);
    declaration.select(block);
    return new NativeSqlInsertTerminal(config, declaration);
  }
}
