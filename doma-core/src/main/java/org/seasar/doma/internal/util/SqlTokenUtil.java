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
 * <p>This class provides optimized character classification methods for SQL parsing using
 * pre-computed lookup tables for maximum performance.
 *
 * <h2>Performance Benefits</h2>
 *
 * <ul>
 *   <li><strong>Constant Time</strong>: O(1) lookup for ASCII characters (0-127)
 *   <li><strong>Branch Reduction</strong>: Eliminates multiple if-else chains
 *   <li><strong>Cache Friendly</strong>: Small lookup tables fit in CPU cache
 *   <li><strong>JIT Optimization</strong>: Simple array access is highly optimizable
 * </ul>
 *
 * <h2>Design Principles</h2>
 *
 * <ul>
 *   <li><strong>ASCII Optimization</strong>: Most SQL content is ASCII, so we optimize for this
 *       case
 *   <li><strong>Bit Flags</strong>: Use byte bit flags for efficient character property storage
 *   <li><strong>Static Initialization</strong>: Pre-compute all classifications at class load time
 *   <li><strong>Compatibility</strong>: Maintains exact behavior compatibility with
 *       ClassicSqlTokenUtil
 * </ul>
 */
public final class SqlTokenUtil {

  /** ASCII range size for lookup table optimization. */
  private static final int ASCII_SIZE = 128;

  /** Bit flag indicating the character is SQL whitespace. */
  private static final byte TYPE_WHITESPACE = 1;

  /** Bit flag indicating the character is a SQL delimiter/operator that terminates words. */
  private static final byte TYPE_DELIMITER = 2;

  /** Pre-computed character type table for ASCII range (0-127). */
  private static final byte[] CHAR_TYPES = new byte[ASCII_SIZE];

  static {
    // Pre-compute character classifications for all ASCII characters
    // Using ClassicSqlTokenUtil logic: exclude only whitespace and specific delimiters
    for (int i = 0; i < ASCII_SIZE; i++) {
      char c = (char) i;
      byte type = 0;

      // SQL whitespace characters (matching ClassicSqlTokenUtil)
      if (isWhitespaceChar(c)) {
        type |= TYPE_WHITESPACE;
      }

      // SQL word delimiters (matching ClassicSqlTokenUtil exclusion list)
      if (isDelimiterChar(c)) {
        type |= TYPE_DELIMITER;
      }

      CHAR_TYPES[i] = type;
    }
  }

  /** Private constructor to prevent instantiation. */
  private SqlTokenUtil() {}

  /**
   * Determines if a character can be part of an SQL word.
   *
   * <p>This method uses the same logic as ClassicSqlTokenUtil: a character is considered a word
   * part unless it's whitespace or one of the specific SQL delimiters. This method is optimized for
   * ASCII characters with O(1) lookup time.
   *
   * @param c the character to test
   * @return true if the character can be part of an SQL word
   */
  public static boolean isWordPart(char c) {
    if (c < ASCII_SIZE) {
      // Fast path: use pre-computed table
      // Character is word part if it's neither whitespace nor delimiter
      return (CHAR_TYPES[c] & (TYPE_WHITESPACE | TYPE_DELIMITER)) == 0;
    }
    // Fallback for non-ASCII characters - matches ClassicSqlTokenUtil behavior
    // Non-ASCII characters are word parts unless they're whitespace
    return !Character.isWhitespace(c);
  }

  /**
   * Determines if a character is SQL whitespace.
   *
   * <p>SQL whitespace includes space, tab, form feed, and other control characters that should be
   * treated as separators in SQL parsing.
   *
   * @param c the character to test
   * @return true if the character is SQL whitespace
   */
  public static boolean isWhitespace(char c) {
    if (c < ASCII_SIZE) {
      return (CHAR_TYPES[c] & TYPE_WHITESPACE) != 0;
    }
    // Fallback for non-ASCII characters
    return Character.isWhitespace(c);
  }

  /**
   * Helper method to identify whitespace characters during static initialization.
   *
   * @param c the character to test
   * @return true if the character should be classified as whitespace
   */
  private static boolean isWhitespaceChar(char c) {
    switch (c) {
      case '\u0009': // TAB
      case '\u000B': // Vertical TAB
      case '\u000C': // Form Feed
      case '\u001C': // File Separator
      case '\u001D': // Group Separator
      case '\u001E': // Record Separator
      case '\u001F': // Unit Separator
      case '\u0020': // Space
        return true;
      default:
        return false;
    }
  }

  /**
   * Helper method to identify delimiter characters during static initialization.
   *
   * <p>These characters match exactly the exclusion list from ClassicSqlTokenUtil.isWordPart().
   *
   * @param c the character to test
   * @return true if the character should be classified as a delimiter
   */
  private static boolean isDelimiterChar(char c) {
    switch (c) {
      case '=':
      case '<':
      case '>':
      case '-':
      case ',':
      case '/':
      case '*':
      case '+':
      case '(':
      case ')':
      case ';':
      case '\r': // Carriage return
      case '\n': // Line feed
        return true;
      default:
        return false;
    }
  }
}
