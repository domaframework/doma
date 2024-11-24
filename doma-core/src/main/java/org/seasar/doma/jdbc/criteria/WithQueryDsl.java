package org.seasar.doma.jdbc.criteria;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SelectSettings;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.context.WithContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.statement.UnifiedSelectStarting;

/**
 * A query DSL entry point.
 *
 * <p>This class unifies {@link Entityql} and {@link NativeSql} APIs.
 */
public class WithQueryDsl {
  private final Config config;
  private final List<WithContext> withContexts;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param withContexts EntityMetamodel and SetOperationContext pair list
   */
  public WithQueryDsl(Config config, List<WithContext> withContexts) {
    this.config = Objects.requireNonNull(config);
    this.withContexts = Objects.requireNonNull(withContexts);
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
        new SelectContext(
            entityMetamodel,
            Optional.ofNullable(setOperationContextForSubQuery),
            this.withContexts);
    settingsConsumer.accept(context.getSettings());
    SelectFromDeclaration declaration = new SelectFromDeclaration(context);
    return new UnifiedSelectStarting<>(config, declaration, entityMetamodel);
  }
}
