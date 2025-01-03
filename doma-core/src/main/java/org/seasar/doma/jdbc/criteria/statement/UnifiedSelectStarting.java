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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.HavingDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.tuple.Row;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;
import org.seasar.doma.jdbc.criteria.tuple.Tuple4;
import org.seasar.doma.jdbc.criteria.tuple.Tuple5;
import org.seasar.doma.jdbc.criteria.tuple.Tuple6;
import org.seasar.doma.jdbc.criteria.tuple.Tuple7;
import org.seasar.doma.jdbc.criteria.tuple.Tuple8;
import org.seasar.doma.jdbc.criteria.tuple.Tuple9;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * Represents a SELECT statement starting point.
 *
 * <p>Note: {@link #project(EntityMetamodel)} and {@link #projectTo(EntityMetamodel,
 * PropertyMetamodel[])} will remove duplicate entities from the results.
 *
 * @param <ENTITY> the type of the entity
 */
public class UnifiedSelectStarting<ENTITY>
    extends AbstractStatement<UnifiedSelectStarting<ENTITY>, List<ENTITY>>
    implements SetOperand<ENTITY>, EntityQueryable<ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  /**
   * Creates an instance.
   *
   * @param config the configuration
   * @param declaration the declaration
   * @param entityMetamodel the entity metamodel
   */
  public UnifiedSelectStarting(
      Config config, SelectFromDeclaration declaration, EntityMetamodel<ENTITY> entityMetamodel) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
  }

  @Override
  public UnifiedSelectStarting<ENTITY> distinct() {
    declaration.distinct(DistinctOption.basic());
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> distinct(DistinctOption distinctOption) {
    Objects.requireNonNull(distinctOption);
    declaration.distinct(distinctOption);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.basic());
    return this;
  }

  @Override
  public UnifiedSelectStarting<ENTITY> forUpdate(ForUpdateOption option) {
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
    return new UnifiedSelectTerminal<>(config, declaration, entityMetamodel)
        .associate(first, second, associator);
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
    return new UnifiedSelectTerminal<>(config, declaration, entityMetamodel)
        .associate(first, second, associator, option);
  }

  @Override
  public <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    return asUnifiedSelectTerminal().associateWith(first, second, associator);
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
    return asUnifiedSelectTerminal().associateWith(first, second, associator, option);
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

  /**
   * Specifies the group by clause.
   *
   * @param propertyMetamodels the property metamodels to group by
   * @return the select statement
   */
  public NativeSqlSelectStarting<ENTITY> groupBy(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().groupBy(propertyMetamodels);
  }

  /**
   * Specifies the having clause.
   *
   * @param block the block to declare the having condition
   * @return the select statement
   */
  public NativeSqlSelectStarting<ENTITY> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlSelectStarting().having(block);
  }

  /**
   * Projects the result to the entity. The duplicated entities are NOT removed from the result.
   *
   * @return the result
   */
  public SetOperand<ENTITY> select() {
    return asNativeSqlSelectStarting().select();
  }

  /**
   * Projects the result to the specified entity. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel the entity metamodel to project
   * @return the result
   */
  public <T> SetOperand<T> select(EntityMetamodel<T> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return asNativeSqlSelectStarting().select(entityMetamodel);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @return the result
   */
  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      EntityMetamodel<T1> entityMetamodel1, EntityMetamodel<T2> entityMetamodel2) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    return asNativeSqlSelectStarting().select(entityMetamodel1, entityMetamodel2);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    return asNativeSqlSelectStarting().select(entityMetamodel1, entityMetamodel2, entityMetamodel3);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4> SetOperand<Tuple4<T1, T2, T3, T4>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    return asNativeSqlSelectStarting()
        .select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @param entityMetamodel5 the fifth entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5> SetOperand<Tuple5<T1, T2, T3, T4, T5>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4,
      EntityMetamodel<T5> entityMetamodel5) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    Objects.requireNonNull(entityMetamodel5);
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @param entityMetamodel5 the fifth entity metamodel to project
   * @param entityMetamodel6 the sixth entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6> SetOperand<Tuple6<T1, T2, T3, T4, T5, T6>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4,
      EntityMetamodel<T5> entityMetamodel5,
      EntityMetamodel<T6> entityMetamodel6) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    Objects.requireNonNull(entityMetamodel5);
    Objects.requireNonNull(entityMetamodel6);
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5,
            entityMetamodel6);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @param entityMetamodel5 the fifth entity metamodel to project
   * @param entityMetamodel6 the sixth entity metamodel to project
   * @param entityMetamodel7 the seventh entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7> SetOperand<Tuple7<T1, T2, T3, T4, T5, T6, T7>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4,
      EntityMetamodel<T5> entityMetamodel5,
      EntityMetamodel<T6> entityMetamodel6,
      EntityMetamodel<T7> entityMetamodel7) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    Objects.requireNonNull(entityMetamodel5);
    Objects.requireNonNull(entityMetamodel6);
    Objects.requireNonNull(entityMetamodel7);
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5,
            entityMetamodel6,
            entityMetamodel7);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @param entityMetamodel5 the fifth entity metamodel to project
   * @param entityMetamodel6 the sixth entity metamodel to project
   * @param entityMetamodel7 the seventh entity metamodel to project
   * @param entityMetamodel8 the eighth entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7, T8> SetOperand<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4,
      EntityMetamodel<T5> entityMetamodel5,
      EntityMetamodel<T6> entityMetamodel6,
      EntityMetamodel<T7> entityMetamodel7,
      EntityMetamodel<T8> entityMetamodel8) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    Objects.requireNonNull(entityMetamodel5);
    Objects.requireNonNull(entityMetamodel6);
    Objects.requireNonNull(entityMetamodel7);
    Objects.requireNonNull(entityMetamodel8);
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5,
            entityMetamodel6,
            entityMetamodel7,
            entityMetamodel8);
  }

  /**
   * Projects the result to the specified entities. The duplicated entities are NOT removed from the
   * result.
   *
   * @param entityMetamodel1 the first entity metamodel to project
   * @param entityMetamodel2 the second entity metamodel to project
   * @param entityMetamodel3 the third entity metamodel to project
   * @param entityMetamodel4 the fourth entity metamodel to project
   * @param entityMetamodel5 the fifth entity metamodel to project
   * @param entityMetamodel6 the sixth entity metamodel to project
   * @param entityMetamodel7 the seventh entity metamodel to project
   * @param entityMetamodel8 the eighth entity metamodel to project
   * @param entityMetamodel9 the ninth entity metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7, T8, T9>
      SetOperand<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(
          EntityMetamodel<T1> entityMetamodel1,
          EntityMetamodel<T2> entityMetamodel2,
          EntityMetamodel<T3> entityMetamodel3,
          EntityMetamodel<T4> entityMetamodel4,
          EntityMetamodel<T5> entityMetamodel5,
          EntityMetamodel<T6> entityMetamodel6,
          EntityMetamodel<T7> entityMetamodel7,
          EntityMetamodel<T8> entityMetamodel8,
          EntityMetamodel<T9> entityMetamodel9) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    Objects.requireNonNull(entityMetamodel5);
    Objects.requireNonNull(entityMetamodel6);
    Objects.requireNonNull(entityMetamodel7);
    Objects.requireNonNull(entityMetamodel8);
    Objects.requireNonNull(entityMetamodel9);
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5,
            entityMetamodel6,
            entityMetamodel7,
            entityMetamodel8,
            entityMetamodel9);
  }

  /**
   * Projects the result with the specified property. The duplicated tuples are NOT removed from the
   * result.
   *
   * @param propertyMetamodel the property metamodel to project
   * @return the result
   */
  public <T> SetOperand<T> select(PropertyMetamodel<T> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return asNativeSqlSelectStarting().select(propertyMetamodel);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @return the result
   */
  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      PropertyMetamodel<T1> propertyMetamodel1, PropertyMetamodel<T2> propertyMetamodel2) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    return asNativeSqlSelectStarting().select(propertyMetamodel1, propertyMetamodel2);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @return the result
   */
  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    return asNativeSqlSelectStarting()
        .select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4> SetOperand<Tuple4<T1, T2, T3, T4>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    return asNativeSqlSelectStarting()
        .select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @param propertyMetamodel5 the fifth property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5> SetOperand<Tuple5<T1, T2, T3, T4, T5>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4,
      PropertyMetamodel<T5> propertyMetamodel5) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    Objects.requireNonNull(propertyMetamodel5);
    return asNativeSqlSelectStarting()
        .select(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @param propertyMetamodel5 the fifth property metamodel to project
   * @param propertyMetamodel6 the sixth property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6> SetOperand<Tuple6<T1, T2, T3, T4, T5, T6>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4,
      PropertyMetamodel<T5> propertyMetamodel5,
      PropertyMetamodel<T6> propertyMetamodel6) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    Objects.requireNonNull(propertyMetamodel5);
    Objects.requireNonNull(propertyMetamodel6);
    return asNativeSqlSelectStarting()
        .select(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @param propertyMetamodel5 the fifth property metamodel to project
   * @param propertyMetamodel6 the sixth property metamodel to project
   * @param propertyMetamodel7 the seventh property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7> SetOperand<Tuple7<T1, T2, T3, T4, T5, T6, T7>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4,
      PropertyMetamodel<T5> propertyMetamodel5,
      PropertyMetamodel<T6> propertyMetamodel6,
      PropertyMetamodel<T7> propertyMetamodel7) {
    declaration.select(
        Arrays.asList(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7));
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    Objects.requireNonNull(propertyMetamodel5);
    Objects.requireNonNull(propertyMetamodel6);
    Objects.requireNonNull(propertyMetamodel7);
    return asNativeSqlSelectStarting()
        .select(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @param propertyMetamodel5 the fifth property metamodel to project
   * @param propertyMetamodel6 the sixth property metamodel to project
   * @param propertyMetamodel7 the seventh property metamodel to project
   * @param propertyMetamodel8 the eighth property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7, T8> SetOperand<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4,
      PropertyMetamodel<T5> propertyMetamodel5,
      PropertyMetamodel<T6> propertyMetamodel6,
      PropertyMetamodel<T7> propertyMetamodel7,
      PropertyMetamodel<T8> propertyMetamodel8) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    Objects.requireNonNull(propertyMetamodel5);
    Objects.requireNonNull(propertyMetamodel6);
    Objects.requireNonNull(propertyMetamodel7);
    Objects.requireNonNull(propertyMetamodel8);
    return asNativeSqlSelectStarting()
        .select(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7,
            propertyMetamodel8);
  }

  /**
   * Projects the result with the specified properties. The duplicated tuples are NOT removed from
   * the result.
   *
   * @param propertyMetamodel1 the first property metamodel to project
   * @param propertyMetamodel2 the second property metamodel to project
   * @param propertyMetamodel3 the third property metamodel to project
   * @param propertyMetamodel4 the fourth property metamodel to project
   * @param propertyMetamodel5 the fifth property metamodel to project
   * @param propertyMetamodel6 the sixth property metamodel to project
   * @param propertyMetamodel7 the seventh property metamodel to project
   * @param propertyMetamodel8 the eighth property metamodel to project
   * @param propertyMetamodel9 the ninth property metamodel to project
   * @return the result
   */
  public <T1, T2, T3, T4, T5, T6, T7, T8, T9>
      SetOperand<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(
          PropertyMetamodel<T1> propertyMetamodel1,
          PropertyMetamodel<T2> propertyMetamodel2,
          PropertyMetamodel<T3> propertyMetamodel3,
          PropertyMetamodel<T4> propertyMetamodel4,
          PropertyMetamodel<T5> propertyMetamodel5,
          PropertyMetamodel<T6> propertyMetamodel6,
          PropertyMetamodel<T7> propertyMetamodel7,
          PropertyMetamodel<T8> propertyMetamodel8,
          PropertyMetamodel<T9> propertyMetamodel9) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    Objects.requireNonNull(propertyMetamodel5);
    Objects.requireNonNull(propertyMetamodel6);
    Objects.requireNonNull(propertyMetamodel7);
    Objects.requireNonNull(propertyMetamodel8);
    Objects.requireNonNull(propertyMetamodel9);
    return asNativeSqlSelectStarting()
        .select(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7,
            propertyMetamodel8,
            propertyMetamodel9);
  }

  /**
   * Projects the result as a List of {@link Row} implicitly with the specified properties. The
   * duplicated rows are NOT removed from the result.
   *
   * @param propertyMetamodels the property metamodels to project
   * @return the result
   */
  public SetOperand<Row> select(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().select(propertyMetamodel, propertyMetamodels);
  }

  /**
   * Projects the result as a List of {@link Row} explicitly with the specified properties. The
   * duplicated rows are NOT removed from the result.
   *
   * @param propertyMetamodels the property metamodels to project
   * @return the result
   */
  public SetOperand<Row> selectAsRow(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().selectAsRow(propertyMetamodel, propertyMetamodels);
  }

  /**
   * Projects the result to the entity. The duplicated entities are NOT removed from the result.
   *
   * @return the result
   */
  public <RESULT> SetOperand<RESULT> selectTo(
      EntityMetamodel<RESULT> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().selectTo(entityMetamodel, propertyMetamodels);
  }

  @Override
  public Stream<ENTITY> openStream() {
    return asNativeSqlSelectStarting().openStream();
  }

  @Override
  public <RESULT> RESULT mapStream(Function<Stream<ENTITY>, RESULT> streamMapper) {
    return asNativeSqlSelectStarting().mapStream(streamMapper);
  }

  @Override
  public SetOperationContext<ENTITY> getContext() {
    return asNativeSqlSelectStarting().getContext();
  }

  @Override
  public SetOperator<ENTITY> union(SetOperand<ENTITY> other) {
    return asNativeSqlSelectStarting().union(other);
  }

  @Override
  public SetOperator<ENTITY> unionAll(SetOperand<ENTITY> other) {
    return asNativeSqlSelectStarting().unionAll(other);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    return asEntityqlSelectStarting().createCommand();
  }

  private UnifiedSelectTerminal<ENTITY> asUnifiedSelectTerminal() {
    return new UnifiedSelectTerminal<>(config, declaration, entityMetamodel);
  }

  private EntityqlSelectStarting<ENTITY> asEntityqlSelectStarting() {
    return new EntityqlSelectStarting<>(config, declaration, entityMetamodel);
  }

  private NativeSqlSelectStarting<ENTITY> asNativeSqlSelectStarting() {
    Function<SelectQuery, ObjectProvider<ENTITY>> factory =
        query -> new EntityProvider<>(entityMetamodel.asType(), query, false);
    return new NativeSqlSelectStarting<>(config, declaration, entityMetamodel, factory);
  }
}
