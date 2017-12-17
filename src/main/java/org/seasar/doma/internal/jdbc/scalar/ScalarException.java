package org.seasar.doma.internal.jdbc.scalar;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;

public class ScalarException extends DomaException {

    private static final long serialVersionUID = 1L;

    public ScalarException(Message messageCode, Object... args) {
        super(messageCode, args);
    }

    public ScalarException(Message messageCode, Throwable cause, Object... args) {
        super(messageCode, cause, args);
    }

}
