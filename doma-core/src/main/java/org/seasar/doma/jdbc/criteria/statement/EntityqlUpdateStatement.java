package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.criteria.context.Options;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.Query;

public class EntityqlUpdateStatement<ENTITY>
    extends AbstractStatement<ENTITY, EntityqlUpdateStatement<ENTITY>> {

  private final EntityDef<ENTITY> entityDef;
  private final ENTITY entity;
  private final Options options;

  public EntityqlUpdateStatement(
      Config config, EntityDef<ENTITY> entityDef, ENTITY entity, Options options) {
    super(Objects.requireNonNull(config));
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entity = Objects.requireNonNull(entity);
    this.options = Objects.requireNonNull(options);
  }

  @Override
  protected Command<ENTITY> createCommand() {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoUpdateQuery<ENTITY> query =
        config.getQueryImplementors().createAutoUpdateQuery(EXECUTE_METHOD, entityType);
    query.setConfig(config);
    query.setEntity(entity);
    query.setMethod(EXECUTE_METHOD);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(config.getQueryTimeout());
    query.setSqlLogType(options.sqlLogType());
    query.setNullExcluded(false);
    query.setVersionIgnored(false);
    query.setIncludedPropertyNames();
    query.setExcludedPropertyNames();
    query.setUnchangedPropertyIncluded(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.setMessage(options.comment());
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
