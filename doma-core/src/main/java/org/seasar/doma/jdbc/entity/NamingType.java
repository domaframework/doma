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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.Column;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Table;
import org.seasar.doma.internal.util.StringUtil;

/**
 * Defines naming conventions for converting between entity/property names and table/column names.
 *
 * <p>This enum provides strategies for automatically deriving database table names from entity class
 * names when {@link Table#name()} is not specified, and for deriving database column names from
 * entity property names when {@link Column#name()} is not specified.
 *
 * <p>Naming conventions are essential for maintaining consistent naming patterns between
 * Java code and database schemas, especially in applications that follow specific
 * naming standards.
 *
 * @see org.seasar.doma.Entity#naming()
 * @see org.seasar.doma.Table
 * @see org.seasar.doma.Column
 */
public enum NamingType {

  /**
   * Preserves the original name without any conversion.
   *
   * <p>This naming convention leaves entity and property names unchanged when
   * mapping to table and column names. Use this when your Java naming conventions
   * already match your database naming conventions.
   */
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
   * Converts camel case text to snake case with uppercase letters.
   *
   * <p>This naming convention transforms Java camelCase identifiers to
   * database-style SNAKE_CASE identifiers with all uppercase letters.
   * This is commonly used in databases that follow uppercase naming conventions.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>AAA_BBB</code>
   *   <li><code>aaa3Bbb</code> is converted to <code>AAA3_BBB</code>
   * </ul>
   *
   * @see #SNAKE_LOWER_CASE
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
   * Converts camel case text to snake case with lowercase letters.
   *
   * <p>This naming convention transforms Java camelCase identifiers to
   * database-style snake_case identifiers with all lowercase letters.
   * This is commonly used in databases that follow lowercase naming conventions.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>aaa_bbb</code>
   *   <li><code>aaa3Bbb</code> is converted to <code>aaa3_bbb</code>
   * </ul>
   *
   * @see #SNAKE_UPPER_CASE
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

  /**
   * Converts camel case text to all uppercase text without adding underscores.
   *
   * <p>This naming convention transforms Java camelCase identifiers to
   * database-style identifiers with all uppercase letters, but without
   * adding underscores between words.
   *
   * <p>Example:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>AAABBB</code>
   * </ul>
   *
   * @see #LOWER_CASE
   * @see #SNAKE_UPPER_CASE
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
   * Converts camel case text to all lowercase text without adding underscores.
   *
   * <p>This naming convention transforms Java camelCase identifiers to
   * database-style identifiers with all lowercase letters, but without
   * adding underscores between words.
   *
   * <p>Example:
   *
   * <ul>
   *   <li><code>aaaBbb</code> is converted to <code>aaabbb</code>
   * </ul>
   *
   * @see #UPPER_CASE
   * @see #SNAKE_LOWER_CASE
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
   * Applies this naming convention to convert a Java identifier to a database identifier.
   *
   * <p>This method is used to convert entity class names to table names and
   * entity property names to column names according to the specific naming convention.
   *
   * @param text the entity name or property name to convert
   * @return the converted table name or column name
   * @throws DomaNullPointerException if the text is {@code null}
   */
  public abstract String apply(String text);

  /**
   * Attempts to convert a database identifier back to its original Java identifier form.
   *
   * <p>This method performs the reverse operation of {@link #apply(String)}, converting
   * database table and column names back to their corresponding Java entity and property
   * names. The conversion may not be perfect for all naming conventions.
   *
   * @param text the database table or column name to convert back
   * @return the Java entity or property name
   * @throws DomaNullPointerException if the text is {@code null}
   */
  public abstract String revert(String text);
}
