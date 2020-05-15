package org.seasar.doma.jdbc.criteria.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface Projection {
  All All = new All() {};

  <R> R accept(Visitor<R> visitor);

  class All implements Projection {
    private All() {}

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  class List implements Projection {
    public final java.util.List<PropertyMetamodel<?>> propertyMetamodels;

    public List(PropertyMetamodel<?>... propertyMetamodels) {
      this(Arrays.asList(propertyMetamodels));
    }

    public List(java.util.List<PropertyMetamodel<?>> propertyMetamodels) {
      Objects.requireNonNull(propertyMetamodels);
      this.propertyMetamodels = Collections.unmodifiableList(propertyMetamodels);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  interface Visitor<R> {
    R visit(All all);

    R visit(List list);
  }
}
