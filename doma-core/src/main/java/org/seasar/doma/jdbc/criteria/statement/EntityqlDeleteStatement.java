/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.OptimisticLockException;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.command.DeleteReturningCommand;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.ReturningProperties;

public class EntityqlDeleteStatement<ENTITY>
    extends AbstractStatement<EntityqlDeleteStatement<ENTITY>, Result<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final DeleteSettings settings;
  private final ReturningProperties returning;

  public EntityqlDeleteStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      DeleteSettings settings) {
    this(config, entityMetamodel, entity, settings, ReturningProperties.NONE);
  }

  private EntityqlDeleteStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      DeleteSettings settings,
      ReturningProperties returning) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
    this.returning = returning;
  }

  public Statement<Result<ENTITY>> returning(PropertyMetamodel<?>... properties) {
    var returning = SpecificMetamodels.of(entityMetamodel, properties);
    return new EntityqlDeleteStatement<>(config, entityMetamodel, entity, settings, returning);
  }

  /**
   * {@inheritDoc}
   *
   * @throws EmptyWhereClauseException if {@link DeleteSettings#getAllowEmptyWhere()} returns
   *     {@literal false} and the WHERE clause is empty
   * @throws OptimisticLockException if the entity has a version property and an update count is
   *     {@literal 0}
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public Result<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<Result<ENTITY>> createCommand() {
    EntityType<ENTITY> entityType = entityMetamodel.asType();
    AutoDeleteQuery<ENTITY> query =
        config.getQueryImplementors().createAutoDeleteQuery(EXECUTE_METHOD, entityType);
    query.setConfig(config);
    query.setMethod(EXECUTE_METHOD);
    query.setEntity(entity);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setVersionIgnored(settings.getIgnoreVersion());
    query.setOptimisticLockExceptionSuppressed(settings.getSuppressOptimisticLockException());
    query.setMessage(settings.getComment());
    query.setReturning(returning);
    query.prepare();
    if (returning.isNone()) {
      return createCommand(query);
    } else {
      return createReturningCommand(entityType, query);
    }
  }

  private Command<Result<ENTITY>> createCommand(AutoDeleteQuery<ENTITY> query) {
    DeleteCommand command =
        config.getCommandImplementors().createDeleteCommand(EXECUTE_METHOD, query);
    return new Command<>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public Result<ENTITY> execute() {
        int count = command.execute();
        query.complete();
        return new Result<>(count, query.getEntity());
      }
    };
  }

  private Command<Result<ENTITY>> createReturningCommand(
      EntityType<ENTITY> entityType, AutoDeleteQuery<ENTITY> query) {
    DeleteReturningCommand<ENTITY> command =
        config
            .getCommandImplementors()
            .createDeleteReturningCommand(
                EXECUTE_METHOD, query, new EntitySingleResultHandler<>(entityType), () -> null);
    return new Command<>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public Result<ENTITY> execute() {
        ENTITY entity = command.execute();
        int count = entity == null ? 0 : 1;
        query.complete();
        return new Result<>(count, entity);
      }
    };
  }
}
