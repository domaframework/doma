package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;

public interface SetOperationContext<ELEMENT> {

  void accept(Visitor visitor);

  class Select<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SelectContext context;

    public Select(SelectContext context) {
      Objects.requireNonNull(context);
      this.context = context;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class Union<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SetOperationContext<ELEMENT> left;
    public final SetOperationContext<ELEMENT> right;

    public Union(SetOperationContext<ELEMENT> left, SetOperationContext<ELEMENT> right) {
      Objects.requireNonNull(left);
      Objects.requireNonNull(right);
      this.left = left;
      this.right = right;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  class UnionAll<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SetOperationContext<ELEMENT> left;
    public final SetOperationContext<ELEMENT> right;

    public UnionAll(SetOperationContext<ELEMENT> left, SetOperationContext<ELEMENT> right) {
      Objects.requireNonNull(left);
      Objects.requireNonNull(right);
      this.left = left;
      this.right = right;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(Select<?> select);

    void visit(Union<?> union);

    void visit(UnionAll<?> unionAll);
  }
}
