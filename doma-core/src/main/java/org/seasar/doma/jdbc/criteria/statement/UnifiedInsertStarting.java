package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.InsertDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.InsertSelectDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.ValuesDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

/**
 * Represents an INSERT statement starting point.
 *
 * @param <ENTITY> the type of the entity
 */
public class UnifiedInsertStarting<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final InsertSettings settings;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param entityMetamodel the entity metamodel
   * @param settings the settings
   */
  public UnifiedInsertStarting(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, InsertSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  /**
   * Inserts the entity.
   *
   * @param entity the entity
   * @return the insert statement
   */
  public EntityqlInsertStatement<ENTITY> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlInsertStatement(entity);
  }

  /**
   * Inserts the entities in batch.
   *
   * @param entities the entities
   * @return the insert statement
   */
  public EntityqlBatchInsertStatement<ENTITY> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchInsertStatement(entities);
  }

  /**
   * Inserts the entities in a multi-row insert.
   *
   * @param entities the entities
   * @return the insert statement
   */
  public EntityqlMultiInsertStatement<ENTITY> multi(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlMultiInsertStatement(entities);
  }

  /**
   * Specifies the values clause.
   *
   * @param block the block that provides the values declaration
   * @return the insert statement
   */
  public NativeSqlInsertTerminal values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlInsertStarting().values(block);
  }

  /**
   * Specifies the select clause.
   *
   * @param block the block that provides the select declaration
   * @return the insert statement
   */
  public NativeSqlInsertTerminal select(
      Function<InsertSelectDeclaration, SubSelectContext<?>> block) {
    Objects.requireNonNull(block);
    return asNativeSqlInsertStarting().select(block);
  }

  private EntityqlInsertStatement<ENTITY> asEntityqlInsertStatement(ENTITY entity) {
    return new EntityqlInsertStatement<>(config, entityMetamodel, entity, settings);
  }

  private EntityqlBatchInsertStatement<ENTITY> asEntityqlBatchInsertStatement(
      List<ENTITY> entities) {
    return new EntityqlBatchInsertStatement<>(config, entityMetamodel, entities, settings);
  }

  private EntityqlMultiInsertStatement<ENTITY> asEntityqlMultiInsertStatement(
      List<ENTITY> entities) {
    return new EntityqlMultiInsertStatement<>(config, entityMetamodel, entities, settings);
  }

  private NativeSqlInsertStarting asNativeSqlInsertStarting() {
    InsertContext context = new InsertContext(entityMetamodel, settings);
    InsertDeclaration declaration = new InsertDeclaration(context);
    return new NativeSqlInsertStarting(config, declaration);
  }
}
