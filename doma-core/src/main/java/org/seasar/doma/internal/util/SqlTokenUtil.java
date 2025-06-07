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

public final class SqlTokenUtil {

  // Fast lookup tables for ASCII characters (0-127)
  // Using bitmask approach for maximum performance
  private static final long WHITESPACE_MASK_LOW;
  private static final long WHITESPACE_MASK_HIGH;
  private static final long OPERATOR_MASK_LOW;
  private static final long OPERATOR_MASK_HIGH;

  // Pre-computed lookup table for non-ASCII characters that are definitely word parts
  private static final boolean[] WORD_PART_TABLE = new boolean[128];

  static {
    // Initialize whitespace bitmasks
    // SqlTokenUtil whitespace chars: 0x09, 0x0B, 0x0C, 0x1C, 0x1D, 0x1E, 0x1F, 0x20
    long whitespaceLow = 0L;
    long whitespaceHigh = 0L;

    whitespaceLow |= (1L << 0x09); // TAB
    whitespaceLow |= (1L << 0x0B); // VT
    whitespaceLow |= (1L << 0x0C); // FF
    whitespaceLow |= (1L << 0x1C); // FS
    whitespaceLow |= (1L << 0x1D); // GS
    whitespaceLow |= (1L << 0x1E); // RS
    whitespaceLow |= (1L << 0x1F); // US
    whitespaceLow |= (1L << 0x20); // SPACE

    WHITESPACE_MASK_LOW = whitespaceLow;
    WHITESPACE_MASK_HIGH = whitespaceHigh;

    // Initialize operator bitmasks
    // Operator chars: =<>-,/*+();
    long operatorLow = 0L;
    long operatorHigh = 0L;

    operatorLow |= (1L << '='); // 0x3D
    operatorLow |= (1L << '<'); // 0x3C
    operatorLow |= (1L << '>'); // 0x3E
    operatorLow |= (1L << '-'); // 0x2D
    operatorLow |= (1L << ','); // 0x2C
    operatorLow |= (1L << '/'); // 0x2F
    operatorLow |= (1L << '*'); // 0x2A
    operatorLow |= (1L << '+'); // 0x2B
    operatorLow |= (1L << '('); // 0x28
    operatorLow |= (1L << ')'); // 0x29
    operatorLow |= (1L << ';'); // 0x3B

    OPERATOR_MASK_LOW = operatorLow;
    OPERATOR_MASK_HIGH = operatorHigh;

    // Pre-compute word part table for ASCII range
    for (int i = 0; i < 128; i++) {
      char c = (char) i;
      // Use the original logic but pre-compute results
      boolean isJavaWhitespace = Character.isWhitespace(c);
      boolean isSqlOperator = isOperatorChar(c);
      WORD_PART_TABLE[i] = !isJavaWhitespace && !isSqlOperator;
    }
  }

  /** Fast path method to check if character is a SQL operator using bitmask. */
  private static boolean isOperatorChar(char c) {
    if (c < 64) {
      return (OPERATOR_MASK_LOW & (1L << c)) != 0;
    } else if (c < 128) {
      return (OPERATOR_MASK_HIGH & (1L << (c - 64))) != 0;
    }
    return false;
  }

  /** Optimized isWordPart method using lookup table for ASCII and fallback for Unicode. */
  public static boolean isWordPart(char c) {
    // Fast path for ASCII characters using pre-computed lookup table
    if (c < 128) {
      return WORD_PART_TABLE[c];
    }

    // Fallback for non-ASCII characters - use original logic
    if (Character.isWhitespace(c)) {
      return false;
    }

    // Non-ASCII characters are not SQL operators, so they are word parts
    return true;
  }

  /** Optimized isWhitespace method using bitmask for maximum performance. */
  public static boolean isWhitespace(char c) {
    // Ultra-fast bitmask lookup for ASCII characters
    if (c < 64) {
      return (WHITESPACE_MASK_LOW & (1L << c)) != 0;
    } else if (c < 128) {
      return (WHITESPACE_MASK_HIGH & (1L << (c - 64))) != 0;
    }

    // Non-ASCII characters are never whitespace according to SqlTokenUtil
    return false;
  }
}
