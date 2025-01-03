/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
