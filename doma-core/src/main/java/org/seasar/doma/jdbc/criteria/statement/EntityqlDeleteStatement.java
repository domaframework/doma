package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlDeleteStatement<ENTITY>
    extends AbstractStatement<EntityqlDeleteStatement<ENTITY>, ENTITY> {

  private final EntityDef<ENTITY> entityDef;
  private final ENTITY entity;
  private final DeleteSettings settings;

  public EntityqlDeleteStatement(
      Config config, EntityDef<ENTITY> entityDef, ENTITY entity, DeleteSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
  }

  @Override
  protected Command<ENTITY> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoDeleteQuery(EXECUTE_METHOD, entityType);
    query.setConfig(config);
    query.setMethod(EXECUTE_METHOD);
    query.setEntity(entity);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setVersionIgnored(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(settings.getComment());
    query.prepare();
    DeleteCommand command =
        config.getCommandImplementors().createDeleteCommand(EXECUTE_METHOD, query);
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
