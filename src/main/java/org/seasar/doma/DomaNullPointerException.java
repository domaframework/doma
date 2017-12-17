package org.seasar.doma;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that an argument is {@code null} and it does not allow in
 * the method.
 * <p>
 * This class makes it easy to distinguish Doma's specifications from Doma's
 * bugs.
 */
public class DomaNullPointerException extends DomaException {

    private static final long serialVersionUID = 1L;

    /** the parameter name that corresponds to the argument */
    protected final String parameterName;

    /**
     * Constructs an instance.
     * 
     * @param parameterName
     *            the parameter name that corresponds to the argument
     */
    public DomaNullPointerException(String parameterName) {
        super(Message.DOMA0001, parameterName);
        this.parameterName = parameterName;
    }

    /**
     * Returns the parameter name that corresponds to the argument.
     * 
     * @return the parameter name
     */
    public String getParameterName() {
        return parameterName;
    }

}
