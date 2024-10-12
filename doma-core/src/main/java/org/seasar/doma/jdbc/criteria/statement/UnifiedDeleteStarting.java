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

public class UnifiedDeleteStarting<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final DeleteSettings settings;

  public UnifiedDeleteStarting(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, DeleteSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  public Statement<Result<ENTITY>> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlDeleteStatement(entity);
  }

  public Statement<BatchResult<ENTITY>> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchDeleteStatement(entities);
  }

  public Statement<Integer> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlDeleteStarting().where(block);
  }

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
