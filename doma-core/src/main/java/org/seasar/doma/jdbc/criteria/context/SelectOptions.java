package org.seasar.doma.jdbc.criteria.context;

public class SelectOptions extends Options {
  private int fetchSize = 0;
  private int maxRows = 0;

  public int getFetchSize() {
    return fetchSize;
  }

  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }
}
