package org.seasar.doma.slf4j;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.AbstractJdbcLogger;
import org.seasar.doma.jdbc.LogKind;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

public class Slf4jJdbcLogger extends AbstractJdbcLogger<Level> {

  public Slf4jJdbcLogger() {
    super(Level.DEBUG);
  }

  public Slf4jJdbcLogger(Level level) {
    super(level);
  }

  @SuppressWarnings("CStyleArrayDeclaration")
  @Override
  protected void logDaoMethodEntering(
      String callerClassName,
      String callerMethodName,
      Object args[],
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.DAO.fullName(), level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  protected void logDaoMethodExiting(
      String callerClassName,
      String callerMethodName,
      Object result,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.DAO.fullName(), level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  protected void logDaoMethodThrowing(
      String callerClassName,
      String callerMethodName,
      RuntimeException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.DAO.fullName(), level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  protected void logSqlExecutionSkipping(
      String callerClassName,
      String callerMethodName,
      SqlExecutionSkipCause cause,
      Level level,
      Supplier<String> messageSupplier) {
    String loggerName = LogKind.SKIP.fullName() + "." + cause.name();
    log(loggerName, level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  protected void logSql(
      String callerClassName,
      String callerMethodName,
      Sql<?> sql,
      Level level,
      Supplier<String> messageSupplier) {
    String loggerName = LogKind.SQL.fullName() + "." + sql.getKind().name();
    log(loggerName, level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  protected void logTransactionBegun(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionEnded(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionSavepointCreated(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionCommitted(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionRolledback(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionSavepointRolledback(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionSavepointReleased(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      Level level,
      Supplier<String> messageSupplier) {
    log(
        LogKind.LOCAL_TRANSACTION.fullName(),
        level,
        callerClassName,
        callerMethodName,
        null,
        messageSupplier);
  }

  @Override
  protected void logTransactionRollbackFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  protected void logAutoCommitEnablingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  protected void logTransactionIsolationSettingFailure(
      String callerClassName,
      String callerMethodName,
      int transactionIsolationLevel,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  protected void logConnectionClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  protected void logStatementClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  protected void logResultSetClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Level level,
      Supplier<String> messageSupplier) {
    log(LogKind.FAILURE.fullName(), level, callerClassName, callerMethodName, e, messageSupplier);
  }

  protected void log(
      String loggerName,
      Level level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier) {
    Objects.requireNonNull(loggerName);
    Objects.requireNonNull(level);
    Objects.requireNonNull(messageSupplier);
    final Logger logger = LoggerFactory.getLogger(loggerName);
    switch (level) {
      case ERROR:
        if (logger.isErrorEnabled()) {
          Marker marker = getMarker(callerClassName, callerMethodName);
          logger.error(marker, messageSupplier.get(), throwable);
        }
        break;
      case WARN:
        if (logger.isWarnEnabled()) {
          Marker marker = getMarker(callerClassName, callerMethodName);
          logger.warn(marker, messageSupplier.get(), throwable);
        }
        break;
      case INFO:
        if (logger.isInfoEnabled()) {
          Marker marker = getMarker(callerClassName, callerMethodName);
          logger.info(marker, messageSupplier.get(), throwable);
        }
        break;
      case DEBUG:
        if (logger.isDebugEnabled()) {
          Marker marker = getMarker(callerClassName, callerMethodName);
          logger.debug(marker, messageSupplier.get(), throwable);
        }
        break;
      case TRACE:
        if (logger.isTraceEnabled()) {
          Marker marker = getMarker(callerClassName, callerMethodName);
          logger.trace(marker, messageSupplier.get(), throwable);
        }
        break;
    }
  }

  private Marker getMarker(String callerClassName, String callerMethodName) {
    return MarkerFactory.getMarker(callerClassName + "#" + callerMethodName);
  }

  @Override
  protected void log(
      Level level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier) {
    throw new UnsupportedOperationException();
  }
}
