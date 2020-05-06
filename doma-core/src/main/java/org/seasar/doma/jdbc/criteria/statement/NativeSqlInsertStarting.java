package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
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
}
