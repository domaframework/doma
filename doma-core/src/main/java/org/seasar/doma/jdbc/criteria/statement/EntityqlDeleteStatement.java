package org.seasar.doma.jdbc.criteria.statement;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.message.Message;

public class EntityqlDeleteStatement<ENTITY> extends AbstractStatement<ENTITY> {

  private static final Method METHOD;

  static {
    try {
      METHOD = EntityqlDeleteStatement.class.getMethod(EXECUTE_METHOD_NAME, Config.class);
    } catch (NoSuchMethodException e) {
      throw new DomaException(Message.DOMA6005, e, EXECUTE_METHOD_NAME);
    }
  }

  private final EntityDef<ENTITY> entityDef;
  private final ENTITY entity;

  public EntityqlDeleteStatement(EntityDef<ENTITY> entityDef, ENTITY entity) {
    this.entityDef = Objects.requireNonNull(entityDef);
    this.entity = Objects.requireNonNull(entity);
  }

  @Override
  protected Command<ENTITY> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType) {
    EntityType<ENTITY> entityType = entityDef.asType();
    AutoDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoDeleteQuery(METHOD, entityType);
    query.setConfig(config);
    query.setMethod(METHOD);
    query.setConfig(config);
    query.setEntity(entity);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(config.getQueryTimeout());
    query.setSqlLogType(sqlLogType);
    query.setVersionIgnored(false);
    query.setOptimisticLockExceptionSuppressed(false);
    query.prepare();
    DeleteCommand command = config.getCommandImplementors().createDeleteCommand(METHOD, query);
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
