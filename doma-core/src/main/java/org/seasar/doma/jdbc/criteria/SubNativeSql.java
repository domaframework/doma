package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

/** Provides the ways to issue subquery SQL statements */
public class SubNativeSql {
  protected final Config config;

  public SubNativeSql(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return new SubSelectFromDeclaration<>(entityMetamodel);
  }

  public <ENTITY> SubSelectFromDeclaration<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(entityMetamodel);
    return new SubSelectFromDeclaration<>(entityMetamodel, subSelectContext);
  }
}
