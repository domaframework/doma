package org.seasar.doma.jdbc.criteria.context;

import java.sql.PreparedStatement;

/** Represents the settings for an INSERT criteria query. */
public class InsertSettings extends Settings {
  private int batchSize = 0;
  private boolean excludeNull;

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
   * Returns whether to exclude null properties or not.
   *
   * @return whether to exclude null properties or not. The default value is {@literal false}.
   */
  public boolean getExcludeNull() {
    return excludeNull;
  }

  /**
   * Sets whether to exclude null properties or not.
   *
   * <p>If the value is {@code true}, the null properties are excluded from the SQL INSERT
   * statement.
   *
   * @param excludeNull whether to exclude null properties or not
   */
  public void setExcludeNull(boolean excludeNull) {
    this.excludeNull = excludeNull;
  }
}
