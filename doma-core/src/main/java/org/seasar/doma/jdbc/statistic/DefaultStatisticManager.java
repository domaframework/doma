package org.seasar.doma.jdbc.statistic;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;

public class DefaultStatisticManager implements StatisticManager {

  private volatile boolean enabled;

  private final ConcurrentMap<String, AtomicReference<Statistic>> statisticMap =
      new ConcurrentHashMap<>();

  public DefaultStatisticManager() {
    this(false);
  }

  public DefaultStatisticManager(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public Iterable<Statistic> getStatistics() {
    return statisticMap.values().stream().map(AtomicReference::get).toList();
  }

  @Override
  public void recordSqlExecution(Sql<?> sql, long startTimeNanos, long endTimeNanos) {
    if (!enabled) {
      return;
    }
    String rawSql = sql.getRawSql();
    long execTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTimeNanos - startTimeNanos);
    AtomicReference<Statistic> ref =
        statisticMap.computeIfAbsent(
            rawSql,
            key ->
                new AtomicReference<>(
                    new Statistic(key, SqlKind.OTHER, 0, 0, Long.MAX_VALUE, 0, 0.0)));
    while (true) {
      Statistic expectedValue = ref.get();
      long count = expectedValue.execCount() + 1;
      long maxTime = Math.max(expectedValue.execMaxTime(), execTimeMillis);
      long minTime = Math.min(expectedValue.execMinTime(), execTimeMillis);
      long totalTime = expectedValue.execTotalTime() + execTimeMillis;
      double avgTime = totalTime / (double) count;
      Statistic newValue =
          new Statistic(rawSql, sql.getKind(), count, maxTime, minTime, totalTime, avgTime);
      if (ref.compareAndSet(expectedValue, newValue)) {
        break;
      }
    }
  }

  @Override
  public void clear() {
    statisticMap.clear();
  }

  @Override
  public String toString() {
    return "DefaultStatisticManager{"
        + "enabled="
        + enabled
        + ", statisticMap.size()="
        + statisticMap.size()
        + '}';
  }
}
