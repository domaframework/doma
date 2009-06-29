package org.seasar.doma.jdbc;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomaConfigException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final String className;

    protected final String methodName;

    public DomaConfigException(String className, String methodName) {
        super(MessageCode.DOMA0003, className, methodName);
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
