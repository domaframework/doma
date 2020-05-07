package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchDeleteStatement<ENTITY>
    extends AbstractStatement<EntityqlBatchDeleteStatement<ENTITY>, List<ENTITY>> {

  private static final EmptySql EMPTY_SQL = new EmptySql(SqlKind.BATCH_DELETE);
  private final EntityDef<ENTITY> entityDef;
  private final List<ENTITY> entities;
  private final Options options;

  public EntityqlBatchDeleteStatement(
      Config config, EntityDef<ENTITY> entityDef, List<ENTITY> entities, Options options) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entities = Objects.requireNonNull(entities);
    this.options = Objects.requireNonNull(options);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoBatchDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchDeleteQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(config.getQueryTimeout());
    query.setBatchSize(config.getBatchSize());
    query.setSqlLogType(options.getSqlLogType());
    query.setVersionIgnored(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(options.getComment());
    query.prepare();
    BatchDeleteCommand command =
        config.getCommandImplementors().createBatchDeleteCommand(EXECUTE_METHOD, query);
    return new Command<List<ENTITY>>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public List<ENTITY> execute() {
        command.execute();
        query.complete();
        return query.getEntities();
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
