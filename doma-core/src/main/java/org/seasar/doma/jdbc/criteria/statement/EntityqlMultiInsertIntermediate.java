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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.MultiResult;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class EntityqlMultiInsertIntermediate<ENTITY>
    extends AbstractStatement<EntityqlMultiInsertIntermediate<ENTITY>, MultiResult<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final List<ENTITY> entities;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;

  public EntityqlMultiInsertIntermediate(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      List<ENTITY> entities,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entities = Objects.requireNonNull(entities);
    this.settings = Objects.requireNonNull(settings);
    this.duplicateKeyType = Objects.requireNonNull(duplicateKeyType);
  }

  /**
   * Specify the keys used for duplicate checking UPSERT statement. if no keys are specified, the
   * {@link org.seasar.doma.Id} property are used for duplicate checking.
   *
   * @param keys keys the keys used for duplicate checking
   * @return selecting set statement builder
   */
  public EntityqlMultiInsertTerminal<ENTITY> keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    return new EntityqlMultiInsertTerminal<>(
        config,
        entityMetamodel,
        entities,
        settings,
        duplicateKeyType,
        Arrays.stream(keys).toList());
  }

  public Statement<MultiResult<ENTITY>> returning() {
    return new EntityqlMultiInsertTerminal<>(
        config, entityMetamodel, entities, settings, duplicateKeyType, List.of(), true);
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public MultiResult<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<MultiResult<ENTITY>> createCommand() {
    EntityqlMultiInsertTerminal<ENTITY> terminal =
        new EntityqlMultiInsertTerminal<>(
            config, entityMetamodel, entities, settings, duplicateKeyType, List.of());
    return terminal.createCommand();
  }

  @Override
  public Sql<?> asSql() {
    if (entities.isEmpty()) {
      return EMPTY_SQL;
    }
    return super.asSql();
  }
}
