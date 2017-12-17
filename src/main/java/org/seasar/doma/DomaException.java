package org.seasar.doma;

import org.seasar.doma.message.MessageResource;

/**
 * The root exception in the Doma framework.
 */
public class DomaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** the message resource */
    protected final MessageResource messageResource;

    /** the message arguments */
    protected final Object args;

    /**
     * Constructs an instance.
     * 
     * @param messageResource
     *            the message resource
     * @param args
     *            the message arguments
     */
    public DomaException(MessageResource messageResource, Object... args) {
        this(messageResource, null, args);
    }

    /**
     * Constructs an instance with a cause.
     * 
     * @param messageResource
     *            the message resource
     * @param cause
     *            the cause error or exception
     * @param args
     *            the message arguments
     */
    public DomaException(MessageResource messageResource, Throwable cause, Object... args) {
        super(messageResource.getMessage(args), cause);
        this.messageResource = messageResource;
        this.args = args;
    }

    /**
     * Returns the message resource.
     * 
     * @return the message resource
     */
    public MessageResource getMessageResource() {
        return messageResource;
    }

    /**
     * Returns the message arguments.
     * 
     * @return the message arguments
     */
    public Object getArgs() {
        return args;
    }

}
