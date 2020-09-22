package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

class Slf4jJdbcLoggerTest {

  private final Sql<?> selectSql =
      new PreparedSql(
          SqlKind.SELECT,
          "raw",
          "formatted",
          "sqlFilePath",
          Collections.emptyList(),
          SqlLogType.FORMATTED);

  private final Sql<?> insertSql =
      new PreparedSql(
          SqlKind.INSERT,
          "raw",
          "formatted",
          "sqlFilePath",
          Collections.emptyList(),
          SqlLogType.FORMATTED);

  @AfterEach
  void afterEach() {
    List<ILoggingEvent> events = getEvents();
    events.clear();
  }

  @Test
  void constructor() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger(Level.TRACE);
    String className = getClass().getName();
    String methodName = "constructor";
    logger.logSql(className, methodName, selectSql);

    List<ILoggingEvent> events = getEvents();
    assertEquals(0, events.size());
  }

  @Test
  void logSql() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger();
    String className = getClass().getName();
    String methodName = "logSql";
    logger.logSql(className, methodName, selectSql);

    List<ILoggingEvent> events = getEvents();
    assertEquals(1, events.size());
  }

  @Test
  void logSql_childLogger() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger();
    String className = getClass().getName();
    String methodName = "logSql";
    logger.logSql(className, methodName, insertSql);

    List<ILoggingEvent> events = getEvents();
    assertEquals(0, events.size());
  }

  @Test
  void logSql_classAndMethod_null() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger();
    logger.logSql(null, null, selectSql);

    List<ILoggingEvent> events = getEvents();
    assertEquals(1, events.size());
  }

  @Test
  void logConnectionClosingFailure() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger();
    String className = getClass().getName();
    String methodName = "logConnectionClosingFailure";
    SQLException exception = new SQLException("my exception");
    logger.logConnectionClosingFailure(className, methodName, exception);

    List<ILoggingEvent> events = getEvents();
    assertEquals(1, events.size());
  }

  @Test
  void logSqlExecutionSkipping() {
    Slf4jJdbcLogger logger = new Slf4jJdbcLogger();
    String className = getClass().getName();
    String methodName = "logSqlExecutionSkipping";
    logger.logSqlExecutionSkipping(className, methodName, SqlExecutionSkipCause.STATE_UNCHANGED);

    List<ILoggingEvent> events = getEvents();
    assertEquals(1, events.size());
  }

  private List<ILoggingEvent> getEvents() {
    ch.qos.logback.classic.Logger rootLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    ListAppender<ILoggingEvent> appender =
        (ListAppender<ILoggingEvent>) rootLogger.getAppender("LIST");
    return appender.list;
  }
}
