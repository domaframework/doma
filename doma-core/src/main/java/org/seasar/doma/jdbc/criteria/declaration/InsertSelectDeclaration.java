package org.seasar.doma.jdbc.criteria.declaration;

import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class InsertSelectDeclaration {

  public SubSelectFromDeclaration from(EntityMetamodel<?> entityMetamodel) {
    return new SubSelectFromDeclaration(entityMetamodel);
  }
}
