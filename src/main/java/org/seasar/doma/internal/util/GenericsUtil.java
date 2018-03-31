package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author taedium
 * @since 1.27.1
 */
public final class GenericsUtil {

  public static Class<?> inferTypeArgument(Class<?> clazz, TypeVariable<?> typeVariable) {
    assertNotNull(clazz, typeVariable);

    var inferrer = new TypeArgumentInferrer(clazz, typeVariable);
    var arg = inferrer.infer();
    if (arg instanceof Class) {
      return (Class<?>) arg;
    }
    return null;
  }

  protected static class TypeArgumentInferrer {

    protected final Class<?> clazz;

    protected final GenericDeclaration genericDeclaration;

    protected final int index;

    public TypeArgumentInferrer(Class<?> clazz, TypeVariable<?> typeVariable) {
      this.clazz = clazz;
      this.genericDeclaration = typeVariable.getGenericDeclaration();
      this.index = getTypeParameterIndex(genericDeclaration, typeVariable);
    }

    private int getTypeParameterIndex(
        GenericDeclaration genericDeclaration, TypeVariable<?> typeVariable) {
      Type[] types = genericDeclaration.getTypeParameters();
      for (int i = 0, len = types.length; i < len; i++) {
        if (types[i] == typeVariable) {
          return i;
        }
      }
      return 0;
    }

    public Type infer() {
      var superclass = clazz.getSuperclass();
      var superclassType = clazz.getGenericSuperclass();
      if (superclass != null) {
        var arg = getTypeArgumentRecursive(superclass, superclassType);
        if (arg != null) {
          return arg;
        }
      }

      var interfaces = clazz.getInterfaces();
      var interfaceTypes = clazz.getGenericInterfaces();
      for (var i = 0; i < interfaces.length; i++) {
        var arg = getTypeArgumentRecursive(interfaces[i], interfaceTypes[i]);
        if (arg != null) {
          return arg;
        }
      }

      return null;
    }

    protected Type getTypeArgumentRecursive(Class<?> clazz, Type type) {
      if (clazz == null) {
        return null;
      }

      var arg = getTypeArgument(clazz, type);
      if (arg != null) {
        return arg;
      }

      var superclass = clazz.getSuperclass();
      var superclassType = clazz.getGenericSuperclass();
      if (superclass != null) {
        arg = getTypeArgumentRecursive(superclass, superclassType);
        if (arg != null) {
          return arg;
        }
      }

      var interfaces = clazz.getInterfaces();
      var interfaceTypes = clazz.getGenericInterfaces();
      for (var i = 0; i < interfaces.length; i++) {
        arg = getTypeArgumentRecursive(interfaces[i], interfaceTypes[i]);
        if (arg != null) {
          return arg;
        }
      }

      return null;
    }

    protected Type getTypeArgument(Class<?> clazz, Type type) {
      if (genericDeclaration == clazz && type instanceof ParameterizedType) {
        var parameterizedType = (ParameterizedType) type;
        var args = parameterizedType.getActualTypeArguments();
        if (index < args.length) {
          return args[index];
        }
      }
      return null;
    }
  }
}
