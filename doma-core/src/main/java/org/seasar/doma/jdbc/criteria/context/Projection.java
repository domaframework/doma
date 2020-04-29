package org.seasar.doma.jdbc.criteria.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import org.seasar.doma.def.PropertyDef;

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
    public final java.util.List<PropertyDef<?>> propertyDefs;

    public List(PropertyDef<?>... propertyDefs) {
      this(Arrays.asList(propertyDefs));
    }

    public List(java.util.List<PropertyDef<?>> propertyDefs) {
      Objects.requireNonNull(propertyDefs);
      this.propertyDefs = Collections.unmodifiableList(propertyDefs);
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
