package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public interface OrderByItem {

  void accept(Visitor visitor);

  class Name implements OrderByItem {
    public final PropertyDef<?> value;

    public Name(PropertyDef<?> value) {
      this.value = Objects.requireNonNull(value);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Index implements OrderByItem {
    public final int value;

    public Index(int value) {
      this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(Name name);

    void visit(Index index);
  }
}
