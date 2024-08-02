package org.seasar.doma.jdbc.query;

import java.util.List;

public class QueryOperandPairList {
  private final List<QueryOperandPair> pairs;

  public QueryOperandPairList(List<QueryOperandPair> pairs) {
    this.pairs = pairs;
  }

  public List<QueryOperandPair> getPairs() {
    return pairs;
  }

  public boolean isEmpty() {
    return pairs.isEmpty();
  }
}
