/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc;

import java.io.Serializable;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/**
 * The options for an SQL SELECT statement.
 *
 * <pre>
 * SelectOptions options = SelectOptions.get().offset(10).limit(50).forUpdate();
 * </pre>
 */
public class SelectOptions implements Serializable {

  private static final long serialVersionUID = 1L;

  protected long offset = -1;

  protected long limit = -1;

  protected boolean count;

  protected long countSize = -1;

  protected SelectForUpdateType forUpdateType;

  protected int waitSeconds;

  protected String[] aliases = new String[] {};

  protected SelectOptions() {}

  /**
   * Creates a new {@link SelectOptions} instance.
   *
   * @return the new {@link SelectOptions} instance
   */
  public static SelectOptions get() {
    return new SelectOptions();
  }

  /**
   * Indicates to apply pessimistic locking.
   *
   * @return this instance
   */
  public SelectOptions forUpdate() {
    forUpdateType = SelectForUpdateType.NORMAL;
    return this;
  }

  /**
   * Indicates to apply pessimistic locking with specified aliases.
   *
   * @param aliases aliases of the table or the column
   * @return this instance
   */
  public SelectOptions forUpdate(String... aliases) {
    if (aliases == null) {
      throw new DomaNullPointerException("aliases");
    }
    forUpdateType = SelectForUpdateType.NORMAL;
    this.aliases = aliases;
    return this;
  }

  /**
   * Indicates to apply no-wait pessimistic locking.
   *
   * @return the instance
   */
  public SelectOptions forUpdateNowait() {
    forUpdateType = SelectForUpdateType.NOWAIT;
    return this;
  }

  /**
   * Indicates to apply no-wait pessimistic locking with specified aliases.
   *
   * @param aliases aliases of the table or the column
   * @return this instance
   */
  public SelectOptions forUpdateNowait(String... aliases) {
    if (aliases == null) {
      throw new DomaNullPointerException("aliases");
    }
    forUpdateType = SelectForUpdateType.NOWAIT;
    this.aliases = aliases;
    return this;
  }

  /**
   * Indicates to apply pessimistic locking with specified wait seconds.
   *
   * @param waitSeconds the wait seconds
   * @return this instance
   */
  public SelectOptions forUpdateWait(int waitSeconds) {
    if (waitSeconds < 0) {
      throw new DomaIllegalArgumentException("waitSeconds", "waitSeconds < 0");
    }
    forUpdateType = SelectForUpdateType.WAIT;
    this.waitSeconds = waitSeconds;
    return this;
  }

  /**
   * Indicates to apply pessimistic locking with specified wait seconds and aliases.
   *
   * @param waitSeconds the wait seconds
   * @param aliases aliases of the table or the column
   * @return this instance
   */
  public SelectOptions forUpdateWait(int waitSeconds, String... aliases) {
    if (waitSeconds < 0) {
      throw new DomaIllegalArgumentException("waitSeconds", "waitSeconds < 0");
    }
    if (aliases == null) {
      throw new DomaNullPointerException("aliases");
    }
    forUpdateType = SelectForUpdateType.WAIT;
    this.waitSeconds = waitSeconds;
    this.aliases = aliases;
    return this;
  }

  /**
   * Indicates to apply the the specified offset.
   *
   * @param offset the offset
   * @return this instance
   */
  public SelectOptions offset(int offset) {
    if (offset < 0) {
      throw new DomaIllegalArgumentException("offset", "offset < 0");
    }
    this.offset = offset;
    return this;
  }

  /**
   * Indicates to apply the specified limit.
   *
   * @param limit the limit
   * @return this instance
   */
  public SelectOptions limit(int limit) {
    if (limit < 0) {
      throw new DomaIllegalArgumentException("limit", "limit < 0");
    }
    this.limit = limit;
    return this;
  }

  /**
   * Indicates to count all rows.
   *
   * @return this instance
   */
  public SelectOptions count() {
    this.count = true;
    return this;
  }

  /**
   * Returns the count of all rows.
   *
   * <p>If {@link #count()} is not invoked before the DAO method execution, this method returns
   * {@code -1}.
   *
   * @return the count of all rows
   */
  public long getCount() {
    return countSize;
  }
}
