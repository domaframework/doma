package org.seasar.doma.jdbc.criteria;

import org.seasar.doma.jdbc.criteria.declaration.AggregateFunction;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public final class AggregateFunctions {

  public static <PROPERTY> AggregateFunction.Avg<PROPERTY> avg(PropertyDef<PROPERTY> propertyDef) {
    return new AggregateFunction.Avg<>(propertyDef);
  }

  public static AggregateFunction.Count count() {
    return new AggregateFunction.Count(AggregateFunction.Asterisk);
  }

  public static AggregateFunction.Count count(PropertyDef<?> propertyDef) {
    return new AggregateFunction.Count(propertyDef);
  }

  public static <PROPERTY> AggregateFunction.Max<PROPERTY> max(PropertyDef<PROPERTY> propertyDef) {
    return new AggregateFunction.Max<>(propertyDef);
  }

  public static <PROPERTY> AggregateFunction.Min<PROPERTY> min(PropertyDef<PROPERTY> propertyDef) {
    return new AggregateFunction.Min<>(propertyDef);
  }

  public static <PROPERTY> AggregateFunction.Sum<PROPERTY> sum(PropertyDef<PROPERTY> propertyDef) {
    return new AggregateFunction.Sum<>(propertyDef);
  }
}
