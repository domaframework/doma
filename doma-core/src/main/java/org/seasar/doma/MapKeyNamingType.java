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
package org.seasar.doma;

import org.seasar.doma.internal.util.StringUtil;

/**
 * Defines naming convention rules for the keys contained in a {@code Map<Object, String>} object.
 *
 * <p>The key name is resolved from a column name by applying this convention. This is used
 * when mapping database query results to Map objects.
 *
 * <p>Different naming conventions can be applied to transform column names into map keys
 * according to the desired format (camel case, upper case, lower case, or unchanged).
 */
public enum MapKeyNamingType {

  /** Does nothing. */
  NONE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text;
    }
  },

  /**
   * Converts an underscore separated string to a camel case string.
   *
   * <p>For example, {@code AAA_BBB} is converted to {@code aaaBbb}.
   */
  CAMEL_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  /**
   * Converts a string to an upper case string.
   *
   * <p>For example、{@code aaaBbb} is converted to {@code AAABBB}.
   */
  UPPER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toUpperCase();
    }
  },

  /**
   * Converts a string to an lower case string.
   *
   * <p>For example, {@code aaaBbb} is converted to {@code aaabbb}.
   */
  LOWER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toLowerCase();
    }
  };

  /**
   * Applies this naming conversion.
   *
   * @param text the text
   * @return the converted text
   */
  public abstract String apply(String text);
}
