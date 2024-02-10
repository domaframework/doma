package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class DuplicateKeyUpdateDeclaration {

  private final InsertContext context;

  public DuplicateKeyUpdateDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
    this.context.duplicateKeyType = Optional.of(DuplicateKeyType.UPDATE);
  }

  public void keys(PropertyMetamodel<?>... keys) {
    context.upsertKeys = Arrays.asList(keys);
  }

  public void set(PropertyMetamodel<?>... setPropertyMetamodels) {
    context.upsertSetPropertyMetamodels = Arrays.asList(setPropertyMetamodels);
  }
}
