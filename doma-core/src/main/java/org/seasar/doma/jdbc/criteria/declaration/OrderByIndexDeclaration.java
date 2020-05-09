package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class OrderByIndexDeclaration {

  private final SetOperationContext<?> context;

  public OrderByIndexDeclaration(SetOperationContext<?> context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public void asc(int index) {
    add(new Pair<>(new OrderByItem.Index(index), "asc"));
  }

  public void desc(int index) {
    add(new Pair<>(new OrderByItem.Index(index), "desc"));
  }

  private void add(Pair<OrderByItem.Index, String> pair) {
    context.accept(
        new SetOperationContext.Visitor<Void>() {
          @Override
          public Void visit(SetOperationContext.Select<?> select) {
            return null;
          }

          @Override
          public Void visit(SetOperationContext.Union<?> union) {
            union.orderBy.add(pair);
            return null;
          }

          @Override
          public Void visit(SetOperationContext.UnionAll<?> unionAll) {
            unionAll.orderBy.add(pair);
            return null;
          }
        });
  }
}
