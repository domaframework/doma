package org.seasar.doma.jdbc.criteria.context;

public class InsertSettings extends Settings {
  private int batchSize = 0;
  private boolean excludeNull;

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public boolean getExcludeNull() {
    return excludeNull;
  }

  public void setExcludeNull(boolean excludeNull) {
    this.excludeNull = excludeNull;
  }
}
