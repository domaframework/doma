package org.seasar.doma.jdbc.criteria.context;

import java.sql.PreparedStatement;

/** Represents the settings for a DELETE criteria query. */
public class DeleteSettings extends Settings {
  private int batchSize = 0;
  private boolean allowEmptyWhere;
  private boolean ignoreVersion;
  private boolean suppressOptimisticLockException;

  /**
   * Returns the batch size.
   *
   * @return the batch size. The default value is {@literal 0}.
   */
  public int getBatchSize() {
    return batchSize;
  }

  /**
   * Sets the batch size.
   *
   * <p>If the value is less than 1, it is regarded as 1.
   *
   * @param batchSize the batch size
   * @see PreparedStatement#executeBatch()
   * @see PreparedStatement#addBatch()
   */
  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  /**
   * Returns whether the empty WHERE clause is allowed or not.
   *
   * @return whether the empty WHERE clause is allowed or not. The default value is {@literal
   *     false}.
   */
  public boolean getAllowEmptyWhere() {
    return allowEmptyWhere;
  }

  /**
   * Sets whether the empty WHERE clause is allowed or not.
   *
   * <p>If the value is {@literal true} and the WHERE clause is empty, {@link
   * org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException} will be suppressed.
   *
   * @param allowEmptyWhere whether the empty WHERE clause is allowed or not
   */
  public void setAllowEmptyWhere(boolean allowEmptyWhere) {
    this.allowEmptyWhere = allowEmptyWhere;
  }

  /**
   * Returns whether to ignore the version property or not.
   *
   * @return whether to ignore the version property or not. The default value is {@literal false}.
   */
  public boolean getIgnoreVersion() {
    return ignoreVersion;
  }

  /**
   * Sets whether to ignore the version property or not.
   *
   * <p>If the value is {@code true}, the column that mapped to the version property is excluded
   * from the SQL DELETE statement.
   *
   * @param ignoreVersion whether to ignore the version property or not
   */
  public void setIgnoreVersion(boolean ignoreVersion) {
    this.ignoreVersion = ignoreVersion;
  }

  /**
   * Returns whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not.
   *
   * @return whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not. The
   *     default value is {@literal false}.
   */
  public boolean getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
  }

  /**
   * Sets whether to suppress {@link org.seasar.doma.jdbc.OptimisticLockException} or not.
   *
   * @param suppressOptimisticLockException whether to suppress {@link
   *     org.seasar.doma.jdbc.OptimisticLockException} or not
   */
  public void setSuppressOptimisticLockException(boolean suppressOptimisticLockException) {
    this.suppressOptimisticLockException = suppressOptimisticLockException;
  }
}
