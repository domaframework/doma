package org.seasar.doma.jdbc.criteria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.context.WithContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.statement.UnifiedDeleteStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedInsertStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.UnifiedUpdateStarting;

/**
 * A query DSL entry point.
 *
 * <p>This class unifies {@link Entityql} and {@link NativeSql} APIs.
 */
public class QueryDsl {
  private final Config config;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   */
  public QueryDsl(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  /**
   * Creates a WithQueryDsl.
   *
   * @param entityMetamodel the entity metamodel to use for the context of the common table
   *     expression
   * @param subQuery the sub-query to use in the common table expression
   * @return a new WithQueryDsl instance configured with the specified entity metamodel and
   *     sub-query
   */
  public WithQueryDsl with(EntityMetamodel<?> entityMetamodel, SetOperand<?> subQuery) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(subQuery);
    List<WithContext> withContexts = List.of(new WithContext(entityMetamodel, subQuery));
    return with(withContexts);
  }

  /**
   * Creates a WithQueryDsl.
   *
   * @param withContexts the list of entity metamodel and sub-query pair in use for the context of
   *     the common table expression
   * @return a new WithQueryDsl instance configured with the specified entity metamodel and
   *     sub-query
   */
  public WithQueryDsl with(List<WithContext> withContexts) {
    Objects.requireNonNull(withContexts);
    return new WithQueryDsl(config, withContexts);
  }

  /**
   * Creates a select statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param <ENTITY> the entity type
   * @return the starting point of the select statement
   */
  public <ENTITY> UnifiedSelectStarting<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return from(entityMetamodel, null, settings -> {});
  }

  /**
   * Creates a select statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param settingsConsumer the consumer of the settings
   * @param <ENTITY> the entity type
   * @return the starting point of the select statement
   */
  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<SelectSettings> settingsConsumer) {
    return from(entityMetamodel, null, settingsConsumer);
  }

  /**
   * Creates a select statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param setOperandForSubQuery the set operand for the sub query
   * @param <ENTITY> the entity type
   * @return the starting point of the select statement
   */
  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel, SetOperand<?> setOperandForSubQuery) {
    return from(entityMetamodel, setOperandForSubQuery, settings -> {});
  }

  /**
   * Creates a select statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param setOperandForSubQuery the set operand for the sub query
   * @param settingsConsumer the consumer of the settings
   * @param <ENTITY> the entity type
   * @return the starting point of the select statement
   */
  public <ENTITY> UnifiedSelectStarting<ENTITY> from(
      EntityMetamodel<ENTITY> entityMetamodel,
      SetOperand<?> setOperandForSubQuery,
      Consumer<SelectSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    SetOperationContext<?> setOperationContextForSubQuery =
        setOperandForSubQuery == null ? null : setOperandForSubQuery.getContext();
    SelectContext context =
        new SelectContext(entityMetamodel, Optional.ofNullable(setOperationContextForSubQuery));
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new UnifiedSelectStarting<>(config, declaration, entityMetamodel);
  }

  /**
   * Creates an update statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param <ENTITY> the entity type
   * @return the starting point of the update statement
   */
  public <ENTITY> UnifiedUpdateStarting<ENTITY> update(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return update(entityMetamodel, settings -> {});
  }

  /**
   * Creates an update statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param settingsConsumer the consumer of the settings
   * @param <ENTITY> the entity type
   * @return the starting point of the update statement
   */
  public <ENTITY> UnifiedUpdateStarting<ENTITY> update(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<UpdateSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    UpdateSettings settings = new UpdateSettings();
    settingsConsumer.accept(settings);
    return new UnifiedUpdateStarting<>(config, entityMetamodel, settings);
  }

  /**
   * Creates a delete statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param <ENTITY> the entity type
   * @return the starting point of the delete statement
   */
  public <ENTITY> UnifiedDeleteStarting<ENTITY> delete(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return delete(entityMetamodel, settings -> {});
  }

  /**
   * Creates a delete statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param settingsConsumer the consumer of the settings
   * @param <ENTITY> the entity type
   * @return the starting point of the delete statement
   */
  public <ENTITY> UnifiedDeleteStarting<ENTITY> delete(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<DeleteSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    DeleteSettings settings = new DeleteSettings();
    settingsConsumer.accept(settings);
    return new UnifiedDeleteStarting<>(config, entityMetamodel, settings);
  }

  /**
   * Creates an insert statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param <ENTITY> the entity type
   * @return the starting point of the insert statement
   */
  public <ENTITY> UnifiedInsertStarting<ENTITY> insert(EntityMetamodel<ENTITY> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return insert(entityMetamodel, settings -> {});
  }

  /**
   * Creates an insert statement.
   *
   * @param entityMetamodel the entity metamodel
   * @param settingsConsumer the consumer of the settings
   * @param <ENTITY> the entity type
   * @return the starting point of the insert statement
   */
  public <ENTITY> UnifiedInsertStarting<ENTITY> insert(
      EntityMetamodel<ENTITY> entityMetamodel, Consumer<InsertSettings> settingsConsumer) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(settingsConsumer);
    InsertSettings settings = new InsertSettings();
    settingsConsumer.accept(settings);
    return new UnifiedInsertStarting<>(config, entityMetamodel, settings);
  }
}
