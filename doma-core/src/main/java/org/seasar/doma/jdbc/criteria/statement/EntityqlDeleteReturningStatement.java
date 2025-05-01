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
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.DeleteSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.ReturningProperties;

public class EntityqlDeleteReturningStatement<ENTITY>
    extends AbstractStatement<EntityqlDeleteReturningStatement<ENTITY>, ENTITY>
    implements Singular<ENTITY> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final DeleteSettings settings;
  private final ReturningProperties returning;

  public EntityqlDeleteReturningStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      DeleteSettings settings,
      ReturningProperties returning) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
    this.returning = Objects.requireNonNull(returning);
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
  public ENTITY execute() {
    return super.execute();
  }

  @Override
  protected Command<ENTITY> createCommand() {
    var entityType = entityMetamodel.asType();
    var query = config.getQueryImplementors().createAutoDeleteQuery(EXECUTE_METHOD, entityType);
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
    var command =
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
      public ENTITY execute() {
        ENTITY entity = command.execute();
        query.complete();
        return entity;
      }
    };
  }
}
