package org.seasar.doma.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The logger for JDBC related operations.
 *
 * <p>The implementation instance must be thread safe.
 *
 * <p>The implementation methods must not throw any exceptions.
 */
public interface JdbcLogger {

  /**
   * Logs the start of a DAO method.
   *
   * @param callerClassName the name of the DAO class
   * @param callerMethodName the name of the DAO method
   * @param args the arguments of the DAO method
   */
  void logDaoMethodEntering(String callerClassName, String callerMethodName, Object... args);

  /**
   * Logs the normal end of a DAO method.
   *
   * <p>At the end of the DAO method, either this method or {@link #logDaoMethodThrowing(String,
   * String, RuntimeException)} must be invoked.
   *
   * @param callerClassName the name of the DAO class
   * @param callerMethodName the name of the DAO method
   * @param result the method result or null if the method return type is {@code void}
   */
  void logDaoMethodExiting(String callerClassName, String callerMethodName, Object result);

  /**
   * Logs the abnormal end of a DAO method.
   *
   * <p>At the end of the DAO method, either this method or {@link #logDaoMethodExiting(String,
   * String, Object)} must be invoked.
   *
   * @param callerClassName the name of the DAO class
   * @param callerMethodName the name of the DAO method
   * @param e the runtime exception
   */
  void logDaoMethodThrowing(String callerClassName, String callerMethodName, RuntimeException e);

  /**
   * Logs a skip of an SQL execution.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param cause the cause of the skip
   */
  void logSqlExecutionSkipping(
      String callerClassName, String callerMethodName, SqlExecutionSkipCause cause);

  /**
   * Logs an SQL statement.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param sql SQL
   */
  void logSql(String callerClassName, String callerMethodName, Sql<?> sql);

  /**
   * Logs a beginning of a transaction.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   */
  void logTransactionBegun(String callerClassName, String callerMethodName, String transactionId);

  /**
   * Logs an end of a transaction.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   */
  void logTransactionEnded(String callerClassName, String callerMethodName, String transactionId);

  /**
   * Logs a commit of a transaction.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   */
  void logTransactionCommitted(
      String callerClassName, String callerMethodName, String transactionId);

  /**
   * Logs a creation of a save point.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   * @param savepointName the save point name
   */
  void logTransactionSavepointCreated(
      String callerClassName, String callerMethodName, String transactionId, String savepointName);

  /**
   * Logs a rollback of a transaction.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   */
  void logTransactionRolledback(
      String callerClassName, String callerMethodName, String transactionId);

  /**
   * Logs a rollback of a save point.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   * @param savepointName the save point name
   */
  void logTransactionSavepointRolledback(
      String callerClassName, String callerMethodName, String transactionId, String savepointName);

  /**
   * Logs a release of a save point.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   * @param savepointName the save point name
   */
  default void logTransactionSavepointReleased(
      String callerClassName,
      String callerMethodName,
      String transactionId,
      String savepointName) {}

  /**
   * Logs a failure of a transaction rollback.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionId the transaction id
   * @param e the cause of the failure
   */
  void logTransactionRollbackFailure(
      String callerClassName, String callerMethodName, String transactionId, SQLException e);

  /**
   * Logs a failure of an auto commit enabling.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param e the cause of the failure
   * @see Connection#setAutoCommit(boolean)
   */
  void logAutoCommitEnablingFailure(
      String callerClassName, String callerMethodName, SQLException e);

  /**
   * Logs a failure of a transaction isolation setting.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param transactionIsolationLevel the transaction isolation level
   * @param e the cause of the failure
   * @see Connection#setTransactionIsolation(int)
   */
  void logTransactionIsolationSettingFailure(
      String callerClassName,
      String callerMethodName,
      int transactionIsolationLevel,
      SQLException e);

  /**
   * Logs a failure of a connection close.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param e the cause of the failure
   * @see Connection#close()
   */
  void logConnectionClosingFailure(String callerClassName, String callerMethodName, SQLException e);

  /**
   * Logs a failure of a statement close.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param e the cause of the failure
   * @see Statement#close()
   */
  void logStatementClosingFailure(String callerClassName, String callerMethodName, SQLException e);

  /**
   * Logs a failure of a result set close.
   *
   * @param callerClassName the caller class name
   * @param callerMethodName the caller method name
   * @param e the cause of the failure
   * @see ResultSet#close()
   */
  void logResultSetClosingFailure(String callerClassName, String callerMethodName, SQLException e);
}
