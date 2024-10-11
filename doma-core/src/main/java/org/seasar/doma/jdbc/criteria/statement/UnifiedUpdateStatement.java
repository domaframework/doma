package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class UnifiedUpdateStatement<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final UpdateSettings settings;

  public UnifiedUpdateStatement(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, UpdateSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  public Statement<Result<ENTITY>> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlUpdateStatement(entity);
  }

  public Statement<BatchResult<ENTITY>> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchUpdateStatement(entities);
  }

  public NativeSqlUpdateTerminal set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlUpdateStarting().set(block);
  }

  private EntityqlUpdateStatement<ENTITY> asEntityqlUpdateStatement(ENTITY entity) {
    return new EntityqlUpdateStatement<>(config, entityMetamodel, entity, settings);
  }

  private EntityqlBatchUpdateStatement<ENTITY> asEntityqlBatchUpdateStatement(
      List<ENTITY> entities) {
    return new EntityqlBatchUpdateStatement<>(config, entityMetamodel, entities, settings);
  }

  private NativeSqlUpdateStarting asNativeSqlUpdateStarting() {
    UpdateContext context = new UpdateContext(entityMetamodel, settings);
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }
}
