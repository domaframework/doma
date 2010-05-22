/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
        } catch (IllegalArgumentException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        } catch (InvocationTargetException e) {
            throw new WrapException(e);
        }
    }

    public static String createSignature(String methodName,
            Class<?>[] paramTypes) {
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
