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

public final class BoxedPrimitiveUtil {

    public static char unbox(Character value) {
        return value != null ? value.charValue() : 0;
    }

    public static boolean unbox(Boolean value) {
        return value != null ? value.booleanValue() : false;
    }

    public static byte unbox(Byte value) {
        return value != null ? value.byteValue() : 0;
    }

    public static short unbox(Short value) {
        return value != null ? value.shortValue() : 0;
    }

    public static int unbox(Integer value) {
        return value != null ? value.intValue() : 0;
    }

    public static long unbox(Long value) {
        return value != null ? value.longValue() : 0L;
    }

    public static float unbox(Float value) {
        return value != null ? value.floatValue() : 0f;
    }

    public static double unbox(Double value) {
        return value != null ? value.doubleValue() : 0d;
    }
}
