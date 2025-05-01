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

import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.InsertReturningCommand;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.ReturningProperties;

class EntityqlInsertReturningStatement<ENTITY>
    extends AbstractStatement<EntityqlInsertReturningStatement<ENTITY>, ENTITY>
    implements Singular<ENTITY> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;
  private final List<PropertyMetamodel<?>> keys;
  private final ReturningProperties returning;

  public EntityqlInsertReturningStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType,
      List<PropertyMetamodel<?>> keys,
      ReturningProperties returning) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
    this.duplicateKeyType = Objects.requireNonNull(duplicateKeyType);
    this.keys = Objects.requireNonNull(keys);
    this.returning = Objects.requireNonNull(returning);
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public ENTITY execute() {
    return super.execute();
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
    query.setNullExcluded(settings.getExcludeNull());
    query.setIncludedPropertyNames(
        settings.include().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setExcludedPropertyNames(
        settings.exclude().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setMessage(settings.getComment());
    query.setDuplicateKeyType(duplicateKeyType);
    query.setDuplicateKeyNames(
        keys.stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setReturning(returning);
    query.prepare();

    InsertReturningCommand<ENTITY> command =
        config
            .getCommandImplementors()
            .createInsertReturningCommand(
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
