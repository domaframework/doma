package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;

public class NativeSqlUpdateStarting {

  private final Config config;
  private final UpdateDeclaration declaration;

  public NativeSqlUpdateStarting(Config config, UpdateDeclaration declaration) {
    Objects.requireNonNull(config);
    Objects.requireNonNull(declaration);
    this.config = config;
    this.declaration = declaration;
  }

  public NativeSqlUpdateTerminal set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.set(block);
    return new NativeSqlUpdateTerminal(config, declaration);
  }
}
