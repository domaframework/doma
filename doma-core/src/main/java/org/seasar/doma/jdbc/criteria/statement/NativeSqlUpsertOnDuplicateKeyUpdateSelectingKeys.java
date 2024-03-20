package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertOnDuplicateKeyUpdateSetValuesDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys
    extends AbstractStatement<NativeSqlUpsertTerminal, Integer> {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys(
      Config config, InsertDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
    InsertContext context = this.declaration.getContext();
    context.onDuplicateContext.duplicateKeyType = DuplicateKeyType.UPDATE;
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement. if no keys are specified, the
   * {@link org.seasar.doma.Id} property are used for duplicate checking.
   *
   * @param keys keys the keys used for duplicate checking
   * @return selecting set statement builder
   */
  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    InsertContext context = this.declaration.getContext();
    context.onDuplicateContext.keys = Arrays.asList(keys);
    return new NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(config, declaration);
  }

  /**
   * Specify the set clause for the UPSERT statement. if no set-value are specified, the insert
   * value that exclude {@link org.seasar.doma.Id} properties are used for set value.
   *
   * @param block the consumer to set the clause
   * @return terminal statement
   */
  public NativeSqlUpsertTerminal set(
      Consumer<InsertOnDuplicateKeyUpdateSetValuesDeclaration> block) {
    NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet statement =
        new NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(config, declaration);
    return statement.set(block);
  }

  @Override
  protected Command<Integer> createCommand() {
    NativeSqlUpsertTerminal statement = new NativeSqlUpsertTerminal(config, declaration);
    return statement.createCommand();
  }
}
