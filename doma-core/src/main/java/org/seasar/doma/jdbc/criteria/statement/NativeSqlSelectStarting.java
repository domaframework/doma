package org.seasar.doma.jdbc.criteria.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.command.MappedResultProvider;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
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
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;
import org.seasar.doma.jdbc.criteria.tuple.Tuple4;
import org.seasar.doma.jdbc.criteria.tuple.Tuple5;
import org.seasar.doma.jdbc.criteria.tuple.Tuple6;
import org.seasar.doma.jdbc.criteria.tuple.Tuple7;
import org.seasar.doma.jdbc.criteria.tuple.Tuple8;
import org.seasar.doma.jdbc.criteria.tuple.Tuple9;
import org.seasar.doma.jdbc.query.SelectQuery;

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
    declaration.distinct(DistinctOption.ENABLED);
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
    declaration.forUpdate(ForUpdateOption.WAIT);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    declaration.forUpdate(option);
    return this;
  }

  public <T> SetOperand<T> select(PropertyMetamodel<T> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    declaration.select(propertyMetamodel);
    return new NativeSqlSelectIntermediate<>(
        config, declaration, createMappedResultProviderFactory(row -> row.get(propertyMetamodel)));
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      PropertyMetamodel<T1> propertyMetamodel1, PropertyMetamodel<T2> propertyMetamodel2) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    declaration.select(propertyMetamodel1, propertyMetamodel2);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
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
    declaration.select(propertyMetamodel1, propertyMetamodel2, propertyMetamodel3);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
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
        propertyMetamodel1, propertyMetamodel2, propertyMetamodel3, propertyMetamodel4);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
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
        propertyMetamodel1,
        propertyMetamodel2,
        propertyMetamodel3,
        propertyMetamodel4,
        propertyMetamodel5);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
              T5 item5 = row.get(propertyMetamodel5);
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
        propertyMetamodel1,
        propertyMetamodel2,
        propertyMetamodel3,
        propertyMetamodel4,
        propertyMetamodel5,
        propertyMetamodel6);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
              T5 item5 = row.get(propertyMetamodel5);
              T6 item6 = row.get(propertyMetamodel6);
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
        propertyMetamodel1,
        propertyMetamodel2,
        propertyMetamodel3,
        propertyMetamodel4,
        propertyMetamodel5,
        propertyMetamodel6,
        propertyMetamodel7);
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
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
              T5 item5 = row.get(propertyMetamodel5);
              T6 item6 = row.get(propertyMetamodel6);
              T7 item7 = row.get(propertyMetamodel7);
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
        propertyMetamodel1,
        propertyMetamodel2,
        propertyMetamodel3,
        propertyMetamodel4,
        propertyMetamodel5,
        propertyMetamodel6,
        propertyMetamodel7,
        propertyMetamodel8);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
              T5 item5 = row.get(propertyMetamodel5);
              T6 item6 = row.get(propertyMetamodel6);
              T7 item7 = row.get(propertyMetamodel7);
              T8 item8 = row.get(propertyMetamodel8);
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
        propertyMetamodel1,
        propertyMetamodel2,
        propertyMetamodel3,
        propertyMetamodel4,
        propertyMetamodel5,
        propertyMetamodel6,
        propertyMetamodel7,
        propertyMetamodel8,
        propertyMetamodel9);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyMetamodel1);
              T2 item2 = row.get(propertyMetamodel2);
              T3 item3 = row.get(propertyMetamodel3);
              T4 item4 = row.get(propertyMetamodel4);
              T5 item5 = row.get(propertyMetamodel5);
              T6 item6 = row.get(propertyMetamodel6);
              T7 item7 = row.get(propertyMetamodel7);
              T8 item8 = row.get(propertyMetamodel8);
              T9 item9 = row.get(propertyMetamodel9);
              return new Tuple9<>(item1, item2, item3, item4, item5, item6, item7, item8, item9);
            }));
  }

  public SetOperand<List<Object>> select(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    for (int i = 0; i < propertyMetamodels.length; i++) {
      Objects.requireNonNull(propertyMetamodels[i], "propertyMetamodels[" + i + "]");
    }
    List<PropertyMetamodel<?>> list;
    if (propertyMetamodels.length == 0) {
      SelectContext context = declaration.getContext();
      list = context.allPropertyMetamodels();
    } else {
      declaration.select(propertyMetamodels);
      list = Arrays.asList(propertyMetamodels);
    }
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              List<Object> results = new ArrayList<>();
              for (PropertyMetamodel<?> propertyMetamodel : list) {
                Object value = row.get(propertyMetamodel);
                results.add(value);
              }
              return results;
            }));
  }

  private <RESULT> Function<SelectQuery, ObjectProvider<RESULT>> createMappedResultProviderFactory(
      Function<Row, RESULT> rowMapper) {
    return query -> new MappedResultProvider<>(query, rowMapper);
  }

  @Override
  public SetOperationContext<ENTITY> getContext() {
    NativeSqlSelectIntermediate<ENTITY> intermediate =
        new NativeSqlSelectIntermediate<>(config, declaration, createEntityProviderFactory());
    return intermediate.getContext();
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
