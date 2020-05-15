package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlUpdateStatement<ENTITY>
    extends AbstractStatement<EntityqlUpdateStatement<ENTITY>, ENTITY> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final UpdateSettings settings;

  public EntityqlUpdateStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      UpdateSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<ENTITY> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoUpdateQuery<ENTITY> query =
        config.getQueryImplementors().createAutoUpdateQuery(EXECUTE_METHOD, entityType);
    query.setConfig(config);
    query.setEntity(entity);
    query.setMethod(EXECUTE_METHOD);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setNullExcluded(false);
    query.setVersionIgnored(false);
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setUnchangedPropertyIncluded(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(settings.getComment());
    query.prepare();
    UpdateCommand command =
        config.getCommandImplementors().createUpdateCommand(EXECUTE_METHOD, query);
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
