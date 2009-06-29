package org.seasar.doma.internal.expr;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class ExpressionException extends DomaException {

    private static final long serialVersionUID = 1L;

    public ExpressionException(MessageCode messageCode, Object... args) {
        super(messageCode, args);
    }

    public ExpressionException(MessageCode messageCode, Throwable cause,
            Object... args) {
        super(messageCode, cause, args);
    }

}
