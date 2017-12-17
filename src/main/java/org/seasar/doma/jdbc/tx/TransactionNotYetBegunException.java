package org.seasar.doma.jdbc.tx;

import java.io.Serializable;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a transaction is not yet begun.
 */
public class TransactionNotYetBegunException extends JdbcException implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransactionNotYetBegunException(Message message, Object... args) {
        super(message, args);
    }

}
