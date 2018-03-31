package org.seasar.doma.internal.util;

public final class BoxedPrimitiveUtil {

  public static char unbox(Character value) {
    return value != null ? value : 0;
  }

  public static boolean unbox(Boolean value) {
    return value != null && value;
  }

  public static byte unbox(Byte value) {
    return value != null ? value : 0;
  }

  public static short unbox(Short value) {
    return value != null ? value : 0;
  }

  public static int unbox(Integer value) {
    return value != null ? value : 0;
  }

  public static long unbox(Long value) {
    return value != null ? value : 0L;
  }

  public static float unbox(Float value) {
    return value != null ? value : 0f;
  }

  public static double unbox(Double value) {
    return value != null ? value : 0d;
  }
}
