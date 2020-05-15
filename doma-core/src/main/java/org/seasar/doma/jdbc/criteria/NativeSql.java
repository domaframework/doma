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
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
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

  public <ENTITY> NativeSqlSelectStarting<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    return from(entityMetamodel, (options) -> {});
  }

  public <ENTITY> NativeSqlSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    SelectContext context = new SelectContext(entityMetamodel);
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    Function<SelectQuery, ObjectProvider<ENTITY>> factory =
        query -> new EntityProvider<>(entityMetamodel.asType(), query, false);
    return new NativeSqlSelectStarting<>(config, declaration, entityMetamodel, factory);
  }

  public <ENTITY> NativeSqlUpdateStarting update(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return this.update(entityMetamodel, options -> {});
  }

  public <ENTITY> NativeSqlUpdateStarting update(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    UpdateContext context = new UpdateContext(entityMetamodel);
    settingsConsumer.accept(context.getSettings());
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }

  public <ENTITY> NativeSqlDeleteStarting delete(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return this.delete(entityMetamodel, options -> {});
  }

  public <ENTITY> NativeSqlDeleteStarting delete(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    DeleteContext context = new DeleteContext(entityMetamodel);
    settingsConsumer.accept(context.getSettings());
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }

  public <ENTITY> NativeSqlInsertStarting insert(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return this.insert(entityMetamodel, options -> {});
  }

  public <ENTITY> NativeSqlInsertStarting insert(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    InsertContext context = new InsertContext(entityMetamodel);
    settingsConsumer.accept(context.getSettings());
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
