package org.seasar.doma.domain;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomainUnsupportedOperationException extends DomainException {

    private static final long serialVersionUID = 1L;

    public DomainUnsupportedOperationException() {
        super(MessageCode.DOMA1003);
    }
}
