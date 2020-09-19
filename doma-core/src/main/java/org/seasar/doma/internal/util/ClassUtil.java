package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.internal.WrapException;

public final class ClassUtil {

  public static <T> T newInstance(Class<T> clazz) throws WrapException {
    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new WrapException(e);
    }
  }

  public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes)
      throws WrapException {
    try {
      return clazz.getConstructor(parameterTypes);
    } catch (SecurityException | NoSuchMethodException e) {
      throw new WrapException(e);
    }
  }

  public static <T> Method getMethod(Class<T> clazz, String name, Class<?>... parameterTypes)
      throws WrapException {
    try {
      return clazz.getMethod(name, parameterTypes);
    } catch (SecurityException | NoSuchMethodException e) {
      throw new WrapException(e);
    }
  }

  public static <T> Method getDeclaredMethod(
      Class<T> clazz, String name, Class<?>... parameterTypes) throws WrapException {
    assertNotNull(clazz, name, parameterTypes);
    try {
      return clazz.getDeclaredMethod(name, parameterTypes);
    } catch (SecurityException | NoSuchMethodException e) {
      throw new WrapException(e);
    }
  }

  public static <T> Field getDeclaredField(Class<T> clazz, String name) throws WrapException {
    assertNotNull(clazz, name);
    try {
      return clazz.getDeclaredField(name);
    } catch (SecurityException | NoSuchFieldException e) {
      throw new WrapException(e);
    }
  }

  public static String getPackageName(String binaryName) {
    assertNotNull(binaryName);
    int pos = binaryName.lastIndexOf('.');
    if (pos < 0) {
      return "";
    }
    return binaryName.substring(0, pos);
  }

  public static List<String> getEnclosingNames(String binaryName) {
    assertNotNull(binaryName);
    String packageExcludedName = getLastPart(binaryName, '.');
    List<String> names = Arrays.asList(packageExcludedName.split("\\$"));
    if (names.size() <= 1) {
      return Collections.emptyList();
    }
    return names.subList(0, names.size() - 1);
  }

  public static String getSimpleName(String binaryName) {
    assertNotNull(binaryName);
    String packageExcludedName = getLastPart(binaryName, '.');
    return getLastPart(packageExcludedName, '$');
  }

  private static String getLastPart(String text, char ch) {
    int pos = text.lastIndexOf(ch);
    if (pos < 0) {
      return text;
    }
    return text.substring(pos + 1);
  }

  public static Class<?> toBoxedPrimitiveTypeIfPossible(Class<?> clazz) {
    assertNotNull(clazz);
    if (clazz == void.class) {
      return Void.class;
    }
    if (clazz == char.class) {
      return Character.class;
    }
    if (clazz == boolean.class) {
      return Boolean.class;
    }
    if (clazz == byte.class) {
      return Byte.class;
    }
    if (clazz == short.class) {
      return Short.class;
    }
    if (clazz == int.class) {
      return Integer.class;
    }
    if (clazz == long.class) {
      return Long.class;
    }
    if (clazz == float.class) {
      return Float.class;
    }
    if (clazz == double.class) {
      return Double.class;
    }
    return clazz;
  }

  public static <R> R traverse(Class<?> clazz, Function<Class<?>, R> f) {
    assertNotNull(clazz);
    {
      R result = f.apply(clazz);
      if (result != null) {
        return result;
      }
    }
    for (Class<?> c : clazz.getInterfaces()) {
      R result = traverse(c, f);
      if (result != null) {
        return result;
      }
    }
    Class<?> superclass = clazz.getSuperclass();
    if (superclass == null) {
      return null;
    }
    return traverse(superclass, f);
  }
}
