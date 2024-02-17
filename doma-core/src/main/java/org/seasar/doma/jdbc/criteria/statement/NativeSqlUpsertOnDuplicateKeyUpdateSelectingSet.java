package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpsertSetValuesDeclaration;

public class NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet
    extends AbstractStatement<NativeSqlUpsertTerminal, Integer> {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(
      Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  /**
   * Specify the set clause for the UPSERT statement. if no set-value are specified, the insert
   * value are used for set value.
   *
   * @param block the consumer to set the clause
   * @return terminal statement
   */
  public NativeSqlUpsertTerminal set(Consumer<UpsertSetValuesDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.upsertSetValues(block);
    return new NativeSqlUpsertTerminal(config, declaration);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlUpsertTerminal query = new NativeSqlUpsertTerminal(config, declaration);
    return query.createCommand();
  }
}
