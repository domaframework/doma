package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlInsertStatement<ENTITY>
    extends AbstractStatement<EntityqlInsertStatement<ENTITY>, ENTITY> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final InsertSettings settings;

  public EntityqlInsertStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      InsertSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<ENTITY> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoInsertQuery<ENTITY> query =
        config.getQueryImplementors().createAutoInsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntity(entity);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setNullExcluded(false);
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setMessage(settings.getComment());
    query.prepare();
    InsertCommand command =
        config.getCommandImplementors().createInsertCommand(EXECUTE_METHOD, query);
    return new Command<ENTITY>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public ENTITY execute() {
        command.execute();
        query.complete();
        return query.getEntity();
      }
    };
  }
}