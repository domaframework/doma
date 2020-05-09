package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
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
import org.seasar.doma.jdbc.query.SelectQuery;

public class NativeSql {

  protected final Config config;

  public NativeSql(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  public <ENTITY> NativeSqlSelectStarting<ENTITY> from(EntityDef<ENTITY> entityDef) {
    return from(entityDef, (options) -> {});
  }

  public <ENTITY> NativeSqlSelectStarting<ENTITY> from(
      EntityDef<ENTITY> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    SelectContext context = new SelectContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    Function<SelectQuery, ObjectProvider<ENTITY>> factory =
        query -> new EntityProvider<>(entityDef.asType(), query, false);
    return new NativeSqlSelectStarting<>(config, declaration, entityDef, factory);
  }

  public <ENTITY> NativeSqlUpdateStarting update(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.update(entityDef, options -> {});
  }

  public <ENTITY> NativeSqlUpdateStarting update(
      EntityDef<ENTITY> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    UpdateContext context = new UpdateContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }

  public <ENTITY> NativeSqlDeleteStarting delete(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.delete(entityDef, options -> {});
  }

  public <ENTITY> NativeSqlDeleteStarting delete(
      EntityDef<ENTITY> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    DeleteContext context = new DeleteContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }

  public <ENTITY> NativeSqlInsertStarting insert(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.insert(entityDef, options -> {});
  }

  public <ENTITY> NativeSqlInsertStarting insert(
      EntityDef<ENTITY> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    InsertContext context = new InsertContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
