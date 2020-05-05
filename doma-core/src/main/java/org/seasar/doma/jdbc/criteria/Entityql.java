package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.EntityqlDeleteStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlInsertStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlUpdateStatement;

public final class Entityql {

  public static <ENTITY> EntityqlSelectStatement<ENTITY> from(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    SelectContext context = new SelectContext(entityDef);
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new EntityqlSelectStatement<>(declaration);
  }

  public static <ENTITY> EntityqlUpdateStatement<ENTITY> update(
      EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return new EntityqlUpdateStatement<>(entityDef, entity);
  }

  public static final class delete {
    public static <ENTITY> EntityqlDeleteStatement<ENTITY> from(
        EntityDef<ENTITY> entityDef, ENTITY entity) {
      Objects.requireNonNull(entityDef);
      Objects.requireNonNull(entity);
      return new EntityqlDeleteStatement<>(entityDef, entity);
    }
  }

  public static final class insert {
    public static <ENTITY> EntityqlInsertStatement<ENTITY> into(
        EntityDef<ENTITY> entityDef, ENTITY entity) {
      Objects.requireNonNull(entityDef);
      Objects.requireNonNull(entityDef);
      Objects.requireNonNull(entity);
      return new EntityqlInsertStatement<>(entityDef, entity);
    }
  }
}
