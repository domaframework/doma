package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public record WithContext(
    EntityMetamodel<?> entityMetamodel, SetOperationContext<?> setOperationContext) {
  public WithContext(
      EntityMetamodel<?> entityMetamodel, SetOperationContext<?> setOperationContext) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.setOperationContext = Objects.requireNonNull(setOperationContext);
  }
}
