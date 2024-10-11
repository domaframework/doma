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

public class UnifiedSelectStatement<ENTITY>
    extends AbstractStatement<UnifiedSelectStatement<ENTITY>, List<ENTITY>>
    implements SetOperand<ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;

  public UnifiedSelectStatement(
      Config config, SelectFromDeclaration declaration, EntityMetamodel<ENTITY> entityMetamodel) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
  }

  public UnifiedSelectStatement<ENTITY> distinct() {
    declaration.distinct(DistinctOption.basic());
    return this;
  }

  public UnifiedSelectStatement<ENTITY> distinct(DistinctOption distinctOption) {
    Objects.requireNonNull(distinctOption);
    declaration.distinct(distinctOption);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  public UnifiedSelectStatement<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.basic());
    return this;
  }

  public UnifiedSelectStatement<ENTITY> forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    declaration.forUpdate(option);
    return this;
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associate(first, second, associator, AssociationOption.mandatory());
    return asEntityqlSelectStarting();
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associate(first, second, associator, option);
    return asEntityqlSelectStarting();
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associateWith(first, second, associator, AssociationOption.mandatory());
    return asEntityqlSelectStarting();
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associateWith(first, second, associator, option);
    return asEntityqlSelectStarting();
  }

  private EntityqlSelectStarting<ENTITY> asEntityqlSelectStarting() {
    return new EntityqlSelectStarting<>(config, declaration, entityMetamodel);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityqlSelectTerminal<ENTITY> terminal =
        new EntityqlSelectTerminal<>(config, declaration, entityMetamodel);
    return terminal.createCommand();
  }

  public NativeSqlSelectStarting<ENTITY> groupBy(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().groupBy(propertyMetamodels);
  }

  public NativeSqlSelectStarting<ENTITY> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    return asNativeSqlSelectStarting().having(block);
  }

  public SetOperand<ENTITY> select() {
    return asNativeSqlSelectStarting().select();
  }

  public <T> SetOperand<T> select(EntityMetamodel<T> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    return asNativeSqlSelectStarting().select(entityMetamodel);
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      EntityMetamodel<T1> entityMetamodel1, EntityMetamodel<T2> entityMetamodel2) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    return asNativeSqlSelectStarting().select(entityMetamodel1, entityMetamodel2);
  }

  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      EntityMetamodel<T1> entityMetamodel1,
      EntityMetamodel<T2> entityMetamodel2,
      EntityMetamodel<T3> entityMetamodel3) {
    Objects.requireNonNull(entityMetamodel1);
    Objects.requireNonNull(entityMetamodel2);
    Objects.requireNonNull(entityMetamodel3);
    return asNativeSqlSelectStarting().select(entityMetamodel1, entityMetamodel2, entityMetamodel3);
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
    return asNativeSqlSelectStarting()
        .select(entityMetamodel1, entityMetamodel2, entityMetamodel3, entityMetamodel4);
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
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5);
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
    return asNativeSqlSelectStarting()
        .select(
            entityMetamodel1,
            entityMetamodel2,
            entityMetamodel3,
            entityMetamodel4,
            entityMetamodel5,
            entityMetamodel6);
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

  public <T> SetOperand<T> select(PropertyMetamodel<T> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return asNativeSqlSelectStarting().select(propertyMetamodel);
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      PropertyMetamodel<T1> propertyMetamodel1, PropertyMetamodel<T2> propertyMetamodel2) {
    Objects.requireNonNull(propertyMetamodel1);
    Objects.requireNonNull(propertyMetamodel2);
    return asNativeSqlSelectStarting().select(propertyMetamodel1, propertyMetamodel2);
  }

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

  public SetOperand<Row> select(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().select(propertyMetamodel, propertyMetamodels);
  }

  public SetOperand<Row> selectAsRow(
      PropertyMetamodel<?> propertyMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    return asNativeSqlSelectStarting().selectAsRow(propertyMetamodel, propertyMetamodels);
  }

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

  protected NativeSqlSelectStarting<ENTITY> asNativeSqlSelectStarting() {
    Function<SelectQuery, ObjectProvider<ENTITY>> factory =
        query -> new EntityProvider<>(entityMetamodel.asType(), query, false);
    return new NativeSqlSelectStarting<>(config, declaration, entityMetamodel, factory);
  }
}
