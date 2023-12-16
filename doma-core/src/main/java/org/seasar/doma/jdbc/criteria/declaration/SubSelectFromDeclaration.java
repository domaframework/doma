package org.seasar.doma.jdbc.criteria.declaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.Projection;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;

public class SubSelectFromDeclaration<ENTITY> implements SubSelectContext<ENTITY> {

  private final SelectFromDeclaration declaration;

  public SubSelectFromDeclaration(EntityMetamodel<?> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    SelectContext context = new SelectContext(entityMetamodel, Optional.empty());
    this.declaration = new SelectFromDeclaration(context);
  }

  @Override
  public SelectContext get() {
    return declaration.getContext();
  }

  public SubSelectFromDeclaration<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  public SubSelectFromDeclaration<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  public SubSelectFromDeclaration<ENTITY> where(Consumer<WhereDeclaration> block) {
    declaration.where(block);
    return this;
  }

  public SubSelectFromDeclaration<ENTITY> groupBy(PropertyMetamodel<?>... propertyMetamodels) {
    declaration.groupBy(propertyMetamodels);
    return this;
  }

  public SubSelectFromDeclaration<ENTITY> having(Consumer<HavingDeclaration> block) {
    declaration.having(block);
    return this;
  }

  public SubSelectFromDeclaration<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    declaration.orderBy(block);
    return this;
  }

  public SubSelectContext<EntityMetamodel<ENTITY>> select() {
    SelectContext context = declaration.getContext();
    return () -> context;
  }

  public SubSelectContext<List<PropertyMetamodel<?>>> select(EntityMetamodel<?> entityMetamodel) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.PropertyMetamodels(entityMetamodel.allPropertyMetamodels());
    return () -> context;
  }

  public <PROPERTY> Single<PROPERTY> select(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.PropertyMetamodels(propertyMetamodel);
    return new Single<>(context, propertyMetamodel);
  }

  public <PROPERTY1, PROPERTY2>
      SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> select(
          PropertyMetamodel<PROPERTY1> first, PropertyMetamodel<PROPERTY2> second) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.PropertyMetamodels(first, second);
    return () -> context;
  }

  public <PROPERTY1, PROPERTY2, PROPERTY3>
      SubSelectContext<
              Tuple3<
                  PropertyMetamodel<PROPERTY1>,
                  PropertyMetamodel<PROPERTY2>,
                  PropertyMetamodel<PROPERTY3>>>
          select(
              PropertyMetamodel<PROPERTY1> first,
              PropertyMetamodel<PROPERTY2> second,
              PropertyMetamodel<PROPERTY3> third) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.PropertyMetamodels(first, second, third);
    return () -> context;
  }

  public SubSelectContext<List<PropertyMetamodel<?>>> select(
      PropertyMetamodel<?> propertyMetamodel1,
      PropertyMetamodel<?> propertyMetamodel2,
      PropertyMetamodel<?>... propertyMetamodels) {
    SelectContext context = declaration.getContext();
    List<PropertyMetamodel<?>> list = new ArrayList<>(2 + propertyMetamodels.length);
    list.add(propertyMetamodel1);
    list.add(propertyMetamodel2);
    list.addAll(Arrays.asList(propertyMetamodels));
    context.projection = new Projection.PropertyMetamodels(list);
    return () -> context;
  }
}
