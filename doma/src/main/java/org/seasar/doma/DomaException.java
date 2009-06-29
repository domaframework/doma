package org.seasar.doma;

import org.seasar.doma.message.MessageCode;
import org.seasar.doma.message.Messages;

/**
 * @author taedium
 * 
 */
public class DomaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected final Enum<?> messageCode;

    protected final Object args;

    public DomaException(MessageCode messageCode, Object... args) {
        this(messageCode, null, args);
    }

    public DomaException(MessageCode messageCode, Throwable cause,
            Object... args) {
        super(Messages.getMessage(messageCode, args), cause);
        this.messageCode = messageCode;
        this.args = args;
    }

    public Enum<?> getMessageCode() {
        return messageCode;
    }

    public Object getArgs() {
        return args;
    }

}
