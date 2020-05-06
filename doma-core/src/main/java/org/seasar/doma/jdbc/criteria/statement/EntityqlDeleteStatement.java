package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlDeleteStatement<ENTITY>
    extends AbstractStatement<ENTITY, EntityqlDeleteStatement<ENTITY>> {

  private final EntityDef<ENTITY> entityDef;
  private final ENTITY entity;
  private final Options options;

  public EntityqlDeleteStatement(
      Config config, EntityDef<ENTITY> entityDef, ENTITY entity, Options options) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entity = Objects.requireNonNull(entity);
    this.options = Objects.requireNonNull(options);
  }

  @Override
  protected Command<ENTITY> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoDeleteQuery(EXECUTE_METHOD, entityType);
    query.setConfig(config);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntity(entity);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(config.getQueryTimeout());
    query.setSqlLogType(options.sqlLogType());
    query.setVersionIgnored(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(options.comment());
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
