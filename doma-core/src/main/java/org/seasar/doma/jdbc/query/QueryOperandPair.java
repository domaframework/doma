package org.seasar.doma.jdbc.query;

import java.util.Objects;

public class QueryOperandPair {
  private final QueryOperand left;
  private final QueryOperand right;

  public QueryOperandPair(QueryOperand left, QueryOperand right) {
    this.left = Objects.requireNonNull(left);
    this.right = Objects.requireNonNull(right);
  }

  public QueryOperand getLeft() {
    return left;
  }

  public QueryOperand getRight() {
    return right;
  }
}
