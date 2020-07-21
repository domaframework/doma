package org.seasar.doma.jdbc.tx;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.DataSource;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.message.Message;

/**
 * A local transaction.
 *
 * <p>This instance is thread safe.
 *
 * <p>Begin a transaction by {@link #begin()} or {@link #begin(TransactionIsolationLevel)} and end
 * the transaction always by {@link #commit()} or {@link #rollback()}.
 *
 * <pre>
 * LocalTransaction tx = AppConfig.getLocalTransaction();
 * try {
 *     tx.begin();
 *
 *     Employee employee = dao.selectById(1);
 *     employee.setName(&quot;SMITH&quot;);
 *     employee.setJobType(JobType.PRESIDENT);
 *     dao.update(employee);
 *
 *     tx.commit();
 * } finally {
 *     tx.rollback();
 * }
 * </pre>
 *
 * Same instance can handle multiple transactions sequentially.
 *
 * <pre>
 * LocalTransaction tx = AppConfig.getLocalTransaction();
 * // transaction 1
 * try {
 *     tx.begin();
 *     ...
 *     tx.commit();
 * } finally {
 *     tx.rollback();
 * }
 * // transaction 2
 * try {
 *     tx.begin();
 *     ...
 *     tx.commit();
 * } finally {
 *     tx.rollback();
 * }
 * </pre>
 *
 * Any exceptions that are thrown from this class methods roll back the transaction.
 */
public class LocalTransaction {

  protected final DataSource dataSource;

  protected final ThreadLocal<LocalTransactionContext> localTxContextHolder;

  protected final JdbcLogger jdbcLogger;

  protected final TransactionIsolationLevel defaultTransactionIsolationLevel;

  protected final String className;

  protected LocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger) {
    this(dataSource, localTxContextHolder, jdbcLogger, null);
  }

  protected LocalTransaction(
      DataSource dataSource,
      ThreadLocal<LocalTransactionContext> localTxContextHolder,
      JdbcLogger jdbcLogger,
      TransactionIsolationLevel defaultTransactionIsolationLevel) {
    assertNotNull(dataSource, localTxContextHolder, jdbcLogger);
    this.dataSource = dataSource;
    this.localTxContextHolder = localTxContextHolder;
    this.jdbcLogger = jdbcLogger;
    this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
    this.className = getClass().getName();
  }

  /**
   * Begin this transaction.
   *
   * @throws TransactionAlreadyBegunException if this transaction is already begun
   * @throws JdbcException if a JDBC related error occurs
   */
  public void begin() {
    beginInternal(defaultTransactionIsolationLevel, "begin");
  }

  /**
   * Begin this transaction with the specified transaction isolation level.
   *
   * @param transactionIsolationLevel the transaction isolation level
   * @throws DomaNullPointerException if the {@code transactionIsolationLevel} is {@code null}
   * @throws TransactionAlreadyBegunException if this transaction is already begun
   * @throws JdbcException if a JDBC related error occurs
   */
  public void begin(TransactionIsolationLevel transactionIsolationLevel) {
    if (transactionIsolationLevel == null) {
      throw new DomaNullPointerException("transactionIsolationLevel");
    }
    beginInternal(transactionIsolationLevel, "begin");
  }

  protected void beginInternal(
      TransactionIsolationLevel transactionIsolationLevel, String callerMethodName) {
    assertNotNull(callerMethodName);
    LocalTransactionContext context = localTxContextHolder.get();
    if (isActiveInternal(context)) {
      String id = context.getId();
      rollbackInternal(callerMethodName);
      throw new TransactionAlreadyBegunException(id);
    }
    context = getLocalTransactionContext();
    context.begin(
        () -> {
          Connection connection = JdbcUtil.getConnection(dataSource);

          int transactionIsolation;
          try {
            transactionIsolation = connection.getTransactionIsolation();
          } catch (SQLException e) {
            closeConnection(connection);
            throw new JdbcException(Message.DOMA2056, e, e);
          }
          if (transactionIsolationLevel != null
              && transactionIsolationLevel != TransactionIsolationLevel.DEFAULT) {
            int level = transactionIsolationLevel.getLevel();
            try {
              connection.setTransactionIsolation(level);
            } catch (SQLException e) {
              closeConnection(connection);
              throw new JdbcException(Message.DOMA2055, e, transactionIsolationLevel.name(), e);
            }
          }

          boolean isAutoCommit;
          try {
            isAutoCommit = connection.getAutoCommit();
          } catch (SQLException e) {
            closeConnection(connection);
            throw new JdbcException(Message.DOMA2084, e, e);
          }
          if (isAutoCommit) {
            try {
              connection.setAutoCommit(false);
            } catch (SQLException e) {
              closeConnection(connection);
              throw new JdbcException(Message.DOMA2041, e, e);
            }
          }

          return new LocalTransactionConnection(connection, transactionIsolation, isAutoCommit);
        });
    jdbcLogger.logTransactionBegun(className, callerMethodName, context.getId());
  }

  protected LocalTransactionContext getLocalTransactionContext() {
    LocalTransactionContext context = new LocalTransactionContext();
    localTxContextHolder.set(context);
    return context;
  }

  /**
   * Commits this transaction.
   *
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   * @throws JdbcException if a JDBC related error occurs
   */
  public void commit() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2046);
    }
    if (context.hasConnection()) {
      LocalTransactionConnection connection = context.getConnection();
      try {
        connection.commit();
        jdbcLogger.logTransactionCommitted(className, "commit", context.getId());
      } catch (SQLException e) {
        rollbackInternal("commit");
        throw new JdbcException(Message.DOMA2043, e, e);
      } finally {
        end("commit");
      }
    } else {
      end("commit");
    }
  }

  /**
   * Suspends this transaction.
   *
   * @return the transaction context that is required to resume this transaction
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   */
  public LocalTransactionContext suspend() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2046);
    }
    localTxContextHolder.set(null);
    return context;
  }

  /**
   * Resumes this transaction.
   *
   * <p>This method does not throw any exceptions.
   *
   * @param context the transaction context that is returned from {@link #suspend()}
   */
  public void resume(LocalTransactionContext context) {
    LocalTransactionContext currentContext = localTxContextHolder.get();
    if (isActiveInternal(currentContext)) {
      rollbackInternal("resume");
    }
    localTxContextHolder.set(context);
  }

  /**
   * Undoes all changes made in this transaction.
   *
   * <p>If this transaction is not begun, this method does nothing.
   *
   * <p>This method does not throw any exceptions.
   */
  public void rollback() {
    rollbackInternal("rollback");
  }

  protected void rollbackInternal(String callerMethodName) {
    assertNotNull(callerMethodName);
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      return;
    }
    if (context.hasConnection()) {
      LocalTransactionConnection connection = context.getConnection();
      String id = context.getId();
      try {
        connection.rollback();
        jdbcLogger.logTransactionRolledback(className, callerMethodName, id);
      } catch (SQLException ignored) {
        jdbcLogger.logTransactionRollbackFailure(className, callerMethodName, id, ignored);
      } finally {
        end(callerMethodName);
      }
    } else {
      end(callerMethodName);
    }
  }

  /**
   * Creates a save point with the specified name.
   *
   * @param savepointName the name of the save point
   * @throws DomaNullPointerException if the {@code savepointName} is {@code null}
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   * @throws SavepointAlreadyExistsException if the save point already exists
   * @throws JdbcException if a JDBC related error occurs
   */
  public void setSavepoint(String savepointName) {
    if (savepointName == null) {
      rollbackInternal("setSavepoint");
      throw new DomaNullPointerException("savepointName");
    }
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2053, savepointName);
    }
    String id = context.getId();
    Savepoint savepoint = context.getSavepoint(savepointName);
    if (savepoint != null) {
      rollbackInternal("setSavepoint");
      throw new SavepointAlreadyExistsException(savepointName);
    }
    LocalTransactionConnection connection = context.getConnection();
    try {
      savepoint = connection.setSavepoint(savepointName);
    } catch (SQLException e) {
      rollbackInternal("setSavepoint");
      throw new JdbcException(Message.DOMA2051, e, savepointName, e);
    }
    context.addSavepoint(savepointName, savepoint);
    jdbcLogger.logTransactionSavepointCreated(className, "setSavepoint", id, savepointName);
  }

  /**
   * Whether this transaction has the specified save point.
   *
   * @param savepointName the name of the save point
   * @throws DomaNullPointerException if the {@code savepointName} is {@code null}
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   * @return {@code true} if this transaction has the save point
   */
  public boolean hasSavepoint(String savepointName) {
    if (savepointName == null) {
      rollbackInternal("hasSavepoint");
      throw new DomaNullPointerException("savepointName");
    }
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2057, savepointName);
    }
    return context.getSavepoint(savepointName) != null;
  }

  /**
   * Removes the specified save point and subsequent save points from this transaction.
   *
   * @param savepointName the name of the save point
   * @throws DomaNullPointerException if the {@code savepointName} is {@code null}
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   * @throws JdbcException if a JDBC related error occurs
   */
  public void releaseSavepoint(String savepointName) {
    if (savepointName == null) {
      rollbackInternal("releaseSavepoint");
      throw new DomaNullPointerException("savepointName");
    }
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2061, savepointName);
    }
    String id = context.getId();
    Savepoint savepoint = context.releaseAndGetSavepoint(savepointName);
    if (savepoint == null) {
      rollbackInternal("releaseSavepoint");
      throw new SavepointNotFoundException(savepointName);
    }
    LocalTransactionConnection connection = context.getConnection();
    try {
      connection.releaseSavepoint(savepoint);
    } catch (SQLException e) {
      rollbackInternal("releaseSavepoint");
      throw new JdbcException(Message.DOMA2060, e, savepointName, e);
    }
  }

  /**
   * Undoes all changes made after the given save point.
   *
   * @param savepointName the name of the save point
   * @throws DomaNullPointerException if the {@code savepointName} is {@code null}
   * @throws SavepointNotFoundException if the save point is not found
   * @throws TransactionNotYetBegunException if this transaction is not yet begun
   * @throws JdbcException if a JDBC related error occurs
   */
  public void rollback(String savepointName) {
    if (savepointName == null) {
      rollbackInternal("rollback");
      throw new DomaNullPointerException("savepointName");
    }
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      throw new TransactionNotYetBegunException(Message.DOMA2062, savepointName);
    }
    String id = context.getId();
    Savepoint savepoint = context.getSavepoint(savepointName);
    if (savepoint == null) {
      rollbackInternal("rollback");
      throw new SavepointNotFoundException(savepointName);
    }
    LocalTransactionConnection connection = context.getConnection();
    try {
      connection.rollback(savepoint);
    } catch (SQLException e) {
      rollbackInternal("rollback");
      throw new JdbcException(Message.DOMA2052, e, savepointName, e);
    }
    jdbcLogger.logTransactionSavepointRolledback(className, "rollback", id, savepointName);
  }

  protected void end(String callerMethodName) {
    assertNotNull(callerMethodName);
    LocalTransactionContext context = localTxContextHolder.get();
    if (!isActiveInternal(context)) {
      return;
    }
    endInternal(context, callerMethodName);
    context.end();
  }

  protected void endInternal(LocalTransactionContext context, String callerMethodName) {
    release(context, callerMethodName);
    jdbcLogger.logTransactionEnded(className, callerMethodName, context.getId());
  }

  /**
   * Releases the transaction context.
   *
   * <p>This method does not throw any exceptions.
   *
   * @param context the transaction context
   * @param callerMethodName the caller method name
   */
  protected void release(LocalTransactionContext context, String callerMethodName) {
    assertNotNull(context, callerMethodName);
    if (context == null) {
      return;
    }
    localTxContextHolder.set(null);
    if (!context.hasConnection()) {
      return;
    }
    LocalTransactionConnection localTransactionConnection = context.getConnection();
    Connection connection = localTransactionConnection.getWrappedConnection();

    int isolationLevel = localTransactionConnection.getPreservedTransactionIsolation();
    if (isolationLevel != Connection.TRANSACTION_NONE) {
      try {
        connection.setTransactionIsolation(isolationLevel);
      } catch (SQLException ignored) {
        jdbcLogger.logTransactionIsolationSettingFailure(
            className, callerMethodName, isolationLevel, ignored);
      }
    }

    boolean isAutoCommit = localTransactionConnection.getPreservedAutoCommitState();
    if (isAutoCommit) {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException ignored) {
        jdbcLogger.logAutoCommitEnablingFailure(className, callerMethodName, ignored);
      }
    }

    closeConnection(connection);
  }

  protected void closeConnection(Connection connection) {
    JdbcUtil.close(connection, jdbcLogger);
  }

  /** Returns an unique string to identify this transaction. */
  @Override
  public String toString() {
    LocalTransactionContext context = localTxContextHolder.get();
    String transactionId = context != null ? context.getId() : "null";
    return "{LocalTransaction transactionId=" + transactionId + "}";
  }

  /**
   * Whether this transaction is active.
   *
   * @return {@code true} if this transaction is active
   */
  public boolean isActive() {
    return isActiveInternal(localTxContextHolder.get());
  }

  protected boolean isActiveInternal(LocalTransactionContext context) {
    return context != null;
  }

  /** Marks this transaction to undo in the end of the transaction. */
  public void setRollbackOnly() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (isActiveInternal(context)) {
      context.setRollbackOnly();
    }
  }

  /**
   * Whether this transaction is marked to be undone.
   *
   * @return {@code true} if the current transaction is marked.
   */
  public boolean isRollbackOnly() {
    LocalTransactionContext context = localTxContextHolder.get();
    if (isActiveInternal(context)) {
      return context.isRollbackOnly();
    }
    return false;
  }
}
