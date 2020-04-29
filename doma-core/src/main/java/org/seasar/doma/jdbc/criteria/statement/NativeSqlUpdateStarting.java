package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;

public class NativeSqlUpdateStarting<ELEMENT> {

  private final UpdateDeclaration declaration;

  public NativeSqlUpdateStarting(UpdateDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  public NativeSqlUpdateTerminal<ELEMENT> set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.set(block);
    return new NativeSqlUpdateTerminal<>(declaration);
  }
}
