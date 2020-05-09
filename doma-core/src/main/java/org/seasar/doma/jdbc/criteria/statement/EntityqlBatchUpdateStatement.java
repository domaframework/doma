package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchUpdateStatement<ENTITY>
    extends AbstractStatement<EntityqlBatchUpdateStatement<ENTITY>, List<ENTITY>> {

  private static final EmptySql EMPTY_SQL = new EmptySql(SqlKind.BATCH_UPDATE);
  private final EntityDef<ENTITY> entityDef;
  private final List<ENTITY> entities;
  private final UpdateSettings settings;

  public EntityqlBatchUpdateStatement(
      Config config, EntityDef<ENTITY> entityDef, List<ENTITY> entities, UpdateSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoBatchUpdateQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchUpdateQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setBatchSize(settings.getBatchSize());
    query.setSqlLogType(settings.getSqlLogType());
    query.setVersionIgnored(false);
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(settings.getComment());
    query.prepare();
    BatchUpdateCommand command =
        config.getCommandImplementors().createBatchUpdateCommand(EXECUTE_METHOD, query);
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
