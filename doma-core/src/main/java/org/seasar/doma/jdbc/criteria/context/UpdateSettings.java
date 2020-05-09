package org.seasar.doma.jdbc.criteria.context;

public class UpdateSettings extends Settings {
  int batchSize = 0;
  boolean allowEmptyWhere;

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public boolean getAllowEmptyWhere() {
    return allowEmptyWhere;
  }

  public void setAllowEmptyWhere(boolean allowEmptyWhere) {
    this.allowEmptyWhere = allowEmptyWhere;
  }
}
