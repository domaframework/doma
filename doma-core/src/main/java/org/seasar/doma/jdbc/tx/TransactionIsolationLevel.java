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
package org.seasar.doma.jdbc.tx;

import java.sql.Connection;

/** Defines transaction isolation levels. */
public enum TransactionIsolationLevel {

  /**
   * @see Connection#TRANSACTION_READ_UNCOMMITTED
   */
  READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

  /**
   * @see Connection#TRANSACTION_READ_COMMITTED
   */
  READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

  /**
   * @see Connection#TRANSACTION_REPEATABLE_READ
   */
  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

  /**
   * @see Connection#TRANSACTION_SERIALIZABLE
   */
  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

  /** the default level */
  DEFAULT(-1);

  private final int level;

  TransactionIsolationLevel(int level) {
    this.level = level;
  }

  /**
   * Returns the constant that represents a transaction isolation level.
   *
   * <p>{@literal -1} indicates the default transaction isolation level.
   *
   * @see Connection#TRANSACTION_READ_UNCOMMITTED
   * @see Connection#TRANSACTION_READ_COMMITTED
   * @see Connection#TRANSACTION_REPEATABLE_READ
   * @see Connection#TRANSACTION_SERIALIZABLE
   * @return the constant
   */
  public int getLevel() {
    return level;
  }
}
