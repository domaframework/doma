package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.seasar.doma.internal.WrapException;

public final class FieldUtil {

  public static Object get(Field field, Object target) throws WrapException {
    assertNotNull(field);
    try {
      return field.get(target);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new WrapException(e);
    }
  }

  public static void set(Field field, Object target, Object value) throws WrapException {
    assertNotNull(field);
    try {
      field.set(target, value);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new WrapException(e);
    }
  }

  public static void setAccessible(Field field, boolean flag) throws WrapException {
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
