package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys
    extends AbstractStatement<NativeSqlUpsertTerminal, Integer> {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(
      Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
    this.declaration.getContext().duplicateKeyType = Optional.of(DuplicateKeyType.IGNORE);
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement. if no keys are specified, the
   * primary keys are used for duplicate checking.
   *
   * @param keys keys the keys used for duplicate checking
   * @return the
   */
  public NativeSqlUpsertTerminal keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    InsertContext context = this.declaration.getContext();
    context.upsertKeys = Arrays.asList(keys);
    this.declaration.getContext().upsertKeys = Arrays.asList(keys);
    return new NativeSqlUpsertTerminal(config, declaration);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlUpsertTerminal statement = new NativeSqlUpsertTerminal(config, declaration);
    return statement.createCommand();
  }
}
