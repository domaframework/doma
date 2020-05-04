package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class OrderByDeclaration {

  private final SelectContext context;

  public OrderByDeclaration(SelectContext context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public void asc(PropertyDef<?> propertyDef) {
    Objects.requireNonNull(propertyDef);
    Pair<PropertyDef<?>, String> pair = new Pair<>(propertyDef, "asc");
    context.orderBy.add(pair);
  }

  public void desc(PropertyDef<?> propertyDef) {
    Objects.requireNonNull(propertyDef);
    Pair<PropertyDef<?>, String> pair = new Pair<>(propertyDef, "desc");
    context.orderBy.add(pair);
  }
}
