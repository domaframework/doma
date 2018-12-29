package org.seasar.doma.jdbc;

import org.seasar.doma.internal.WrapException;

/** A helper for classes. */
public interface ClassHelper {

  /**
   * Returns the {@code Class} object associated with the class or interface with the given string
   * name.
   *
   * @param <T> the type of the class
   * @param className the full qualified name of the class
   * @return the object of the class
   * @throws Exception if the class is not found
   * @see Class#forName(String)
   */
  @SuppressWarnings("unchecked")
  default <T> Class<T> forName(String className) throws Exception {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        return (Class<T>) Class.forName(className);
      } else {
        try {
          return (Class<T>) classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
          return (Class<T>) Class.forName(className);
        }
      }
    } catch (ClassNotFoundException e) {
      throw new WrapException(e);
    }
  }
}
