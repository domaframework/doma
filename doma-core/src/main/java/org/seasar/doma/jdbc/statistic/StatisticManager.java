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
package org.seasar.doma.jdbc.statistic;

import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.util.ThrowingSupplier;

/** A manager for statistics. */
public interface StatisticManager {

  /**
   * Returns whether this manager is enabled.
   *
   * @return whether this manager is enabled
   */
  boolean isEnabled();

  /**
   * Sets whether this manager is enabled.
   *
   * @param enabled whether this manager is enabled
   */
  void setEnabled(boolean enabled);

  /**
   * Returns the statistics.
   *
   * @return the statistics
   */
  Iterable<Statistic> getStatistics();

  /**
   * Records the execution of SQL.
   *
   * @param sql the SQL
   * @param startTimeNanos the start time in nanoseconds
   * @param endTimeNanos the end time in nanoseconds
   */
  void recordSqlExecution(Sql<?> sql, long startTimeNanos, long endTimeNanos);

  /**
   * Executes SQL and records the execution.
   *
   * @param <RESULT> the result type
   * @param <EXCEPTION> the exception type
   * @param sql the SQL
   * @param supplier the supplier
   * @return the result
   * @throws EXCEPTION the exception
   */
  default <RESULT, EXCEPTION extends Exception> RESULT executeSql(
      Sql<?> sql, ThrowingSupplier<RESULT, EXCEPTION> supplier) throws EXCEPTION {
    RESULT result;
    if (isEnabled()) {
      long startTimeNanos = System.nanoTime();
      result = supplier.get();
      long endTimeNanos = System.nanoTime();
      recordSqlExecution(sql, startTimeNanos, endTimeNanos);
    } else {
      result = supplier.get();
    }
    return result;
  }

  /** Clears the statistics. */
  void clear();
}
