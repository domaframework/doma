package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.doma.internal.WrapException;

/**
 * @author taedium
 * 
 */
public final class MethodUtil {

    public static <T> T invoke(Method method, Object target, Object... params)
            throws WrapException {
        assertNotNull(method);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) method.invoke(target, params);
            return result;
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new WrapException(e);
        }
    }

    public static String createSignature(String methodName, Class<?>[] paramTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append(methodName);
        buf.append("(");
        for (Class<?> paramType : paramTypes) {
            if (paramType.isArray()) {
                buf.append(paramType.getComponentType().getName());
                buf.append("[]");
            } else {
                buf.append(paramType.getName());
            }
            buf.append(", ");
        }
        if (buf.length() > 2) {
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        return buf.toString();
    }
}
