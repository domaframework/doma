package org.seasar.doma.jdbc.tx;

import java.io.Serializable;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a transaction is already begun.
 */
public class TransactionAlreadyBegunException extends JdbcException implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransactionAlreadyBegunException(String transactionId) {
        super(Message.DOMA2045, transactionId);
    }

}
