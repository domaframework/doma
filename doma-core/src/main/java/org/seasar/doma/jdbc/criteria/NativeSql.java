package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.declaration.DeleteFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertIntoDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlDeleteStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlInsertStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpdateStarting;

public final class NativeSql {

  public static <ELEMENT> NativeSqlSelectStarting<ELEMENT> from(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    SelectContext context = new SelectContext(entityDef);
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new NativeSqlSelectStarting<>(declaration, entityDef);
  }

  public static <ELEMENT> NativeSqlUpdateStarting update(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    UpdateContext context = new UpdateContext(entityDef);
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(declaration);
  }

  public static final class delete {
    public static <ELEMENT> NativeSqlDeleteStarting from(EntityDef<ELEMENT> entityDef) {
      Objects.requireNonNull(entityDef);
      DeleteContext context = new DeleteContext(entityDef);
      DeleteFromDeclaration declaration = new DeleteFromDeclaration(context);
      return new NativeSqlDeleteStarting(declaration);
    }
  }

  public static final class insert {
    public static <ELEMENT> NativeSqlInsertStarting into(EntityDef<ELEMENT> entityDef) {
      Objects.requireNonNull(entityDef);
      InsertContext context = new InsertContext(entityDef);
      InsertIntoDeclaration declaration = new InsertIntoDeclaration(context);
      return new NativeSqlInsertStarting(declaration);
    }
  }
}
