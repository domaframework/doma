package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a JDBC related unsupported method is invoked.
 */
public class JdbcUnsupportedOperationException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String className;

    protected final String methodName;

    public JdbcUnsupportedOperationException(String className, String methodName) {
        super(Message.DOMA2034, className, methodName);
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Returns the class name.
     * 
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Return the unsupported method name.
     * 
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

}
