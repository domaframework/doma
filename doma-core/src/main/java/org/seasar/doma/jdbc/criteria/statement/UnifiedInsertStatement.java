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

public class UnifiedInsertStatement<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final InsertSettings settings;

  public UnifiedInsertStatement(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, InsertSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  public EntityqlInsertStatement<ENTITY> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlInsertStatement(entity);
  }

  public EntityqlBatchInsertStatement<ENTITY> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchInsertStatement(entities);
  }

  public EntityqlMultiInsertStatement<ENTITY> multi(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlMultiInsertStatement(entities);
  }

  public NativeSqlInsertTerminal values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlInsertStarting().values(block);
  }

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
