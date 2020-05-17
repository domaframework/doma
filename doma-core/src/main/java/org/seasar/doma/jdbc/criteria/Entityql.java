package org.seasar.doma.jdbc.criteria;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
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

  public <ENTITY> EntityqlSelectStatement<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return from(entityMetamodel, settings -> {});
  }

  public <ENTITY> EntityqlSelectStatement<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    SelectContext context = new SelectContext(entityMetamodel);
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new EntityqlSelectStatement<>(config, declaration);
  }

  public <ENTITY> Statement<Result<ENTITY>> update(
      EntityMetamodel<ENTITY> entityMetamodel, ENTITY entity) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    return update(entityMetamodel, entity, settings -> {});
  }

  public <ENTITY> Statement<Result<ENTITY>> update(
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new EntityqlUpdateStatement<>(config, entityMetamodel, entity, settings);
  }

  public <ENTITY> Statement<Result<ENTITY>> delete(
      EntityMetamodel<ENTITY> entityMetamodel, ENTITY entity) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    return delete(entityMetamodel, entity, settings -> {});
  }

  public <ENTITY> Statement<Result<ENTITY>> delete(
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new EntityqlDeleteStatement<>(config, entityMetamodel, entity, settings);
  }

  public <ENTITY> Statement<Result<ENTITY>> insert(
      EntityMetamodel<ENTITY> entityMetamodel, ENTITY entity) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    return insert(entityMetamodel, entity, settings -> {});
  }

  public <ENTITY> Statement<Result<ENTITY>> insert(
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entity);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new EntityqlInsertStatement<>(config, entityMetamodel, entity, settings);
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> update(
      EntityMetamodel<ENTITY> entityMetamodel, List<ENTITY> entities) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    return update(entityMetamodel, entities, settings -> {});
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> update(
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchUpdateStatement<>(config, entityMetamodel, entities, settings);
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> delete(
      EntityMetamodel<ENTITY> entityMetamodel, List<ENTITY> entities) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    return delete(entityMetamodel, entities, settings -> {});
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> delete(
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchDeleteStatement<>(config, entityMetamodel, entities, settings);
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> insert(
      EntityMetamodel<ENTITY> entityMetamodel, List<ENTITY> entities) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    return insert(entityMetamodel, entities, settings -> {});
  }

  public <ENTITY> Statement<BatchResult<ENTITY>> insert(
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(entities);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new EntityqlBatchInsertStatement<>(config, entityMetamodel, entities, settings);
  }
}
