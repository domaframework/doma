package org.seasar.doma;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomaIllegalArgumentException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final String argumentName;

    protected final Object value;

    public DomaIllegalArgumentException(String argumentName, Object value) {
        super(MessageCode.DOMA0001, argumentName, value);
        this.argumentName = argumentName;
        this.value = value;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public Object getValue() {
        return value;
    }

}
