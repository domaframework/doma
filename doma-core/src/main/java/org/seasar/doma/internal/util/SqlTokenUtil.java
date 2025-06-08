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
package org.seasar.doma.internal.util;

/**
 * High-performance SQL token character classification utility.
 *
 * <p>This class provides optimized character classification methods for SQL parsing. It delegates
 * to {@link FastCharClassifier} for maximum performance using pre-computed lookup tables.
 *
 * @see FastCharClassifier for implementation details
 */
public final class SqlTokenUtil {

  /** Private constructor to prevent instantiation. */
  private SqlTokenUtil() {}

  /**
   * Determines if a character can be part of an SQL word.
   *
   * <p>This method delegates to {@link FastCharClassifier#isWordPart(char)} for optimized
   * performance.
   *
   * @param c the character to test
   * @return true if the character can be part of an SQL word
   */
  public static boolean isWordPart(char c) {
    return FastCharClassifier.isWordPart(c);
  }

  /**
   * Determines if a character is SQL whitespace.
   *
   * <p>This method delegates to {@link FastCharClassifier#isWhitespace(char)} for optimized
   * performance.
   *
   * @param c the character to test
   * @return true if the character is SQL whitespace
   */
  public static boolean isWhitespace(char c) {
    return FastCharClassifier.isWhitespace(c);
  }
}
