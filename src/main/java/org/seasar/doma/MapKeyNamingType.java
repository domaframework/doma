package org.seasar.doma;

import org.seasar.doma.internal.util.StringUtil;

/**
 * Defines naming convention rules for the keys contained in a {@code Map<Object, String>} object.
 *
 * <p>The key name is resolved from a column name by applying this convention.
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
