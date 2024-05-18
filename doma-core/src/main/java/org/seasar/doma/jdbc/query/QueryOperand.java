package org.seasar.doma.jdbc.query;

import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface QueryOperand {

  EntityPropertyType<?, ?> getEntityPropertyType();

  void accept(QueryOperand.Visitor visitor);

  final class Param implements QueryOperand {
    public final EntityPropertyType<?, ?> propertyType;
    public final InParameter<?> inParameter;

    public Param(EntityPropertyType<?, ?> propertyType, InParameter<?> inParameter) {
      this.propertyType = Objects.requireNonNull(propertyType);
      this.inParameter = Objects.requireNonNull(inParameter);
    }

    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  final class Prop implements QueryOperand {
    public final EntityPropertyType<?, ?> propertyType;

    public Prop(EntityPropertyType<?, ?> propertyType) {
      this.propertyType = Objects.requireNonNull(propertyType);
    }

    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(QueryOperand.Param param);

    void visit(QueryOperand.Prop prop);
  }
}
