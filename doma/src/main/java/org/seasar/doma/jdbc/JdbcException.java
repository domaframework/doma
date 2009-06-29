package org.seasar.doma.jdbc;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class JdbcException extends DomaException {

    private static final long serialVersionUID = 1L;

    public JdbcException(MessageCode messageCode, Object... args) {
        super(messageCode, args);
    }

    public JdbcException(MessageCode messageCode, Throwable cause,
            Object... args) {
        super(messageCode, cause, args);
    }

}
