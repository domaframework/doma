package org.seasar.doma;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the argument does not meet any preconditions in the
 * method.
 * <p>
 * This class makes it easy to distinguish Doma's specifications from Doma's
 * bugs.
 */
public class DomaIllegalArgumentException extends DomaException {

    private static final long serialVersionUID = 1L;

    /** the parameter name that corresponds to the illegal argument */
    protected final String parameterName;

    /** the detail message */
    protected final String description;

    /**
     * Constructs an instance.
     * 
     * @param parameterName
     *            the parameter name
     * @param description
     *            the detail message
     */
    public DomaIllegalArgumentException(String parameterName, String description) {
        super(Message.DOMA0002, parameterName, description);
        this.parameterName = parameterName;
        this.description = description;
    }

    /**
     * Returns the parameter name that corresponds to the illegal argument.
     * 
     * @return the parameter name
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Returns the detail message.
     * 
     * @return the detail message
     */
    public String getDescription() {
        return description;
    }

}
