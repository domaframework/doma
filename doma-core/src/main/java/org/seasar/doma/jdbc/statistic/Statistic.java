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

import java.util.Objects;

/** Represents a statistic of SQL execution. */
public record Statistic(
    String sql,
    long execCount,
    long execMaxTime,
    long execMinTime,
    long execTotalTime,
    double execAvgTime) {

  /**
   * Creates an instance.
   *
   * @param sql the SQL
   * @param execCount the execution count
   * @param execMaxTime the maximum execution time in milliseconds
   * @param execMinTime the minimum execution time in milliseconds
   * @param execTotalTime the total execution time in milliseconds
   * @param execAvgTime the average execution time in milliseconds
   */
  public Statistic {
    Objects.requireNonNull(sql);
  }

  /**
   * Calculates a new instance.
   *
   * @param execTimeMillis the execution time in milliseconds
   * @return the new instance
   */
  public Statistic calculate(long execTimeMillis) {
    long count = this.execCount + 1;
    long maxTime = Math.max(this.execMaxTime, execTimeMillis);
    long minTime = Math.min(this.execMinTime, execTimeMillis);
    long totalTime = this.execTotalTime + execTimeMillis;
    double avgTime = totalTime / (double) count;
    return new Statistic(sql, count, maxTime, minTime, totalTime, avgTime);
  }

  /**
   * Creates an instance.
   *
   * @param sql the SQL
   * @param execTimeMillis the execution time in milliseconds
   * @return the new instance
   */
  public static Statistic of(String sql, long execTimeMillis) {
    return new Statistic(sql, 1, execTimeMillis, execTimeMillis, execTimeMillis, execTimeMillis);
  }
}
