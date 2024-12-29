package org.seasar.doma.jdbc.statistic;

import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.util.ThrowingSupplier;

public interface StatisticManager {
  boolean isEnabled();

  void setEnabled(boolean enabled);

  Iterable<Statistic> getStatistics();

  void recordSqlExecution(Sql<?> sql, long startTimeNanos, long endTimeNanos);

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

  void clear();
}
