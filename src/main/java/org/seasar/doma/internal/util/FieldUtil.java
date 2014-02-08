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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.seasar.doma.internal.WrapException;

/**
 * @author taedium
 * 
 */
public final class FieldUtil {

    public static Object get(Field field, Object target) throws WrapException {
        assertNotNull(field);
        try {
            return field.get(target);
        } catch (IllegalArgumentException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        }
    }

    public static void set(Field field, Object target, Object value)
            throws WrapException {
        assertNotNull(field);
        try {
            field.set(target, value);
        } catch (IllegalArgumentException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        }
    }

    public static void setAccessible(Field field, boolean flag)
            throws WrapException {
        assertNotNull(field);
        try {
            field.setAccessible(flag);
        } catch (SecurityException e) {
            throw new WrapException(e);
        }
    }

    public static boolean isPublic(Field field) {
        assertNotNull(field);
        return Modifier.isPublic(field.getModifiers());
    }
}
