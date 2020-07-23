package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class OrderByNameDeclaration {

  private final SelectContext context;

  public OrderByNameDeclaration(SelectContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public void asc(PropertyMetamodel<?> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    Pair<OrderByItem, String> pair = new Pair<>(new OrderByItem.Name(propertyMetamodel), "asc");
    context.orderBy.add(pair);
  }

  public void desc(PropertyMetamodel<?> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    Pair<OrderByItem, String> pair = new Pair<>(new OrderByItem.Name(propertyMetamodel), "desc");
    context.orderBy.add(pair);
  }
}
