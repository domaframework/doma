package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlBatchInsertStatement<ENTITY>
    extends AbstractStatement<EntityqlBatchInsertStatement<ENTITY>, List<ENTITY>> {

  private static final EmptySql EMPTY_SQL = new EmptySql(SqlKind.BATCH_INSERT);
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final InsertSettings settings;

  public EntityqlBatchInsertStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      InsertSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoBatchInsertQuery<ENTITY> query =
        config.getQueryImplementors().createAutoBatchInsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setBatchSize(settings.getBatchSize());
    query.setSqlLogType(settings.getSqlLogType());
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setMessage(settings.getComment());
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
