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

import static org.seasar.doma.jdbc.criteria.statement.EntityqlMultiInsertStatement.EMPTY_SQL;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.ReturningProperties;

public class EntityqlMultiInsertReturningStatement<ENTITY>
    extends AbstractStatement<EntityqlMultiInsertReturningStatement<ENTITY>, List<ENTITY>>
    implements Listable<ENTITY> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;
  private final List<PropertyMetamodel<?>> keys;
  private final ReturningProperties returning;

  public EntityqlMultiInsertReturningStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType,
      List<PropertyMetamodel<?>> keys,
      ReturningProperties returning) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
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
  public List<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    var entityType = entityMetamodel.asType();
    var query =
        config.getQueryImplementors().createAutoMultiInsertQuery(EXECUTE_METHOD, entityType);
    query.setMethod(EXECUTE_METHOD);
    query.setConfig(config);
    query.setEntities(entities);
    query.setCallerClassName(getClass().getName());
    query.setCallerMethodName(EXECUTE_METHOD_NAME);
    query.setQueryTimeout(settings.getQueryTimeout());
    query.setSqlLogType(settings.getSqlLogType());
    query.setIncludedPropertyNames(
        settings.include().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setExcludedPropertyNames(
        settings.exclude().stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setMessage(settings.getComment());
    query.setDuplicateKeyType(this.duplicateKeyType);
    query.setDuplicateKeyNames(
        keys.stream().map(PropertyMetamodel::getName).toArray(String[]::new));
    query.setReturning(returning);
    query.prepare();
    var command =
        config
            .getCommandImplementors()
            .createInsertReturningCommand(
                EXECUTE_METHOD,
                query,
                new EntityResultListHandler<>(entityType),
                Collections::emptyList);
    return new Command<>() {
      @Override
      public Query getQuery() {
        return query;
      }

      @Override
      public List<ENTITY> execute() {
        List<ENTITY> entities = command.execute();
        query.complete();
        return entities;
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
