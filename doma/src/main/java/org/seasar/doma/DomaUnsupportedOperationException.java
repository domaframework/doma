package org.seasar.doma;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomaUnsupportedOperationException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final String className;

    protected final String methodName;

    public DomaUnsupportedOperationException(String className, String methodName) {
        super(MessageCode.DOMA0004, className, methodName);
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
