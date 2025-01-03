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
package org.seasar.doma.jdbc;

import java.sql.SQLException;
import java.util.function.Supplier;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.message.Message;

/**
 * A skeletal implementation of the {@link JdbcLogger} interface.
 *
 * @param <LEVEL> The log level
 */
public abstract class AbstractJdbcLogger<LEVEL> implements JdbcLogger {

  /** the default log level */
  protected LEVEL defaultLevel;

  /**
   * Creates an instance with a log level.
   *
   * @param level the log level
   */
  protected AbstractJdbcLogger(LEVEL level) {
    if (level == null) {
      throw new DomaNullPointerException("level");
    }
    this.defaultLevel = level;
  }

  /**
   * Logs an error or an exception.
   *
   * @param level the log level
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param throwable the error or exception
   * @param messageSupplier the message supplier
   */
  protected abstract void log(
      LEVEL level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier);

  @Override
  public void logDaoMethodEntering(
      String callerClassName, String callerMethodName, Object... args) {
    logDaoMethodEntering(
        callerClassName,
        callerMethodName,
        args,
        defaultLevel,
        () -> Message.DOMA2220.getMessage(callerClassName, callerMethodName));
  }

  @SuppressWarnings("CStyleArrayDeclaration")
  protected void logDaoMethodEntering(
      String callerClassName,
      String callerMethodName,
      Object args[],
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logDaoMethodExiting(String callerClassName, String callerMethodName, Object result) {
    logDaoMethodExiting(
        callerClassName,
        callerMethodName,
        result,
        defaultLevel,
        () -> Message.DOMA2221.getMessage(callerClassName, callerMethodName));
  }

  protected void logDaoMethodExiting(
      String callerClassName,
      String callerMethodName,
      Object result,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logDaoMethodThrowing(
      String callerClassName, String callerMethodName, RuntimeException e) {
    logDaoMethodThrowing(
        callerClassName,
        callerMethodName,
        e,
        defaultLevel,
        () ->
            Message.DOMA2222.getMessage(callerClassName, callerMethodName, e.getClass().getName()));
  }

  protected void logDaoMethodThrowing(
      String callerClassName,
      String callerMethodName,
      RuntimeException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logSqlExecutionSkipping(
      String callerClassName, String callerMethodName, SqlExecutionSkipCause cause) {
    logSqlExecutionSkipping(
        callerClassName,
        callerMethodName,
        cause,
        defaultLevel,
        () -> Message.DOMA2223.getMessage(callerClassName, callerMethodName, cause.name()));
  }

  protected void logSqlExecutionSkipping(
      String callerClassName,
      String callerMethodName,
      SqlExecutionSkipCause cause,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logSql(String callerClassName, String callerMethodName, Sql<?> sql) {
    logSql(
        callerClassName,
        callerMethodName,
        sql,
        defaultLevel,
        () -> Message.DOMA2076.getMessage(sql.getSqlFilePath(), getSqlText(sql)));
  }

  protected void logSql(
      String callerClassName,
      String callerMethodName,
      Sql<?> sql,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  protected String getSqlText(Sql<?> sql) {
    switch (sql.getSqlLogType()) {
      case RAW:
        return sql.getRawSql();
      case FORMATTED:
        return sql.getFormattedSql();
      case NONE:
        return "";
    }
    return AssertionUtil.assertUnreachable();
  }

  @Override
  public void logTransactionBegun(
      String callerClassName, String callerMethodName, String transactionId) {
    logTransactionBegun(
        callerClassName,
        callerMethodName,
        transactionId,
        defaultLevel,
        () -> Message.DOMA2063.getMessage(transactionId));
  }

  protected void logTransactionBegun(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionEnded(
      String callerClassName, String callerMethodName, String transactionId) {
    logTransactionEnded(
        callerClassName,
        callerMethodName,
        transactionId,
        defaultLevel,
        () -> Message.DOMA2064.getMessage(transactionId));
  }

  protected void logTransactionEnded(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionSavepointCreated(
      String callerClassName, String callerMethodName, String transactionId, String savepointName) {
    logTransactionSavepointCreated(
        callerClassName,
        callerMethodName,
        transactionId,
        savepointName,
        defaultLevel,
        () -> Message.DOMA2065.getMessage(transactionId, savepointName));
  }

  protected void logTransactionSavepointCreated(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionCommitted(
      String callerClassName, String callerMethodName, String transactionId) {
    logTransactionCommitted(
        callerClassName,
        callerMethodName,
        transactionId,
        defaultLevel,
        () -> Message.DOMA2067.getMessage(transactionId));
  }

  protected void logTransactionCommitted(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionRolledback(
      String callerClassName, String callerMethodName, String transactionId) {
    logTransactionRolledback(
        callerClassName,
        callerMethodName,
        transactionId,
        defaultLevel,
        () -> Message.DOMA2068.getMessage(transactionId));
  }

  protected void logTransactionRolledback(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionSavepointRolledback(
      String callerClassName, String callerMethodName, String transactionId, String savepointName) {
    logTransactionSavepointRolledback(
        callerClassName,
        callerMethodName,
        transactionId,
        savepointName,
        defaultLevel,
        () -> Message.DOMA2069.getMessage(transactionId, savepointName));
  }

  protected void logTransactionSavepointRolledback(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionSavepointReleased(
      String callerClassName, String callerMethodName, String transactionId, String savepointName) {
    logTransactionSavepointReleased(
        callerClassName,
        callerMethodName,
        transactionId,
        savepointName,
        defaultLevel,
        () -> Message.DOMA2234.getMessage(transactionId, savepointName));
  }

  protected void logTransactionSavepointReleased(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, null, messageSupplier);
  }

  @Override
  public void logTransactionRollbackFailure(
      String callerClassName, String callerMethodName, String transactionId, SQLException e) {
    logTransactionRollbackFailure(
        callerClassName,
        callerMethodName,
        e,
        defaultLevel,
        () -> Message.DOMA2070.getMessage(transactionId));
  }

  protected void logTransactionRollbackFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  public void logAutoCommitEnablingFailure(
      String callerClassName, String callerMethodName, SQLException e) {
    logAutoCommitEnablingFailure(
        callerClassName, callerMethodName, e, defaultLevel, Message.DOMA2071::getMessage);
  }

  protected void logAutoCommitEnablingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  public void logTransactionIsolationSettingFailure(
      String callerClassName,
      String callerMethodName,
      int transactionIsolationLevel,
      SQLException e) {
    logTransactionIsolationSettingFailure(
        callerClassName,
        callerMethodName,
        transactionIsolationLevel,
        e,
        defaultLevel,
        () -> Message.DOMA2072.getMessage(transactionIsolationLevel));
  }

  protected void logTransactionIsolationSettingFailure(
      String callerClassName,
      String callerMethodName,
      int transactionIsolationLevel,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  public void logConnectionClosingFailure(
      String callerClassName, String callerMethodName, SQLException e) {
    logConnectionClosingFailure(
        callerClassName, callerMethodName, e, defaultLevel, Message.DOMA2073::getMessage);
  }

  protected void logConnectionClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  public void logStatementClosingFailure(
      String callerClassName, String callerMethodName, SQLException e) {
    logStatementClosingFailure(
        callerClassName, callerMethodName, e, defaultLevel, Message.DOMA2074::getMessage);
  }

  protected void logStatementClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }

  @Override
  public void logResultSetClosingFailure(
      String callerClassName, String callerMethodName, SQLException e) {
    logResultSetClosingFailure(
        callerClassName, callerMethodName, e, defaultLevel, Message.DOMA2075::getMessage);
  }

  protected void logResultSetClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      LEVEL level,
      Supplier<String> messageSupplier) {
    log(level, callerClassName, callerMethodName, e, messageSupplier);
  }
}
