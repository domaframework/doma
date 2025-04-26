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

import java.util.Collections;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.InsertSettings;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class EntityqlInsertStatement<ENTITY>
    extends AbstractStatement<EntityqlInsertStatement<ENTITY>, Result<ENTITY>> {

  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final ENTITY entity;
  private final InsertSettings settings;
  private DuplicateKeyType duplicateKeyType = DuplicateKeyType.EXCEPTION;

  public EntityqlInsertStatement(
      Config config,
      EntityMetamodel<ENTITY> entityMetamodel,
      ENTITY entity,
      InsertSettings settings) {
    super(Objects.requireNonNull(config));
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.entity = Objects.requireNonNull(entity);
    this.settings = Objects.requireNonNull(settings);
  }

  /**
   * Create statement that inserts or updates
   *
   * @return statement
   */
  public EntityqlInsertIntermediate<ENTITY> onDuplicateKeyUpdate() {
    this.duplicateKeyType = DuplicateKeyType.UPDATE;
    return new EntityqlInsertIntermediate<>(
        config, entityMetamodel, entity, settings, duplicateKeyType);
  }

  /**
   * Create statement that inserts or ignore
   *
   * @return statement
   */
  public EntityqlInsertIntermediate<ENTITY> onDuplicateKeyIgnore() {
    this.duplicateKeyType = DuplicateKeyType.IGNORE;
    return new EntityqlInsertIntermediate<>(
        config, entityMetamodel, entity, settings, duplicateKeyType);
  }

  public Statement<Result<ENTITY>> returning() {
    return new EntityqlInsertIntermediate<>(
            config, entityMetamodel, entity, settings, duplicateKeyType)
        .returning();
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
            config, entityMetamodel, entity, settings, duplicateKeyType, Collections.emptyList());
    return terminal.createCommand();
  }
}
