package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
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
      EntityDef<ENTITY> entityDef, Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(settingsConsumer);
    SelectContext context = new SelectContext(entityDef);
    settingsConsumer.accept(context.getSettings());
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
      EntityDef<ENTITY> entityDef, Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(settingsConsumer);
    UpdateContext context = new UpdateContext(entityDef);
    settingsConsumer.accept(context.getSettings());
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }

  public <ENTITY> NativeSqlDeleteStarting delete(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.delete(entityDef, options -> {});
  }

  public <ENTITY> NativeSqlDeleteStarting delete(
      EntityDef<ENTITY> entityDef, Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(settingsConsumer);
    DeleteContext context = new DeleteContext(entityDef);
    settingsConsumer.accept(context.getSettings());
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }

  public <ENTITY> NativeSqlInsertStarting insert(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return this.insert(entityDef, options -> {});
  }

  public <ENTITY> NativeSqlInsertStarting insert(
      EntityDef<ENTITY> entityDef, Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(settingsConsumer);
    InsertContext context = new InsertContext(entityDef);
    settingsConsumer.accept(context.getSettings());
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
