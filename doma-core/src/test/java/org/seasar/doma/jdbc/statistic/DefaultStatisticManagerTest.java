package org.seasar.doma.jdbc.statistic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;

class DefaultStatisticManagerTest {

  @Test
  void testRecordSqlExecution_disabledManager() {
    DefaultStatisticManager manager = new DefaultStatisticManager(false);
    Sql<?> sql =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table", "", null, List.of(), SqlLogType.NONE);

    long startTime = System.nanoTime();
    long endTime = startTime + TimeUnit.MILLISECONDS.toNanos(50);

    manager.recordSqlExecution(sql, startTime, endTime);

    assertEquals(0, manager.getStatistics().spliterator().estimateSize());
  }

  @Test
  void testRecordSqlExecution_enabledManager() {
    DefaultStatisticManager manager = new DefaultStatisticManager(true);
    Sql<?> sql =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table", "", null, List.of(), SqlLogType.NONE);

    long startTime = System.nanoTime();
    long endTime = startTime + TimeUnit.MILLISECONDS.toNanos(50);

    manager.recordSqlExecution(sql, startTime, endTime);

    assertEquals(1, manager.getStatistics().spliterator().estimateSize());
  }

  @Test
  void testRecordSqlExecution_updateExistingStatistic() {
    DefaultStatisticManager manager = new DefaultStatisticManager(true);
    Sql<?> sql =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table", "", null, List.of(), SqlLogType.NONE);

    long startTime1 = System.nanoTime();
    long endTime1 = startTime1 + TimeUnit.MILLISECONDS.toNanos(50);
    manager.recordSqlExecution(sql, startTime1, endTime1);

    long startTime2 = System.nanoTime();
    long endTime2 = startTime2 + TimeUnit.MILLISECONDS.toNanos(30);
    manager.recordSqlExecution(sql, startTime2, endTime2);

    assertEquals(1, manager.getStatistics().spliterator().estimateSize());
    Statistic statistic = manager.getStatistics().iterator().next();
    assertEquals("SELECT * FROM table", statistic.sql());
    assertEquals(2, statistic.execCount());
  }

  @Test
  void testRecordSqlExecution_nullSql_throwsException() {
    DefaultStatisticManager manager = new DefaultStatisticManager(true);

    long startTime = System.nanoTime();
    long endTime = startTime + TimeUnit.MILLISECONDS.toNanos(50);

    assertThrows(
        NullPointerException.class, () -> manager.recordSqlExecution(null, startTime, endTime));
  }

  @Test
  void testRecordSqlExecution_invalidTime_throwsException() {
    DefaultStatisticManager manager = new DefaultStatisticManager(true);
    Sql<?> sql =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table", "", null, List.of(), SqlLogType.NONE);

    long startTime = System.nanoTime();
    long endTime = startTime - TimeUnit.MILLISECONDS.toNanos(50);

    assertThrows(
        IllegalArgumentException.class, () -> manager.recordSqlExecution(sql, startTime, endTime));
  }

  @Test
  void testRecordSqlExecution_clear() {
    DefaultStatisticManager manager = new DefaultStatisticManager(true);
    Sql<?> sql1 =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table1", "", null, List.of(), SqlLogType.NONE);

    long startTime1 = System.nanoTime();
    long endTime1 = startTime1 + TimeUnit.MILLISECONDS.toNanos(50);
    manager.recordSqlExecution(sql1, startTime1, endTime1);

    Sql<?> sql2 =
        new PreparedSql(
            SqlKind.SELECT, "SELECT * FROM table2", "", null, List.of(), SqlLogType.NONE);
    long startTime2 = System.nanoTime();
    long endTime2 = startTime2 + TimeUnit.MILLISECONDS.toNanos(30);
    manager.recordSqlExecution(sql2, startTime2, endTime2);

    assertEquals(2, manager.getStatistics().spliterator().estimateSize());

    manager.clear();

    assertEquals(0, manager.getStatistics().spliterator().estimateSize());
  }
}
