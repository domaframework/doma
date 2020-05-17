package org.seasar.doma.jdbc.criteria.context;

public class SelectSettings extends Settings {
  private boolean allowEmptyWhere = true;
  private int fetchSize = 0;
  private int maxRows = 0;

  public boolean getAllowEmptyWhere() {
    return allowEmptyWhere;
  }

  public void setAllowEmptyWhere(boolean allowEmptyWhere) {
    this.allowEmptyWhere = allowEmptyWhere;
  }

  public int getFetchSize() {
    return fetchSize;
  }

  public void setFetchSize(int fetchSize) {
    this.fetchSize = fetchSize;
  }

  public int getMaxRows() {
    return maxRows;
  }

  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }
}
