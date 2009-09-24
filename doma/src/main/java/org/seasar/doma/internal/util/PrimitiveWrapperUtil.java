package org.seasar.doma.internal.util;

public final class PrimitiveWrapperUtil {

    public static char toPrimitive(Character value) {
        return value != null ? value.charValue() : 0;
    }

    public static boolean toPrimitive(Boolean value) {
        return value != null ? value.booleanValue() : false;
    }

    public static byte toPrimitive(Byte value) {
        return value != null ? value.byteValue() : 0;
    }

    public static short toPrimitive(Short value) {
        return value != null ? value.shortValue() : 0;
    }

    public static int toPrimitive(Integer value) {
        return value != null ? value.intValue() : 0;
    }

    public static long toPrimitive(Long value) {
        return value != null ? value.longValue() : 0L;
    }

    public static float toPrimitive(Float value) {
        return value != null ? value.floatValue() : 0f;
    }

    public static double toPrimitive(Double value) {
        return value != null ? value.doubleValue() : 0d;
    }
}
