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
package org.seasar.doma.internal.apt;

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
/**
 * This class was forked from <a
 * href="https://github.com/seasarorg/aptina/tree/1.0.0/aptina/aptina-unit">aptina-unit</a>.
 *
 * @author koichik
 */
class AssertionUtils {

  private AssertionUtils() {}

  public static void assertNotNull(final String name, final Object param) throws AssertionError {
    if (param == null) {
      throw new AssertionError(name + " must not be null");
    }
  }

  public static void assertNotEmpty(final String name, final CharSequence string)
      throws AssertionError {
    if (string == null) {
      throw new AssertionError(name + " must not be null");
    }
    if (string.length() == 0) {
      throw new AssertionError(name + " must not be empty");
    }
  }

  public static void assertNotEmpty(final String name, final Object[] array) throws AssertionError {
    if (array == null) {
      throw new AssertionError(name + " must not be null");
    }
    if (array.length == 0) {
      throw new AssertionError(name + " must not be empty");
    }
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == null) {
        throw new AssertionError(name + "[" + i + "] must not be null");
      }
    }
  }
}
