package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Options;
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
    return from(entityDef, (options) -> {});
  }

  public <ELEMENT> NativeSqlSelectStarting<ELEMENT> from(
      EntityDef<ELEMENT> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    SelectContext context = new SelectContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new NativeSqlSelectStarting<>(config, declaration, entityDef);
  }

  public <ELEMENT> NativeSqlUpdateStarting update(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.update(entityDef, options -> {});
  }

  public <ELEMENT> NativeSqlUpdateStarting update(
      EntityDef<ELEMENT> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    UpdateContext context = new UpdateContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }

  public <ELEMENT> NativeSqlDeleteStarting delete(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.delete(entityDef, options -> {});
  }

  public <ELEMENT> NativeSqlDeleteStarting delete(
      EntityDef<ELEMENT> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    DeleteContext context = new DeleteContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }

  public <ELEMENT> NativeSqlInsertStarting insert(EntityDef<ELEMENT> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.insert(entityDef, options -> {});
  }

  public <ELEMENT> NativeSqlInsertStarting insert(
      EntityDef<ELEMENT> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    InsertContext context = new InsertContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
