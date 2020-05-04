package org.seasar.doma.jdbc.criteria.context;

import java.util.function.Function;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public interface Operand {

  <R> R accept(Visitor<R> visitor);

  class Param implements Operand {
    public final Function<ClassHelper, InParameter<?>> value;

    public Param(Function<ClassHelper, InParameter<?>> value) {
      this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  class Prop implements Operand {
    public final PropertyDef<?> value;

    public Prop(PropertyDef<?> value) {
      this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  interface Visitor<R> {

    R visit(Param operand);

    R visit(Prop operand);
  }
}
