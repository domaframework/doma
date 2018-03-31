package org.seasar.doma.internal.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.seasar.doma.internal.WrapException;

/** @author taedium */
public final class ConstructorUtil {

  public static <T> T newInstance(Constructor<T> constructor, Object... params)
      throws WrapException {
    try {
      return constructor.newInstance(params);
    } catch (IllegalArgumentException
        | InvocationTargetException
        | IllegalAccessException
        | InstantiationException e) {
      throw new WrapException(e);
    }
  }

  public static String createSignature(Constructor<?> constructor) {
    return createSignature(constructor.getDeclaringClass(), constructor.getParameterTypes());
  }

  public static String createSignature(Class<?> clazz, Class<?>[] paramTypes) {
    StringBuilder buf = new StringBuilder();
    buf.append(clazz.getName());
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
