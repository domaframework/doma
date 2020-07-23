package org.seasar.doma.jdbc.criteria.declaration;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class InsertSelectDeclaration {

  public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    return new SubSelectFromDeclaration<>(entityMetamodel);
  }
}
