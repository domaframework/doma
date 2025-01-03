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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

/**
 * Represents a SELECT statement terminal.
 *
 * <p>Note: {@link #project(EntityMetamodel)} and {@link #projectTo(EntityMetamodel,
 * PropertyMetamodel[])} will remove duplicate entities from the results.
 *
 * @param <ENTITY> the type of the entity
 */
public class UnifiedSelectTerminal<ENTITY>
    extends AbstractStatement<UnifiedSelectTerminal<ENTITY>, List<ENTITY>>
    implements EntityQueryable<ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param declaration the declaration
   * @param entityMetamodel the entity metamodel
   */
  UnifiedSelectTerminal(
      Config config, SelectFromDeclaration declaration, EntityMetamodel<ENTITY> entityMetamodel) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
  }

  @Override
  public EntityQueryable<ENTITY> distinct() {
    declaration.distinct(DistinctOption.basic());
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> distinct(DistinctOption distinctOption) {
    Objects.requireNonNull(distinctOption);
    declaration.distinct(distinctOption);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.basic());
    return this;
  }

  @Override
  public EntityQueryable<ENTITY> forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    declaration.forUpdate(option);
    return this;
  }

  @Override
  public <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associate(first, second, associator, AssociationOption.mandatory());
    return this;
  }

  @Override
  public <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associate(first, second, associator, option);
    return this;
  }

  @Override
  public <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associateWith(first, second, associator, AssociationOption.mandatory());
    return this;
  }

  @Override
  public <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associateWith(first, second, associator, option);
    return this;
  }

  @Override
  public <RESULT> Listable<RESULT> project(EntityMetamodel<RESULT> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return asEntityqlSelectStarting().select(entityMetamodel);
  }

  @Override
  public <RESULT> Listable<RESULT> projectTo(
      EntityMetamodel<RESULT> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asEntityqlSelectStarting().selectTo(entityMetamodel, propertyMetamodels);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    return asEntityqlSelectStarting().createCommand();
  }

  private EntityqlSelectStarting<ENTITY> asEntityqlSelectStarting() {
    return new EntityqlSelectStarting<>(config, declaration, entityMetamodel);
  }
}
