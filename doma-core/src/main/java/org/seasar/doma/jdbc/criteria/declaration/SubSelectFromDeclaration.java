package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.Projection;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

public class SubSelectFromDeclaration {

  private final SelectFromDeclaration declaration;

  public SubSelectFromDeclaration(EntityMetamodel<?> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    SelectContext context = new SelectContext(entityMetamodel);
    this.declaration = new SelectFromDeclaration(context);
  }

  public SubSelectFromDeclaration innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  public SubSelectFromDeclaration leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  public SubSelectFromDeclaration where(Consumer<WhereDeclaration> block) {
    declaration.where(block);
    return this;
  }

  public SubSelectFromDeclaration orderBy(Consumer<OrderByNameDeclaration> block) {
    declaration.orderBy(block);
    return this;
  }

  public <PROPERTY> SubSelectContext<PROPERTY> select(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.List(propertyMetamodel);
    return new SubSelectContext<>(context);
  }

  public <PROPERTY1, PROPERTY2> SubSelectContext<Tuple2<PROPERTY1, PROPERTY2>> select(
      PropertyMetamodel<PROPERTY1> first, PropertyMetamodel<PROPERTY2> second) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.List(first, second);
    return new SubSelectContext<>(context);
  }
}
