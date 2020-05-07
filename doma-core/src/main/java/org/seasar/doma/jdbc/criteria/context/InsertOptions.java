package org.seasar.doma.jdbc.criteria.context;

public class InsertOptions {
  int batchSize = 0;

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }
}
