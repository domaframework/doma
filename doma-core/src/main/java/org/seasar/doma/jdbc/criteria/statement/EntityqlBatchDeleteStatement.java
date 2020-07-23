package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchDeleteStatement<ENTITY>
    extends AbstractStatement<EntityqlBatchDeleteStatement<ENTITY>, BatchResult<ENTITY>> {

  private static final EmptySql EMPTY_SQL = new EmptySql(SqlKind.BATCH_DELETE);
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final DeleteSettings settings;

  public EntityqlBatchDeleteStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      DeleteSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<BatchResult<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoBatchDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchDeleteQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setBatchSize(settings.getBatchSize());
    query.setSqlLogType(settings.getSqlLogType());
    query.setVersionIgnored(settings.getIgnoreVersion());
    query.setOptimisticLockExceptionSuppressed(settings.getSuppressOptimisticLockException());
    query.setMessage(settings.getComment());
    query.prepare();
    BatchDeleteCommand command =
        config.getCommandImplementors().createBatchDeleteCommand(EXECUTE_METHOD, query);
    return new Command<BatchResult<ENTITY>>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public BatchResult<ENTITY> execute() {
        int[] counts = command.execute();
        query.complete();
        return new BatchResult<>(counts, query.getEntities());
      }
    };
  }

  @Override
  public Sql<?> asSql() {
    if (entities.isEmpty()) {
      return EMPTY_SQL;
    }
    return super.asSql();
  }
}
