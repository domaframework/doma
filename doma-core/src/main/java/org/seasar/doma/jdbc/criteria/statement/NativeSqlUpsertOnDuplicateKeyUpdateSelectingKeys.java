package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.message.Message;

public class NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys {
  private final Config config;
  private final InsertDeclaration declaration;

  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingKeys(
      Config config, InsertDeclaration declaration) {
    Objects.requireNonNull(config);
    this.config = config;
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
    this.declaration.getContext().duplicateKeyType = Optional.of(DuplicateKeyType.UPDATE);
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement.
   *
   * @param keys keys the keys used for duplicate checking
   * @return the
   */
  public NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    if (keys.length == 0) {
      throw new DomaException(Message.DOMA6012);
    }
    this.declaration.getContext().upsertKeys = Arrays.asList(keys);
    return new NativeSqlUpsertOnDuplicateKeyUpdateSelectingSet(config, declaration);
  }
}
