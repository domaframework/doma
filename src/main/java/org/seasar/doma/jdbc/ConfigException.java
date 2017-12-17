package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that there is an inadequate configuration in a
 * {@link Config} object.
 */
public class ConfigException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String className;

    protected final String methodName;

    public ConfigException(String className, String methodName) {
        super(Message.DOMA2035, className, methodName);
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

}
