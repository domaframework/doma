package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;

public interface SetOperationContext<ELEMENT> {

  <R> R accept(Visitor<R> visitor);

  class Select<ELEMENT> implements SetOperationContext<ELEMENT> {
    public final SelectContext context;

    public Select(SelectContext context) {
      Objects.requireNonNull(context);
      this.context = context;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
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
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
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
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }
  }

  interface Visitor<R> {
    R visit(Select<?> select);

    R visit(Union<?> union);

    R visit(UnionAll<?> unionAll);
  }
}
