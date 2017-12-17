package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the DAO method is not found.
 */
public class DaoMethodNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String className;

    protected final String signature;

    public DaoMethodNotFoundException(Throwable cause, String className, String signature) {
        super(Message.DOMA2215, cause, className, signature, cause);
        this.className = className;
        this.signature = signature;
    }

    public String getClassName() {
        return className;
    }

    public String getSignature() {
        return signature;
    }
}
