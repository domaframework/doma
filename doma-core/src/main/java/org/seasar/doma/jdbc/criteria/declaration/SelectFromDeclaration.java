package org.seasar.doma.jdbc.criteria.declaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.ForUpdate;
import org.seasar.doma.jdbc.criteria.context.Join;
import org.seasar.doma.jdbc.criteria.context.JoinKind;
import org.seasar.doma.jdbc.criteria.context.Projection;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.message.Message;

public class SelectFromDeclaration {

  private final SelectContext context;

  public SelectFromDeclaration(SelectContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public SelectContext getContext() {
    return context;
  }

  public void distinct(DistinctOption distinctOption) {
    context.distinct = distinctOption;
  }

  public void innerJoin(EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    join(entityMetamodel, block, JoinKind.INNER);
  }

  public void leftJoin(EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    join(entityMetamodel, block, JoinKind.LEFT);
  }

  private void join(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block, JoinKind joinKind) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    Objects.requireNonNull(joinKind);
    Join join = new Join(entityMetamodel, joinKind);
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

  public void groupBy(PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    context.groupBy.addAll(Arrays.asList(propertyMetamodels));
  }

  public void having(Consumer<HavingDeclaration> block) {
    Objects.requireNonNull(block);
    HavingDeclaration declaration = new HavingDeclaration(context);
    block.accept(declaration);
  }

  public void orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    OrderByNameDeclaration declaration = new OrderByNameDeclaration(context);
    block.accept(declaration);
  }

  public void limit(Integer limit) {
    context.limit = limit;
  }

  public void offset(Integer offset) {
    context.offset = offset;
  }

  public void forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    context.forUpdate = new ForUpdate(option);
  }

  public void select(List<PropertyMetamodel<?>> propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    int i = 0;
    for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodel, "propertyMetamodels: " + i);
    }
    context.projection = new Projection.PropertyMetamodels(propertyMetamodels);
  }

  public void select(EntityMetamodel<?> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    if (!context.getEntityMetamodels().contains(entityMetamodel)) {
      throw new DomaException(Message.DOMA6009, "entityMetamodel");
    }
    context.projection = new Projection.EntityMetamodels(entityMetamodel);
  }

  public void selectTo(
      EntityMetamodel<?> entityMetamodel, List<PropertyMetamodel<?>> propertyMetamodels) {
    Objects.requireNonNull(propertyMetamodels);
    if (!context.getEntityMetamodels().contains(entityMetamodel)) {
      throw new DomaException(Message.DOMA6007, "entityMetamodel");
    }
    int i = 0;
    for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodel, "propertyMetamodels: " + i);
      if (!entityMetamodel.allPropertyMetamodels().contains(propertyMetamodel)) {
        throw new DomaException(Message.DOMA6008, i);
      }
    }
    Set<PropertyMetamodel<?>> projectionTargets = new LinkedHashSet<>();
    EntityType<?> entityType = entityMetamodel.asType();
    List<? extends EntityPropertyType<?, ?>> idPropertyTypes = entityType.getIdPropertyTypes();
    entityMetamodel.allPropertyMetamodels().stream()
        .filter(it -> idPropertyTypes.contains(it.asType()))
        .forEach(projectionTargets::add);
    projectionTargets.addAll(propertyMetamodels);
    context.projection =
        new Projection.EntityMetamodels(entityMetamodel, new ArrayList<>(projectionTargets));
  }

  @SuppressWarnings("unchecked")
  public <ENTITY1, ENTITY2> void associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    if (!context.getEntityMetamodels().contains(first)) {
      if (option == AssociationOption.Kind.MANDATORY) {
        throw new DomaException(Message.DOMA6001, "first");
      }
      return;
    }
    if (!context.getEntityMetamodels().contains(second)) {
      if (option == AssociationOption.Kind.MANDATORY) {
        throw new DomaException(Message.DOMA6001, "second");
      }
      return;
    }
    context.associations.put(
        new Pair<>(first, second),
        (entity1, entity2) -> {
          associator.accept((ENTITY1) entity1, (ENTITY2) entity2);
          return null;
        });
  }

  @SuppressWarnings("unchecked")
  public <ENTITY1, ENTITY2> void associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    if (!context.getEntityMetamodels().contains(first)) {
      if (option == AssociationOption.Kind.MANDATORY) {
        throw new DomaException(Message.DOMA6010, "first");
      }
      return;
    }
    if (!context.getEntityMetamodels().contains(second)) {
      if (option == AssociationOption.Kind.MANDATORY) {
        throw new DomaException(Message.DOMA6010, "second");
      }
      return;
    }
    context.associations.put(
        new Pair<>(first, second),
        (entity1, entity2) -> associator.apply((ENTITY1) entity1, (ENTITY2) entity2));
  }
}
