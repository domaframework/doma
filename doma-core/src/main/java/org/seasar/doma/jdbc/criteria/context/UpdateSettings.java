package org.seasar.doma.jdbc.criteria.context;

public class UpdateSettings extends Settings {
  private int batchSize = 0;
  private boolean allowEmptyWhere;
  private boolean ignoreVersion;
  private boolean suppressOptimisticLockException;
  private boolean excludeNull;

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

  public boolean getIgnoreVersion() {
    return ignoreVersion;
  }

  public void setIgnoreVersion(boolean ignoreVersion) {
    this.ignoreVersion = ignoreVersion;
  }

  public boolean getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
  }

  public void setSuppressOptimisticLockException(boolean suppressOptimisticLockException) {
    this.suppressOptimisticLockException = suppressOptimisticLockException;
  }

  public boolean getExcludeNull() {
    return excludeNull;
  }

  public void setExcludeNull(boolean excludeNull) {
    this.excludeNull = excludeNull;
  }
}
