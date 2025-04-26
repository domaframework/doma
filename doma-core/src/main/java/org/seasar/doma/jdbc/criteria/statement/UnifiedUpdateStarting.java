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
import java.util.function.Consumer;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.context.UpdateSettings;
import org.seasar.doma.jdbc.criteria.declaration.SetDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.UpdateDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

/**
 * Represents an UPDATE statement starting point.
 *
 * @param <ENTITY> the type of the entity
 */
public class UnifiedUpdateStarting<ENTITY> {

  private final Config config;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final UpdateSettings settings;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param entityMetamodel the entity metamodel
   * @param settings the settings
   */
  public UnifiedUpdateStarting(
      Config config, EntityMetamodel<ENTITY> entityMetamodel, UpdateSettings settings) {
    this.config = Objects.requireNonNull(config);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.settings = Objects.requireNonNull(settings);
  }

  /**
   * Updates the entity.
   *
   * @param entity the entity
   * @return the update statement
   */
  public EntityqlUpdateStatement<ENTITY> single(ENTITY entity) {
    Objects.requireNonNull(entity);
    return asEntityqlUpdateStatement(entity);
  }

  /**
   * Updates the entities in batch.
   *
   * @param entities the entities
   * @return the update statement
   */
  public Statement<BatchResult<ENTITY>> batch(List<ENTITY> entities) {
    Objects.requireNonNull(entities);
    return asEntityqlBatchUpdateStatement(entities);
  }

  /**
   * Specifies the set clause.
   *
   * @param block the block to declare the set clause
   * @return the update statement
   */
  public NativeSqlUpdateTerminal set(Consumer<SetDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlUpdateStarting().set(block);
  }

  private EntityqlUpdateStatement<ENTITY> asEntityqlUpdateStatement(ENTITY entity) {
    return new EntityqlUpdateStatement<>(config, entityMetamodel, entity, settings);
  }

  private EntityqlBatchUpdateStatement<ENTITY> asEntityqlBatchUpdateStatement(
      List<ENTITY> entities) {
    return new EntityqlBatchUpdateStatement<>(config, entityMetamodel, entities, settings);
  }

  private NativeSqlUpdateStarting asNativeSqlUpdateStarting() {
    UpdateContext context = new UpdateContext(entityMetamodel, settings);
    UpdateDeclaration declaration = new UpdateDeclaration(context);
    return new NativeSqlUpdateStarting(config, declaration);
  }
}
