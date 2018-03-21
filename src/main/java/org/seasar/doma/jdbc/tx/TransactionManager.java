package org.seasar.doma.jdbc.tx;

import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;

/**
 * A transaction manager.
 * <p>
 * The implementation instance must be thread safe.
 */
public interface TransactionManager {

    /**
     * Executes the transaction whose attribute is REQUIRED.
     * 
     * @param block
     *            the code that is executed in the transaction
     */
    void required(Runnable block);

    /**
     * Executes the transaction whose attribute is REQUIRED with the specified
     * transaction isolation level.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param block
     *            the code that is executed in the transaction
     */
    void required(TransactionIsolationLevel isolationLevel, Runnable block);

    /**
     * Executes the transaction whose attribute is REQUIRED and returns the
     * result.
     * 
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT required(Supplier<RESULT> supplier);

    /**
     * Executes the transaction whose attribute is REQUIRED with the specified
     * transaction isolation level and return the result.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT required(TransactionIsolationLevel isolationLevel,
                             Supplier<RESULT> supplier);

    /**
     * Executes the transaction whose attribute is REQUIRES_NEW.
     * 
     * @param block
     *            the code that is executed in the transaction
     */
    void requiresNew(Runnable block);

    /**
     * Executes the transaction whose attribute is REQUIRES_NEW with the
     * specified transaction isolation level.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param block
     *            the code that is executed in the transaction
     */
    void requiresNew(TransactionIsolationLevel isolationLevel, Runnable block);

    /**
     * Executes the transaction whose attribute is REQUIRES_NEW and returns the
     * result.
     * 
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT requiresNew(Supplier<RESULT> supplier);

    /**
     * Executes the transaction whose attribute is REQUIRES_NEW with the
     * specified transaction isolation level and return the result.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT requiresNew(TransactionIsolationLevel isolationLevel,
                                Supplier<RESULT> supplier);

    /**
     * Executes the transaction whose attribute is NOT_SUPPORTED.
     * 
     * @param block
     *            the code that is executed in the transaction
     */
    void notSupported(Runnable block);

    /**
     * Executes the transaction whose attribute is NOT_SUPPORTED with the
     * specified transaction isolation level.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param block
     *            the code that is executed in the transaction
     */
    void notSupported(TransactionIsolationLevel isolationLevel, Runnable block);

    /**
     * Executes the transaction whose attribute is NOT_SUPPORTED and returns the
     * result.
     * 
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT notSupported(Supplier<RESULT> supplier);

    /**
     * Executes the transaction whose attribute is NOT_SUPPORTED with the
     * specified transaction isolation level and return the result.
     * 
     * @param isolationLevel
     *            the transaction isolation level
     * @param supplier
     *            the code that is executed in the transaction
     * @param <RESULT>
     *            the result type
     * @return the result
     */
    <RESULT> RESULT notSupported(TransactionIsolationLevel isolationLevel,
                                 Supplier<RESULT> supplier);

    /**
     * Marks the current transaction to undo in the end of the transaction.
     */
    void setRollbackOnly();

    /**
     * Whether the current transaction is marked to be undone.
     * 
     * @return {@code true} if the current transaction is marked.
     */
    boolean isRollbackOnly();

    /**
     * Creates a save point with the specified name.
     * <p>
     * Begin a transaction before invoking this method.
     * 
     * @param savepointName
     *            the name of the save point
     * @throws DomaNullPointerException
     *             if the {@code savepointName} is {@code null}
     * @throws TransactionNotYetBegunException
     *             if the transaction is not begun
     * @throws SavepointAlreadyExistsException
     *             if the save point already exists
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    void setSavepoint(String savepointName);

    /**
     * Whether the current transaction has the save point.
     * <p>
     * Begin a transaction before invoking this method.
     * 
     * @param savepointName
     *            the name of the save point
     * @throws DomaNullPointerException
     *             if the {@code savepointName} is {@code null}
     * @throws TransactionNotYetBegunException
     *             if the transaction is not begun
     * @return {@code true} if the transaction has the save point
     */
    boolean hasSavepoint(String savepointName);

    /**
     * Removes the specified save point and subsequent save points from the
     * current transaction.
     * <p>
     * Begin a transaction before invoking this method.
     * 
     * @param savepointName
     *            the name of the save point
     * @throws DomaNullPointerException
     *             if the {@code savepointName} is {@code null}
     * @throws TransactionNotYetBegunException
     *             if the transaction is not yet begun
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    void releaseSavepoint(String savepointName);

    /**
     * Undoes all changes made after the given save point.
     * <p>
     * Begin a transaction before invoking this method.
     * 
     * @param savepointName
     *            the name of the save point
     * @throws DomaNullPointerException
     *             if the {@code savepointName} is {@code null}
     * @throws SavepointNotFoundException
     *             if the save point is not found
     * @throws TransactionNotYetBegunException
     *             if the transaction is not begun
     * @throws JdbcException
     *             if a JDBC related error occurs
     */
    void rollback(String savepointName);

}