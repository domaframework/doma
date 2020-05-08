package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.command.MappedResultProvider;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.HavingDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
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
  private final EntityDef<ENTITY> entityDef;

  public NativeSqlSelectStarting(
      Config config,
      SelectFromDeclaration declaration,
      EntityDef<ENTITY> entityDef,
      Function<SelectQuery, ObjectProvider<ENTITY>> objectProviderFactory) {
    super(Objects.requireNonNull(config), Objects.requireNonNull(objectProviderFactory));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityDef = Objects.requireNonNull(entityDef);
  }

  public NativeSqlSelectStarting<ENTITY> innerJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> leftJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityDef, block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> groupBy(PropertyDef<?>... propertyDefs) {
    Objects.requireNonNull(propertyDefs);
    declaration.groupBy(propertyDefs);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.having(block);
    return this;
  }

  public NativeSqlSelectStarting<ENTITY> orderBy(Consumer<OrderByDeclaration> block) {
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
    declaration.forUpdate(option);
    return this;
  }

  public <T> SetOperand<T> select(PropertyDef<T> propertyDef) {
    declaration.select(propertyDef);
    return new NativeSqlSelectIntermediate<>(
        config, declaration, createMappedResultProviderFactory(row -> row.get(propertyDef)));
  }

  public <T1, T2> SetOperand<Tuple2<T1, T2>> select(
      PropertyDef<T1> propertyDef1, PropertyDef<T2> propertyDef2) {
    declaration.select(propertyDef1, propertyDef2);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              return new Tuple2<>(item1, item2);
            }));
  }

  public <T1, T2, T3> SetOperand<Tuple3<T1, T2, T3>> select(
      PropertyDef<T1> propertyDef1, PropertyDef<T2> propertyDef2, PropertyDef<T3> propertyDef3) {
    declaration.select(propertyDef1, propertyDef2, propertyDef3);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              return new Tuple3<>(item1, item2, item3);
            }));
  }

  public <T1, T2, T3, T4> SetOperand<Tuple4<T1, T2, T3, T4>> select(
      PropertyDef<T1> propertyDef1,
      PropertyDef<T2> propertyDef2,
      PropertyDef<T3> propertyDef3,
      PropertyDef<T4> propertyDef4) {
    declaration.select(propertyDef1, propertyDef2, propertyDef3, propertyDef4);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              return new Tuple4<>(item1, item2, item3, item4);
            }));
  }

  public <T1, T2, T3, T4, T5> SetOperand<Tuple5<T1, T2, T3, T4, T5>> select(
      PropertyDef<T1> propertyDef1,
      PropertyDef<T2> propertyDef2,
      PropertyDef<T3> propertyDef3,
      PropertyDef<T4> propertyDef4,
      PropertyDef<T5> propertyDef5) {
    declaration.select(propertyDef1, propertyDef2, propertyDef3, propertyDef4, propertyDef5);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              T5 item5 = row.get(propertyDef5);
              return new Tuple5<>(item1, item2, item3, item4, item5);
            }));
  }

  public <T1, T2, T3, T4, T5, T6> SetOperand<Tuple6<T1, T2, T3, T4, T5, T6>> select(
      PropertyDef<T1> propertyDef1,
      PropertyDef<T2> propertyDef2,
      PropertyDef<T3> propertyDef3,
      PropertyDef<T4> propertyDef4,
      PropertyDef<T5> propertyDef5,
      PropertyDef<T6> propertyDef6) {
    declaration.select(
        propertyDef1, propertyDef2, propertyDef3, propertyDef4, propertyDef5, propertyDef6);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              T5 item5 = row.get(propertyDef5);
              T6 item6 = row.get(propertyDef6);
              return new Tuple6<>(item1, item2, item3, item4, item5, item6);
            }));
  }

  public <T1, T2, T3, T4, T5, T6, T7> SetOperand<Tuple7<T1, T2, T3, T4, T5, T6, T7>> select(
      PropertyDef<T1> propertyDef1,
      PropertyDef<T2> propertyDef2,
      PropertyDef<T3> propertyDef3,
      PropertyDef<T4> propertyDef4,
      PropertyDef<T5> propertyDef5,
      PropertyDef<T6> propertyDef6,
      PropertyDef<T7> propertyDef7) {
    declaration.select(
        propertyDef1,
        propertyDef2,
        propertyDef3,
        propertyDef4,
        propertyDef5,
        propertyDef6,
        propertyDef7);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              T5 item5 = row.get(propertyDef5);
              T6 item6 = row.get(propertyDef6);
              T7 item7 = row.get(propertyDef7);
              return new Tuple7<>(item1, item2, item3, item4, item5, item6, item7);
            }));
  }

  public <T1, T2, T3, T4, T5, T6, T7, T8> SetOperand<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> select(
      PropertyDef<T1> propertyDef1,
      PropertyDef<T2> propertyDef2,
      PropertyDef<T3> propertyDef3,
      PropertyDef<T4> propertyDef4,
      PropertyDef<T5> propertyDef5,
      PropertyDef<T6> propertyDef6,
      PropertyDef<T7> propertyDef7,
      PropertyDef<T8> propertyDef8) {
    declaration.select(
        propertyDef1,
        propertyDef2,
        propertyDef3,
        propertyDef4,
        propertyDef5,
        propertyDef6,
        propertyDef7,
        propertyDef8);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              T5 item5 = row.get(propertyDef5);
              T6 item6 = row.get(propertyDef6);
              T7 item7 = row.get(propertyDef7);
              T8 item8 = row.get(propertyDef8);
              return new Tuple8<>(item1, item2, item3, item4, item5, item6, item7, item8);
            }));
  }

  public <T1, T2, T3, T4, T5, T6, T7, T8, T9>
      SetOperand<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(
          PropertyDef<T1> propertyDef1,
          PropertyDef<T2> propertyDef2,
          PropertyDef<T3> propertyDef3,
          PropertyDef<T4> propertyDef4,
          PropertyDef<T5> propertyDef5,
          PropertyDef<T6> propertyDef6,
          PropertyDef<T7> propertyDef7,
          PropertyDef<T8> propertyDef8,
          PropertyDef<T9> propertyDef9) {
    declaration.select(
        propertyDef1,
        propertyDef2,
        propertyDef3,
        propertyDef4,
        propertyDef5,
        propertyDef6,
        propertyDef7,
        propertyDef8,
        propertyDef9);
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              T1 item1 = row.get(propertyDef1);
              T2 item2 = row.get(propertyDef2);
              T3 item3 = row.get(propertyDef3);
              T4 item4 = row.get(propertyDef4);
              T5 item5 = row.get(propertyDef5);
              T6 item6 = row.get(propertyDef6);
              T7 item7 = row.get(propertyDef7);
              T8 item8 = row.get(propertyDef8);
              T9 item9 = row.get(propertyDef9);
              return new Tuple9<>(item1, item2, item3, item4, item5, item6, item7, item8, item9);
            }));
  }

  public SetOperand<Map<PropertyDef<?>, Object>> select(PropertyDef<?>... propertyDefs) {
    List<PropertyDef<?>> list;
    if (propertyDefs.length == 0) {
      SelectContext context = declaration.getContext();
      list = context.allPropertyDefs();
    } else {
      declaration.select(propertyDefs);
      list = Arrays.asList(propertyDefs);
    }
    return new NativeSqlSelectIntermediate<>(
        config,
        declaration,
        createMappedResultProviderFactory(
            row -> {
              LinkedHashMap<PropertyDef<?>, Object> map = new LinkedHashMap<>(list.size());
              for (PropertyDef<?> propertyDef : list) {
                Object value = row.get(propertyDef);
                map.put(propertyDef, value);
              }
              return map;
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
    return selectQuery -> new EntityProvider<>(entityDef.asType(), selectQuery, false);
  }
}
