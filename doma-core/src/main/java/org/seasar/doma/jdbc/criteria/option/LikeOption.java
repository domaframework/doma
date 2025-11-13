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
package org.seasar.doma.jdbc.criteria.option;

import java.util.Objects;

/** Represents the option about the LIKE operator. */
public interface LikeOption {

  /** The default escape character */
  char DEFAULT_ESCAPE_CHAR = '$';

  /**
   * Indicates that the option does nothing.
   *
   * @return the option
   */
  static LikeOption none() {
    return None.INSTANCE;
  }

  /**
   * Indicates that the option escapes the LIKE operand with the default escape character {@literal
   * $}.
   *
   * @return the option
   */
  static LikeOption escape() {
    return new Escape(DEFAULT_ESCAPE_CHAR);
  }

  /**
   * Indicates that the option escapes the LIKE operand with {@code escapeChar}.
   *
   * @param escapeChar the escape character
   * @return the option
   */
  static LikeOption escape(char escapeChar) {
    return new Escape(escapeChar);
  }

  /**
   * Indicates that the option escape the LIKE operand with the default escape character {@literal
   * $} and concatenates a wildcard at the end.
   *
   * <p>For example, {@code a%b_} will be converted to the a {@code a$%b$_%}.
   *
   * @return the option
   */
  static LikeOption prefix() {
    return new Prefix(DEFAULT_ESCAPE_CHAR);
  }

  /**
   * Indicates that the option escape the LIKE operand with {@code escapeChar} and concatenates a
   * wildcard at the end.
   *
   * <p>For example, {@code a%b_} is converted to the a {@code a$%b$_%}.
   *
   * @param escapeChar the escape character
   * @return the option
   */
  static LikeOption prefix(char escapeChar) {
    return new Prefix(escapeChar);
  }

  /**
   * Indicates that the option escape the LIKE operand with the default escape character {@literal
   * $} and concatenates a wildcard at the start and the end.
   *
   * <p>For example, {@code a%b_} is converted to the a {@code %a$%b$_%}.
   *
   * @return the option
   */
  static LikeOption infix() {
    return new Infix(DEFAULT_ESCAPE_CHAR);
  }

  /**
   * Indicates that the option escape the LIKE operand with {@code escapeChar} and concatenates a
   * wildcard at the start and the end.
   *
   * <p>For example, {@code a%b_} is converted to the a {@code %a$%b$_%}.
   *
   * @param escapeChar the escape character
   * @return the option
   */
  static LikeOption infix(char escapeChar) {
    return new Infix(escapeChar);
  }

  /**
   * Indicates that the option escape the LIKE operand with the default escape character {@literal
   * $} and concatenates a wildcard at the start.
   *
   * <p>For example, {@code a%b_} is converted to the a {@code %a$%b$_}.
   *
   * @return the option
   */
  static LikeOption suffix() {
    return new Suffix(DEFAULT_ESCAPE_CHAR);
  }

  /**
   * Indicates that the option escape the LIKE operand with {@code escapeChar} and concatenates a
   * wildcard at the start.
   *
   * <p>For example, {@code a%b_} is converted to the a {@code %a$%b$_}.
   *
   * @param escapeChar the escape character
   * @return the option
   */
  static LikeOption suffix(char escapeChar) {
    return new Suffix(escapeChar);
  }

  /**
   * Accepts a visitor.
   *
   * @param visitor a visitor
   */
  void accept(Visitor visitor);

  class None implements LikeOption {
    private static final None INSTANCE = new None();

    private None() {}

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Escape implements LikeOption {
    public final char escapeChar;

    public Escape(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Prefix implements LikeOption {
    public final char escapeChar;

    public Prefix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Infix implements LikeOption {
    public final char escapeChar;

    public Infix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  class Suffix implements LikeOption {
    public final char escapeChar;

    public Suffix(char escapeChar) {
      this.escapeChar = escapeChar;
    }

    @Override
    public void accept(Visitor visitor) {
      Objects.requireNonNull(visitor);
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(None none);

    void visit(Escape escape);

    void visit(Prefix prefix);

    void visit(Infix infix);

    void visit(Suffix suffix);
  }
}
