package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.AssociationKind;
import org.seasar.doma.jdbc.criteria.context.ForUpdate;
import org.seasar.doma.jdbc.criteria.context.Join;
import org.seasar.doma.jdbc.criteria.context.JoinKind;
import org.seasar.doma.jdbc.criteria.context.Projection;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.message.Message;

public class SelectFromDeclaration {

  private final SelectContext context;

  public SelectFromDeclaration(SelectContext context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public SelectContext getContext() {
    return context;
  }

  public void innerJoin(EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    join(entityDef, block, JoinKind.INNER);
  }

  public void leftJoin(EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    join(entityDef, block, JoinKind.LEFT);
  }

  private void join(EntityDef<?> entityDef, Consumer<JoinDeclaration> block, JoinKind joinKind) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(block);
    Objects.requireNonNull(joinKind);
    Join join = new Join(entityDef, joinKind);
    JoinDeclaration declaration = new JoinDeclaration(join);
    block.accept(declaration);
    if (!join.on.isEmpty()) {
      context.joins.add(join);
    }
  }

  public void where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    WhereDeclaration declaration = new WhereDeclaration(context);
    block.accept(declaration);
  }

  public void groupBy(PropertyDef<?>... propertyDefs) {
    context.groupBy.addAll(Arrays.asList(propertyDefs));
  }

  public void having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    HavingDeclaration declaration = new HavingDeclaration(context);
    block.accept(declaration);
  }

  public void orderBy(Consumer<OrderByDeclaration> block) {
    Objects.requireNonNull(block);
    OrderByDeclaration declaration = new OrderByDeclaration(context);
    block.accept(declaration);
  }

  public void limit(Integer limit) {
    context.limit = limit;
  }

  public void offset(Integer offset) {
    context.offset = offset;
  }

  public void forUpdate() {
    context.forUpdate = new ForUpdate(false);
  }

  public void forUpdate(boolean nowait) {
    context.forUpdate = new ForUpdate(nowait);
  }

  public void select(PropertyDef<?>... propertyDefs) {
    context.projection = new Projection.List(propertyDefs);
  }

  @SuppressWarnings("unchecked")
  public <RESULT> void map(Function<Row, RESULT> mapper) {
    context.mapper = (Function<Row, Object>) mapper;
  }

  @SuppressWarnings("unchecked")
  public <ENTITY1, ENTITY2> void associate(
      EntityDef<ENTITY1> first,
      EntityDef<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationKind kind) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(kind);
    if (!context.getEntityDefs().contains(first)) {
      if (kind == AssociationKind.MANDATORY) {
        throw new DomaException(Message.DOMA6001, "first");
      }
      return;
    }
    if (!context.getEntityDefs().contains(second)) {
      if (kind == AssociationKind.MANDATORY) {
        throw new DomaException(Message.DOMA6001, "second");
      }
      return;
    }
    //noinspection unchecked
    context.associations.put(new Pair<>(first, second), (BiConsumer<Object, Object>) associator);
  }
}
