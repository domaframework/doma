package org.seasar.doma.jdbc.query;

import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface UpsertSetValue {
  void accept(UpsertSetValue.Visitor visitor);

  final class Param implements UpsertSetValue {
    public final InParameter<?> inParameter;

    public Param(InParameter<?> inParameter) {
      this.inParameter = Objects.requireNonNull(inParameter);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  final class Prop implements UpsertSetValue {
    public final EntityPropertyType<?, ?> propertyType;

    public Prop(EntityPropertyType<?, ?> propertyType) {
      this.propertyType = Objects.requireNonNull(propertyType);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(UpsertSetValue.Param param);

    void visit(UpsertSetValue.Prop prop);
  }
}
