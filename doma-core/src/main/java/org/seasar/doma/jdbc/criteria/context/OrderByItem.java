package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface OrderByItem {

  void accept(Visitor visitor);

  class Name implements OrderByItem {
    public final PropertyMetamodel<?> value;

    public Name(PropertyMetamodel<?> value) {
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
