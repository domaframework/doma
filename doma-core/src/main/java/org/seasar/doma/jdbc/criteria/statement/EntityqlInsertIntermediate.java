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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class EntityqlInsertIntermediate<ENTITY>
    extends AbstractStatement<EntityqlInsertIntermediate<ENTITY>, Result<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final InsertSettings settings;
  private final DuplicateKeyType duplicateKeyType;

  public EntityqlInsertIntermediate(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      InsertSettings settings,
      DuplicateKeyType duplicateKeyType) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
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
  public EntityqlInsertTerminal<ENTITY> keys(PropertyMetamodel<?>... keys) {
    Objects.requireNonNull(keys);
    return new EntityqlInsertTerminal<>(
        config, entityMetamodel, entity, settings, duplicateKeyType, Arrays.stream(keys).toList());
  }

  public Statement<Result<ENTITY>> returning(PropertyMetamodel<?>... properties) {
    var returning = SpecificMetamodels.of(entityMetamodel, properties);
    return new EntityqlInsertTerminal<>(
        config, entityMetamodel, entity, settings, duplicateKeyType, List.of(), returning);
  }

  /**
   * {@inheritDoc}
   *
   * @throws org.seasar.doma.jdbc.UniqueConstraintException if an unique constraint is violated
   * @throws org.seasar.doma.jdbc.JdbcException if a JDBC related error occurs
   */
  @SuppressWarnings("EmptyMethod")
  @Override
  public Result<ENTITY> execute() {
    return super.execute();
  }

  @Override
  protected Command<Result<ENTITY>> createCommand() {
    EntityqlInsertTerminal<ENTITY> terminal =
        new EntityqlInsertTerminal<>(
            config, entityMetamodel, entity, settings, duplicateKeyType, List.of());
    return terminal.createCommand();
  }
}
