package org.seasar.doma.jdbc.statistic;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.seasar.doma.jdbc.Sql;

/** The default implementation of {@link StatisticManager}. */
public class DefaultStatisticManager implements StatisticManager {

  private volatile boolean enabled;

  private final ConcurrentMap<String, Statistic> statisticMap = new ConcurrentHashMap<>();

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
    return statisticMap.values();
  }

  @Override
  public void recordSqlExecution(Sql<?> sql, long startTimeNanos, long endTimeNanos) {
    if (!enabled) {
      return;
    }
    Objects.requireNonNull(sql);
    if (endTimeNanos < startTimeNanos) {
      throw new IllegalArgumentException("endTimeNanos < startTimeNanos");
    }
    String rawSql = sql.getRawSql();
    long execTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTimeNanos - startTimeNanos);
    statisticMap.compute(
        rawSql,
        (key, value) ->
            value == null ? Statistic.of(key, execTimeMillis) : value.calculate(execTimeMillis));
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
