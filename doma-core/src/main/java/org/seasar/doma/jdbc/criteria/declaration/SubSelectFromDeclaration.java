package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.Tuple2;
import org.seasar.doma.jdbc.criteria.context.Projection;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class SubSelectFromDeclaration {

  private final SelectFromDeclaration declaration;

  public SubSelectFromDeclaration(EntityDef<?> entityDef) {
    Objects.requireNonNull(entityDef);
    SelectContext context = new SelectContext(entityDef);
    this.declaration = new SelectFromDeclaration(context);
  }

  public SubSelectFromDeclaration innerJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    declaration.innerJoin(entityDef, block);
    return this;
  }

  public SubSelectFromDeclaration leftJoin(
      EntityDef<?> entityDef, Consumer<JoinDeclaration> block) {
    declaration.leftJoin(entityDef, block);
    return this;
  }

  public SubSelectFromDeclaration where(Consumer<WhereDeclaration> block) {
    declaration.where(block);
    return this;
  }

  public SubSelectFromDeclaration orderBy(Consumer<OrderByDeclaration> block) {
    declaration.orderBy(block);
    return this;
  }

  public <PROPERTY> SubSelectContext<PROPERTY> select(PropertyDef<PROPERTY> propertyDef) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.List(propertyDef);
    return new SubSelectContext<>(context);
  }

  public <PROPERTY1, PROPERTY2> SubSelectContext<Tuple2<PROPERTY1, PROPERTY2>> select(
      PropertyDef<PROPERTY1> first, PropertyDef<PROPERTY2> second) {
    SelectContext context = declaration.getContext();
    context.projection = new Projection.List(first, second);
    return new SubSelectContext<>(context);
  }
}
