package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Arrays;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class DuplicateKeyIgnoreDeclaration {

  private final InsertContext context;

  public DuplicateKeyIgnoreDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
    this.context.onDuplicateContext.duplicateKeyType = DuplicateKeyType.IGNORE;
  }

  public void keys(PropertyMetamodel<?>... keys) {
    context.onDuplicateContext.keys = Arrays.asList(keys);
  }
}
