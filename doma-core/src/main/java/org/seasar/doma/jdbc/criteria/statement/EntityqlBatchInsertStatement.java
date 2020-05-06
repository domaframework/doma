package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchInsertStatement<ENTITY>
    extends AbstractStatement<List<ENTITY>, EntityqlBatchInsertStatement<ENTITY>> {

  private static final EmptySql EMPTY_SQL = new EmptySql(SqlKind.BATCH_INSERT);
  private final EntityDef<ENTITY> entityDef;
  private final List<ENTITY> entities;
  private final Options options;

  public EntityqlBatchInsertStatement(
      Config config, EntityDef<ENTITY> entityDef, List<ENTITY> entities, Options options) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entities = Objects.requireNonNull(entities);
    this.options = Objects.requireNonNull(options);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoBatchInsertQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchInsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(config.getQueryTimeout());
    query.setBatchSize(config.getBatchSize());
    query.setSqlLogType(options.sqlLogType());
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setMessage(options.comment());
    query.prepare();
    BatchInsertCommand command =
        config.getCommandImplementors().createBatchInsertCommand(EXECUTE_METHOD, query);
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
