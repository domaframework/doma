package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.declaration.DeleteDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlDeleteStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlInsertStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlUpdateStarting;

public class NativeSql {

  protected final Config config;

  public NativeSql(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  public <ELEMENT> NativeSqlSelectStarting<ELEMENT> from(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    SelectContext context = new SelectContext(entityDef);
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new NativeSqlSelectStarting<>(config, declaration, entityDef);
  }

  public <ELEMENT> NativeSqlUpdateStarting update(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    UpdateContext context = new UpdateContext(entityDef);
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }

  public <ELEMENT> NativeSqlDeleteStarting delete(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    DeleteContext context = new DeleteContext(entityDef);
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }

  public <ELEMENT> NativeSqlInsertStarting insert(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    InsertContext context = new InsertContext(entityDef);
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
