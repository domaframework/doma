package org.seasar.doma.domain;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomainValueNullPointerException extends DomainException {

    private static final long serialVersionUID = 1L;

    public DomainValueNullPointerException() {
        super(MessageCode.DOMA1002);
    }

}
