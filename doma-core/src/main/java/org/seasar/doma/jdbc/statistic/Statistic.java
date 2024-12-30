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
