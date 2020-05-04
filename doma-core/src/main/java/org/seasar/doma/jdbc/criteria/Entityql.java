package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStatement;

public final class Entityql {

  public static <ENTITY> EntityqlSelectStatement<ENTITY> from(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    SelectContext context = new SelectContext(entityDef);
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new EntityqlSelectStatement<>(declaration);
  }
}
