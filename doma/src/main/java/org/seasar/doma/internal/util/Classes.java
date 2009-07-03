/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import static org.seasar.doma.internal.util.Assertions.*;

import java.lang.reflect.Method;

import org.seasar.doma.internal.WrapException;

/**
 * @author taedium
 * 
 */
public final class Classes {

    public static <T> T newInstance(Class<T> clazz) throws WrapException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        }
    }

    public static <T> T newInstance(String name) throws WrapException {
        Class<T> clazz = forName(name);
        return newInstance(clazz);
    }

    public static <T> Class<T> forName(String name) throws WrapException {
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(name);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new WrapException(e);
        }
    }

    public static <T> Method getMethod(Class<T> clazz, String name,
            Class<?>... parameterTypes) throws WrapException {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            throw new WrapException(e);
        } catch (NoSuchMethodException e) {
            throw new WrapException(e);
        }
    }

    public static Class<?> toPrimitiveClassIfWrapper(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return boolean.class;
        } else if (clazz == Byte.class) {
            return byte.class;
        } else if (clazz == Short.class) {
            return short.class;
        } else if (clazz == Integer.class) {
            return int.class;
        } else if (clazz == Long.class) {
            return long.class;
        } else if (clazz == Float.class) {
            return float.class;
        } else if (clazz == Double.class) {
            return double.class;
        }
        return clazz;
    }

    public static String getPackageName(String qualifiedName) {
        assertNotNull(qualifiedName);
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            return "";
        }
        return qualifiedName.substring(0, pos);
    }

    public static String getSimpleName(String qualifiedName) {
        assertNotNull(qualifiedName);
        int pos = qualifiedName.lastIndexOf('.');
        if (pos < 0) {
            return qualifiedName;
        }
        return qualifiedName.substring(pos + 1);
    }

}
