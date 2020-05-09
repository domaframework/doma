package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class OrderByNameDeclaration {

  private final SelectContext context;

  public OrderByNameDeclaration(SelectContext context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public void asc(PropertyDef<?> propertyDef) {
    Objects.requireNonNull(propertyDef);
    Pair<OrderByItem, String> pair = new Pair<>(new OrderByItem.Name(propertyDef), "asc");
    context.orderBy.add(pair);
  }

  public void desc(PropertyDef<?> propertyDef) {
    Objects.requireNonNull(propertyDef);
    Pair<OrderByItem, String> pair = new Pair<>(new OrderByItem.Name(propertyDef), "desc");
    context.orderBy.add(pair);
  }
}
