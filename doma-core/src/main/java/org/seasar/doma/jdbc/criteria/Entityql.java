package org.seasar.doma.jdbc.criteria;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.statement.EntityqlBatchDeleteStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlBatchInsertStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlBatchUpdateStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlDeleteStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlInsertStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStatement;
import org.seasar.doma.jdbc.criteria.statement.EntityqlUpdateStatement;
import org.seasar.doma.jdbc.criteria.statement.Statement;

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
      EntityDef<ENTITY> entityDef, Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(settingsConsumer);
    SelectContext context = new SelectContext(entityDef);
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new EntityqlSelectStatement<>(config, declaration);
  }

  public <ENTITY> Statement<ENTITY> update(EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return update(entityDef, entity, options -> {});
  }

  public <ENTITY> Statement<ENTITY> update(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new EntityqlUpdateStatement<>(config, entityDef, entity, settings);
  }

  public <ENTITY> Statement<ENTITY> delete(EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return delete(entityDef, entity, options -> {});
  }

  public <ENTITY> Statement<ENTITY> delete(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new EntityqlDeleteStatement<>(config, entityDef, entity, settings);
  }

  public <ENTITY> Statement<ENTITY> insert(EntityDef<ENTITY> entityDef, ENTITY entity) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    return insert(entityDef, entity, options -> {});
  }

  public <ENTITY> Statement<ENTITY> insert(
      EntityDef<ENTITY> entityDef, ENTITY entity, Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new EntityqlInsertStatement<>(config, entityDef, entity, settings);
  }

  public <ENTITY> Statement<List<ENTITY>> update(
      EntityDef<ENTITY> entityDef, List<ENTITY> entities) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    return update(entityDef, entities, options -> {});
  }

  public <ENTITY> Statement<List<ENTITY>> update(
      EntityDef<ENTITY> entityDef,
      List<ENTITY> entities,
      Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchUpdateStatement<>(config, entityDef, entities, settings);
  }

  public <ENTITY> Statement<List<ENTITY>> delete(
      EntityDef<ENTITY> entityDef, List<ENTITY> entities) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    return delete(entityDef, entities, options -> {});
  }

  public <ENTITY> Statement<List<ENTITY>> delete(
      EntityDef<ENTITY> entityDef,
      List<ENTITY> entities,
      Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchDeleteStatement<>(config, entityDef, entities, settings);
  }

  public <ENTITY> Statement<List<ENTITY>> insert(
      EntityDef<ENTITY> entityDef, List<ENTITY> entities) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    return insert(entityDef, entities, options -> {});
  }

  public <ENTITY> Statement<List<ENTITY>> insert(
      EntityDef<ENTITY> entityDef,
      List<ENTITY> entities,
      Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchInsertStatement<>(config, entityDef, entities, settings);
  }
}
