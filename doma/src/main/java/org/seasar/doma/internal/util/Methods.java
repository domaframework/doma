package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.doma.internal.WrapException;


/**
 * @author taedium
 * 
 */
public final class Methods {

    public static <T> T invoke(Method method, Object target, Object... params)
            throws WrapException {
        assertNotNull(method);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) method.invoke(target, params);
            return result;
        } catch (IllegalArgumentException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        } catch (InvocationTargetException e) {
            throw new WrapException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String name,
            Class<?>... parameterTypes) throws WrapException {
        assertNotNull(clazz, name, parameterTypes);
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            throw new WrapException(e);
        } catch (NoSuchMethodException e) {
            throw new WrapException(e);
        }
    }
}
