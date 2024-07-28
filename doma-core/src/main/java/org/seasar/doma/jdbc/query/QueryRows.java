package org.seasar.doma.jdbc.query;

import java.util.List;

public class QueryRows {
  private final List<QueryOperandPairList> rows;

  public QueryRows(List<QueryOperandPairList> rows) {
    this.rows = rows;
  }

  public List<QueryOperandPairList> getRows() {
    return rows;
  }

  public boolean isEmpty() {
    return rows.isEmpty();
  }

  public boolean hasEmptyValueRow() {
    return rows.stream().anyMatch(QueryOperandPairList::isEmpty);
  }

  public QueryOperandPairList first() {
    return rows.get(0);
  }
}
