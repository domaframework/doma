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
