package org.seasar.doma.domain;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomainIncomparableException extends DomainException {

    private static final long serialVersionUID = 1L;

    public DomainIncomparableException() {
        super(MessageCode.DOMA1001);
    }

}
