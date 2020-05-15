package org.seasar.doma.jdbc.criteria;

import org.seasar.doma.jdbc.criteria.declaration.AggregateFunction;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public final class AggregateFunctions {

  public static <PROPERTY> AggregateFunction.Avg<PROPERTY> avg(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    return new AggregateFunction.Avg<>(propertyMetamodel);
  }

  public static AggregateFunction.Count count() {
    return new AggregateFunction.Count(AggregateFunction.Asterisk);
  }

  public static AggregateFunction.Count count(PropertyMetamodel<?> propertyMetamodel) {
    return new AggregateFunction.Count(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Max<PROPERTY> max(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    return new AggregateFunction.Max<>(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Min<PROPERTY> min(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    return new AggregateFunction.Min<>(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Sum<PROPERTY> sum(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    return new AggregateFunction.Sum<>(propertyMetamodel);
  }
}
