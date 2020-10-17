package org.seasar.doma.jdbc.entity;

import org.seasar.doma.Column;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Table;
import org.seasar.doma.internal.util.StringUtil;

/**
 * A naming convention.
 *
 * <p>This convention resolves the table name from the entity name when {@link Table#name()} is not
 * specified at the entity class, and also resolves the column name from the entity property name
 * when {@link Column#name()} is not specified at the property field.
 */
public enum NamingType {

  /** Converts nothing. */
  NONE {

    @Override
    public String apply(String text) {
      return text;
    }

    @Override
    public String revert(String text) {
      return text;
    }
  },

  /**
   * Converts the camel case text to the text that is snake case and upper case.
   *
   * <p>For examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>AAA_BBB</code>.
   *   <li><code>aaa3Bbb</code> is converted to <code>AAA3_BBB</code>.
   * </ul>
   */
  SNAKE_UPPER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCase(text);
      return s.toUpperCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  /**
   * Converts the camel case text to the text that is snake case and lower case.
   *
   * <p>For examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>aaa_bbb</code>.
   *   <li><code>aaa3Bbb</code> is converted to <code>aaa3_bbb</code>.
   * </ul>
   */
  SNAKE_LOWER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCase(text);
      return s.toLowerCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  @Deprecated
  LENIENT_SNAKE_UPPER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCaseWithLenient(text);
      return s.toUpperCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  @Deprecated
  LENIENT_SNAKE_LOWER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      String s = StringUtil.fromCamelCaseToSnakeCaseWithLenient(text);
      return s.toLowerCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  /**
   * Converts the camel case text to the upper case text.
   *
   * <p>For examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>AAABBB</code>.
   * </ul>
   */
  UPPER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toUpperCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toLowerCase();
    }
  },

  /**
   * Converts the camel case text to the lower case text.
   *
   * <p>For examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>aaabbb</code>.
   * </ul>
   */
  LOWER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toLowerCase();
    }

    @Override
    public String revert(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text;
    }
  };

  /**
   * Applies this convention.
   *
   * @param text the entity name or the property name
   * @return the table name or the column name
   */
  public abstract String apply(String text);

  /**
   * Reverts the text to the original as much as possible.
   *
   * @param text the text that is converted by this convention
   * @return the original
   */
  public abstract String revert(String text);
}
