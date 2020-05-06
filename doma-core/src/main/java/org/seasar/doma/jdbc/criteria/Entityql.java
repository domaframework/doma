package org.seasar.doma.jdbc.criteria;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.EntityqlDeleteStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlInsertStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlUpdateStatement;

public class Entityql {

  protected final Config config;

  public Entityql(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  public <ENTITY> EntityqlSelectStatement<ENTITY> from(EntityDef<ENTITY> entityDef) {
    Objects.requireNonNull(entityDef);
    return from(entityDef, options -> {});
  }

  public <ENTITY> EntityqlSelectStatement<ENTITY> from(
      EntityDef<ENTITY> entityDef, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(optionsConsumer);
    SelectContext context = new SelectContext(entityDef);
    optionsConsumer.accept(context.getOptions());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new EntityqlSelectStatement<>(config, declaration);
  }

  public <ENTITY> EntityqlUpdateStatement<ENTITY> update(
      EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return update(entityDef, entity, options -> {});
  }

  public <ENTITY> EntityqlUpdateStatement<ENTITY> update(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(optionsConsumer);
    Options options = new Options();
    optionsConsumer.accept(options);
    return new EntityqlUpdateStatement<>(config, entityDef, entity, options);
  }

  public <ENTITY> EntityqlDeleteStatement<ENTITY> delete(
      EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return delete(entityDef, entity, options -> {});
  }

  public <ENTITY> EntityqlDeleteStatement<ENTITY> delete(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(optionsConsumer);
    Options options = new Options();
    optionsConsumer.accept(options);
    return new EntityqlDeleteStatement<>(config, entityDef, entity, options);
  }

  public <ENTITY> EntityqlInsertStatement<ENTITY> insert(
      EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return insert(entityDef, entity, options -> {});
  }

  public <ENTITY> EntityqlInsertStatement<ENTITY> insert(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<Options> optionsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(optionsConsumer);
    Options options = new Options();
    optionsConsumer.accept(options);
    return new EntityqlInsertStatement<>(config, entityDef, entity, options);
  }
}
