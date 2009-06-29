package org.seasar.doma.domain;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomainException extends DomaException {

    private static final long serialVersionUID = 1L;

    public DomainException(MessageCode messageCode, Object... args) {
        super(messageCode, args);
    }

    public DomainException(MessageCode messageCode, Throwable cause,
            Object... args) {
        super(messageCode, cause, args);
    }

}
