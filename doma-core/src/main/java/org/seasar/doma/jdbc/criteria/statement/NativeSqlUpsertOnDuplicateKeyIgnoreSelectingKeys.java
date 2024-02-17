package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyIgnoreSelectingKeys(
      Config config, InsertDeclaration declaration) {
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
    this.declaration.getContext().duplicateKeyType = Optional.of(DuplicateKeyType.IGNORE);
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement.
   *
   * @param keys keys the keys used for duplicate checking
   * @return the
   */
  public NativeSqlUpsertTerminal keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    if (keys.length == 0) {
      throw new DomaIllegalArgumentException("keys", "keys are empty");
    }
    this.declaration.getContext().upsertKeys = Arrays.asList(keys);
    return new NativeSqlUpsertTerminal(config, declaration);
  }
}
