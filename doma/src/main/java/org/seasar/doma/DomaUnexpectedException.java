package org.seasar.doma;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomaUnexpectedException extends DomaException {

    private static final long serialVersionUID = 1L;

    public DomaUnexpectedException(Throwable cause) {
        super(MessageCode.DOMA0002, cause, cause);
    }

}
