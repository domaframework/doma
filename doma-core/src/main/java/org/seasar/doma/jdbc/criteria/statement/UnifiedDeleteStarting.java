package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.declaration.DeleteDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

/**
 * Represents a DELETE statement starting point.
 *
 * @param <ENTITY> the type of the entity
 */
public class UnifiedDeleteStarting<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final DeleteSettings settings;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param entityMetamodel the entity metamodel
   * @param settings the settings
   */
  public UnifiedDeleteStarting(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, DeleteSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  /**
   * Deletes the entity.
   *
   * @param entity the entity
   * @return the delete statement
   */
  public Statement<Result<ENTITY>> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlDeleteStatement(entity);
  }

  /**
   * Deletes the entities in batch.
   *
   * @param entities the entities
   * @return the delete statement
   */
  public Statement<BatchResult<ENTITY>> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchDeleteStatement(entities);
  }

  /**
   * Specifies the where clause.
   *
   * @param block the block that provides the where declaration
   * @return the delete statement
   */
  public Statement<Integer> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlDeleteStarting().where(block);
  }

  /**
   * Deletes all the entities.
   *
   * @return the delete statement
   */
  public Statement<Integer> all() {
    settings.setAllowEmptyWhere(true);
    return asNativeSqlDeleteStarting();
  }

  private EntityqlDeleteStatement<ENTITY> asEntityqlDeleteStatement(ENTITY entity) {
    return new EntityqlDeleteStatement<>(config, entityMetamodel, entity, settings);
  }

  private Statement<BatchResult<ENTITY>> asEntityqlBatchDeleteStatement(List<ENTITY> entities) {
    return new EntityqlBatchDeleteStatement<>(config, entityMetamodel, entities, settings);
  }

  private NativeSqlDeleteStarting asNativeSqlDeleteStarting() {
    DeleteContext context = new DeleteContext(entityMetamodel, settings);
    DeleteDeclaration declaration = new DeleteDeclaration(context);
    return new NativeSqlDeleteStarting(config, declaration);
  }
}
