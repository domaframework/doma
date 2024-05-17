package org.seasar.doma.jdbc.query;

public class QueryOperandPair {
  private final QueryOperand left;
  private final QueryOperand right;

  public QueryOperandPair(QueryOperand left, QueryOperand right) {
    this.left = left;
    this.right = right;
  }

  public QueryOperand getLeft() {
    return left;
  }

  public QueryOperand getRight() {
    return right;
  }
}
