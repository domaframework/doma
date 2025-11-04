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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.command.DataRow;
import org.seasar.doma.jdbc.criteria.command.MappedResultProvider;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.HavingDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
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
import org.seasar.doma.message.Message;

public class NativeSqlSelectStarting<ENTITY>
    extends AbstractSetOperand<NativeSqlSelectStarting<ENTITY>, ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  public NativeSqlSelectStarting(
      Config config,
      SelectFromDeclaration declaration,
      EntityMetamodel<ENTITY> entityMetamodel,
      Function<SelectQuery, ObjectProvider<ENTITY>> objectProviderFactory) {
    super(Objects.requireNonNull(config), Objects.requireNonNull(objectProviderFactory));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
  }

  public NativeSqlSelectStarting<ENTITY> distinct() {
    declaration.distinct(DistinctOption.basic());
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> distinct(DistinctOption distinctOption) {
    Objects.requireNonNull(distinctOption);
    declaration.distinct(distinctOption);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> groupBy(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    declaration.groupBy(propertyMetamodels);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.having(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.basic());
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    declaration.forUpdate(option);
    return this;
  }

  public SetOperand<ENTITY> select() {
    declaration.select(entityMetamodel.allPropertyMetamodels());
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(dataRow -> dataRow.get(entityMetamodel)));
  }

  public <T> SetOperand<T> select(EntityMetamodel<T> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    declaration.select(entityMetamodel.allPropertyMetamodels());
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(dataRow -> dataRow.get(entityMetamodel)));
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      EntityMetamodel<T1> entityMetamodel1, EntityMetamodel<T2> entityMetamodel2) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              return new Tuple2<>(item1, item2);
            }));
  }

  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              return new Tuple3<>(item1, item2, item3);
            }));
  }

  public <T1, T2, T3, T4> SetOperand<Tuple4<T1, T2, T3, T4>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3,
      EntityMetamodel<T4> entityMetamodel4) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    Objects.requireNonNull(entityMetamodel4);
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              return new Tuple4<>(item1, item2, item3, item4);
            }));
  }

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
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size()
                + entityMetamodel5.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel5.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              T5 item5 = dataRow.get(entityMetamodel5);
              return new Tuple5<>(item1, item2, item3, item4, item5);
            }));
  }

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
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size()
                + entityMetamodel5.allPropertyMetamodels().size()
                + entityMetamodel6.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel5.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel6.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              T5 item5 = dataRow.get(entityMetamodel5);
              T6 item6 = dataRow.get(entityMetamodel6);
              return new Tuple6<>(item1, item2, item3, item4, item5, item6);
            }));
  }

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
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size()
                + entityMetamodel5.allPropertyMetamodels().size()
                + entityMetamodel6.allPropertyMetamodels().size()
                + entityMetamodel7.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel5.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel6.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel7.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              T5 item5 = dataRow.get(entityMetamodel5);
              T6 item6 = dataRow.get(entityMetamodel6);
              T7 item7 = dataRow.get(entityMetamodel7);
              return new Tuple7<>(item1, item2, item3, item4, item5, item6, item7);
            }));
  }

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
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size()
                + entityMetamodel5.allPropertyMetamodels().size()
                + entityMetamodel6.allPropertyMetamodels().size()
                + entityMetamodel7.allPropertyMetamodels().size()
                + entityMetamodel8.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel5.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel6.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel7.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel8.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              T5 item5 = dataRow.get(entityMetamodel5);
              T6 item6 = dataRow.get(entityMetamodel6);
              T7 item7 = dataRow.get(entityMetamodel7);
              T8 item8 = dataRow.get(entityMetamodel8);
              return new Tuple8<>(item1, item2, item3, item4, item5, item6, item7, item8);
            }));
  }

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
    List<PropertyMetamodel<?>> propertyMetamodels =
        new ArrayList<>(
            entityMetamodel1.allPropertyMetamodels().size()
                + entityMetamodel2.allPropertyMetamodels().size()
                + entityMetamodel3.allPropertyMetamodels().size()
                + entityMetamodel4.allPropertyMetamodels().size()
                + entityMetamodel5.allPropertyMetamodels().size()
                + entityMetamodel6.allPropertyMetamodels().size()
                + entityMetamodel7.allPropertyMetamodels().size()
                + entityMetamodel8.allPropertyMetamodels().size()
                + entityMetamodel9.allPropertyMetamodels().size());
    propertyMetamodels.addAll(entityMetamodel1.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel2.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel3.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel4.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel5.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel6.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel7.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel8.allPropertyMetamodels());
    propertyMetamodels.addAll(entityMetamodel9.allPropertyMetamodels());
    declaration.select(propertyMetamodels);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(entityMetamodel1);
              T2 item2 = dataRow.get(entityMetamodel2);
              T3 item3 = dataRow.get(entityMetamodel3);
              T4 item4 = dataRow.get(entityMetamodel4);
              T5 item5 = dataRow.get(entityMetamodel5);
              T6 item6 = dataRow.get(entityMetamodel6);
              T7 item7 = dataRow.get(entityMetamodel7);
              T8 item8 = dataRow.get(entityMetamodel8);
              T9 item9 = dataRow.get(entityMetamodel9);
              return new Tuple9<>(item1, item2, item3, item4, item5, item6, item7, item8, item9);
            }));
  }

  public <T> SetOperand<T> select(PropertyMetamodel<T> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    declaration.select(Collections.singletonList(propertyMetamodel));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(dataRow -> dataRow.get(propertyMetamodel)));
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      PropertyMetamodel<T1> propertyMetamodel1, PropertyMetamodel<T2> propertyMetamodel2) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    declaration.select(Arrays.asList(propertyMetamodel1, propertyMetamodel2));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              return new Tuple2<>(item1, item2);
            }));
  }

  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    declaration.select(Arrays.asList(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              return new Tuple3<>(item1, item2, item3);
            }));
  }

  public <T1, T2, T3, T4> SetOperand<Tuple4<T1, T2, T3, T4>> select(
      PropertyMetamodel<T1> propertyMetamodel1,
      PropertyMetamodel<T2> propertyMetamodel2,
      PropertyMetamodel<T3> propertyMetamodel3,
      PropertyMetamodel<T4> propertyMetamodel4) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    Objects.requireNonNull(propertyMetamodel3);
    Objects.requireNonNull(propertyMetamodel4);
    declaration.select(
        Arrays.asList(
            propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              return new Tuple4<>(item1, item2, item3, item4);
            }));
  }

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
    declaration.select(
        Arrays.asList(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              T5 item5 = dataRow.get(propertyMetamodel5);
              return new Tuple5<>(item1, item2, item3, item4, item5);
            }));
  }

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
    declaration.select(
        Arrays.asList(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              T5 item5 = dataRow.get(propertyMetamodel5);
              T6 item6 = dataRow.get(propertyMetamodel6);
              return new Tuple6<>(item1, item2, item3, item4, item5, item6);
            }));
  }

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
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              T5 item5 = dataRow.get(propertyMetamodel5);
              T6 item6 = dataRow.get(propertyMetamodel6);
              T7 item7 = dataRow.get(propertyMetamodel7);
              return new Tuple7<>(item1, item2, item3, item4, item5, item6, item7);
            }));
  }

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
    declaration.select(
        Arrays.asList(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7,
            propertyMetamodel8));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              T5 item5 = dataRow.get(propertyMetamodel5);
              T6 item6 = dataRow.get(propertyMetamodel6);
              T7 item7 = dataRow.get(propertyMetamodel7);
              T8 item8 = dataRow.get(propertyMetamodel8);
              return new Tuple8<>(item1, item2, item3, item4, item5, item6, item7, item8);
            }));
  }

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
    declaration.select(
        Arrays.asList(
            propertyMetamodel1,
            propertyMetamodel2,
            propertyMetamodel3,
            propertyMetamodel4,
            propertyMetamodel5,
            propertyMetamodel6,
            propertyMetamodel7,
            propertyMetamodel8,
            propertyMetamodel9));
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              T1 item1 = dataRow.get(propertyMetamodel1);
              T2 item2 = dataRow.get(propertyMetamodel2);
              T3 item3 = dataRow.get(propertyMetamodel3);
              T4 item4 = dataRow.get(propertyMetamodel4);
              T5 item5 = dataRow.get(propertyMetamodel5);
              T6 item6 = dataRow.get(propertyMetamodel6);
              T7 item7 = dataRow.get(propertyMetamodel7);
              T8 item8 = dataRow.get(propertyMetamodel8);
              T9 item9 = dataRow.get(propertyMetamodel9);
              return new Tuple9<>(item1, item2, item3, item4, item5, item6, item7, item8, item9);
            }));
  }

  public SetOperand<Row> select(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    for (int i = 0; i < propertyMetamodels.length; i++) {
      Objects.requireNonNull(propertyMetamodels[i], "propertyMetamodels[" + i + "]");
    }
    return selectAsRowInternal(propertyMetamodel, propertyMetamodels);
  }

  public SetOperand<Row> selectAsRow(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    for (int i = 0; i < propertyMetamodels.length; i++) {
      Objects.requireNonNull(propertyMetamodels[i], "propertyMetamodels[" + i + "]");
    }
    return selectAsRowInternal(propertyMetamodel, propertyMetamodels);
  }

  private SetOperand<Row> selectAsRowInternal(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    List<PropertyMetamodel<?>> list = new ArrayList<>(1 + propertyMetamodels.length);
    list.add(propertyMetamodel);
    list.addAll(Arrays.asList(propertyMetamodels));
    declaration.select(list);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow -> {
              Map<PropertyMetamodel<?>, Object> map = new LinkedHashMap<>();
              for (PropertyMetamodel<?> p : list) {
                Object value = dataRow.get(p);
                map.put(p, value);
              }
              return new Row() {

                @Override
                public boolean containsKey(PropertyMetamodel<?> key) {
                  return map.containsKey(key);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> propertyMetamodel) {
                  if (!map.containsKey(propertyMetamodel)) {
                    throw new DomaException(Message.DOMA6002, propertyMetamodel.getName());
                  }
                  Object value = map.get(propertyMetamodel);
                  return (PROPERTY) value;
                }

                @Override
                public Set<PropertyMetamodel<?>> keySet() {
                  return map.keySet();
                }

                @Override
                public Collection<Object> values() {
                  return map.values();
                }

                @Override
                public int size() {
                  return map.size();
                }
              };
            }));
  }

  public <RESULT> SetOperand<RESULT> selectTo(
      EntityMetamodel<RESULT> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    List<PropertyMetamodel<?>> projectionTargets = Arrays.asList(propertyMetamodels);
    declaration.selectTo(entityMetamodel, projectionTargets);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            dataRow ->
                dataRow.get(
                    entityMetamodel, declaration.getContext().getProjectionPropertyMetamodels())));
  }

  private <RESULT> Function<SelectQuery, ObjectProvider<RESULT>> createMappedResultProviderFactory(
      Function<DataRow, RESULT> rowMapper) {
    return query -> new MappedResultProvider<>(query, rowMapper);
  }

  @Override
  public SetOperationContext<ENTITY> getContext() {
    NativeSqlSelectIntermediate<ENTITY> intermediate =
        new NativeSqlSelectIntermediate<>(config, declaration, createEntityProviderFactory());
    return intermediate.getContext();
  }

  @Override
  public Stream<ENTITY> openStream() {
    NativeSqlSelectIntermediate<ENTITY> intermediate =
        new NativeSqlSelectIntermediate<>(config, declaration, createEntityProviderFactory());
    return intermediate.openStream();
  }

  @Override
  public <RESULT> RESULT mapStream(Function<Stream<ENTITY>, RESULT> streamMapper) {
    NativeSqlSelectIntermediate<ENTITY> intermediate =
        new NativeSqlSelectIntermediate<>(config, declaration, createEntityProviderFactory());
    return intermediate.mapStream(streamMapper);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    NativeSqlSelectIntermediate<ENTITY> intermediate =
        new NativeSqlSelectIntermediate<>(config, declaration, createEntityProviderFactory());
    return intermediate.createCommand();
  }

  private Function<SelectQuery, ObjectProvider<ENTITY>> createEntityProviderFactory() {
    return selectQuery -> new EntityProvider<>(entityMetamodel.asType(), selectQuery, false);
  }
}
