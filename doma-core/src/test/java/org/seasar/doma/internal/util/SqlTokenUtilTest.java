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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for SqlTokenUtil to verify character classification logic. These tests serve as a
 * specification for the character classification behavior and can be used as a reference for future
 * optimizations.
 */
public class SqlTokenUtilTest {

  @Test
  public void testIsWhitespace_standardWhitespaceCharacters() {
    // Test all whitespace characters defined in SqlTokenUtil
    assertTrue(SqlTokenUtil.isWhitespace('\u0009')); // TAB
    assertTrue(SqlTokenUtil.isWhitespace('\u000B')); // VT (Vertical Tab)
    assertTrue(SqlTokenUtil.isWhitespace('\u000C')); // FF (Form Feed)
    assertTrue(SqlTokenUtil.isWhitespace('\u001C')); // FS (File Separator)
    assertTrue(SqlTokenUtil.isWhitespace('\u001D')); // GS (Group Separator)
    assertTrue(SqlTokenUtil.isWhitespace('\u001E')); // RS (Record Separator)
    assertTrue(SqlTokenUtil.isWhitespace('\u001F')); // US (Unit Separator)
    assertTrue(SqlTokenUtil.isWhitespace('\u0020')); // SPACE
  }

  @Test
  public void testIsWhitespace_nonWhitespaceCharacters() {
    // Test common non-whitespace characters
    assertFalse(SqlTokenUtil.isWhitespace('a'));
    assertFalse(SqlTokenUtil.isWhitespace('Z'));
    assertFalse(SqlTokenUtil.isWhitespace('0'));
    assertFalse(SqlTokenUtil.isWhitespace('9'));
    assertFalse(SqlTokenUtil.isWhitespace('_'));
    assertFalse(SqlTokenUtil.isWhitespace('$'));
    assertFalse(SqlTokenUtil.isWhitespace('='));
    assertFalse(SqlTokenUtil.isWhitespace('<'));
    assertFalse(SqlTokenUtil.isWhitespace('>'));
    assertFalse(SqlTokenUtil.isWhitespace('-'));
    assertFalse(SqlTokenUtil.isWhitespace(','));
    assertFalse(SqlTokenUtil.isWhitespace('/'));
    assertFalse(SqlTokenUtil.isWhitespace('*'));
    assertFalse(SqlTokenUtil.isWhitespace('+'));
    assertFalse(SqlTokenUtil.isWhitespace('('));
    assertFalse(SqlTokenUtil.isWhitespace(')'));
    assertFalse(SqlTokenUtil.isWhitespace(';'));
  }

  @Test
  public void testIsWhitespace_commonWhitespaceNotInSpec() {
    // Test common whitespace characters that are NOT considered whitespace by SqlTokenUtil
    assertFalse(SqlTokenUtil.isWhitespace('\n')); // Line Feed
    assertFalse(SqlTokenUtil.isWhitespace('\r')); // Carriage Return
    assertFalse(SqlTokenUtil.isWhitespace((char) 0x000A)); // LF (same as \n)
    assertFalse(SqlTokenUtil.isWhitespace((char) 0x000D)); // CR (same as \r)
  }

  @Test
  public void testIsWordPart_letters() {
    // Test uppercase letters
    for (char c = 'A'; c <= 'Z'; c++) {
      assertTrue(SqlTokenUtil.isWordPart(c), "Uppercase letter '" + c + "' should be word part");
    }

    // Test lowercase letters
    for (char c = 'a'; c <= 'z'; c++) {
      assertTrue(SqlTokenUtil.isWordPart(c), "Lowercase letter '" + c + "' should be word part");
    }
  }

  @Test
  public void testIsWordPart_digits() {
    // Test digits
    for (char c = '0'; c <= '9'; c++) {
      assertTrue(SqlTokenUtil.isWordPart(c), "Digit '" + c + "' should be word part");
    }
  }

  @Test
  public void testIsWordPart_specialCharacters() {
    // Test special characters that are considered word parts
    assertTrue(SqlTokenUtil.isWordPart('_')); // Underscore
    assertTrue(SqlTokenUtil.isWordPart('$')); // Dollar sign
    assertTrue(SqlTokenUtil.isWordPart('@')); // At sign
    assertTrue(SqlTokenUtil.isWordPart('#')); // Hash/pound
    assertTrue(SqlTokenUtil.isWordPart('.')); // Dot
    assertTrue(SqlTokenUtil.isWordPart('!')); // Exclamation
    assertTrue(SqlTokenUtil.isWordPart('?')); // Question mark
    assertTrue(SqlTokenUtil.isWordPart('&')); // Ampersand
    assertTrue(SqlTokenUtil.isWordPart('|')); // Pipe
    assertTrue(SqlTokenUtil.isWordPart('^')); // Caret
    assertTrue(SqlTokenUtil.isWordPart('~')); // Tilde
    assertTrue(SqlTokenUtil.isWordPart('`')); // Backtick
    assertTrue(SqlTokenUtil.isWordPart('\'')); // Single quote
    assertTrue(SqlTokenUtil.isWordPart('"')); // Double quote
    assertTrue(SqlTokenUtil.isWordPart('[')); // Left bracket
    assertTrue(SqlTokenUtil.isWordPart(']')); // Right bracket
    assertTrue(SqlTokenUtil.isWordPart('{')); // Left brace
    assertTrue(SqlTokenUtil.isWordPart('}')); // Right brace
    assertTrue(SqlTokenUtil.isWordPart(':')); // Colon
  }

  @Test
  public void testIsWordPart_nonWordPartCharacters() {
    // Test characters that are explicitly NOT word parts according to SqlTokenUtil
    assertFalse(SqlTokenUtil.isWordPart('=')); // Equals
    assertFalse(SqlTokenUtil.isWordPart('<')); // Less than
    assertFalse(SqlTokenUtil.isWordPart('>')); // Greater than
    assertFalse(SqlTokenUtil.isWordPart('-')); // Hyphen/minus
    assertFalse(SqlTokenUtil.isWordPart(',')); // Comma
    assertFalse(SqlTokenUtil.isWordPart('/')); // Forward slash
    assertFalse(SqlTokenUtil.isWordPart('*')); // Asterisk
    assertFalse(SqlTokenUtil.isWordPart('+')); // Plus
    assertFalse(SqlTokenUtil.isWordPart('(')); // Left parenthesis
    assertFalse(SqlTokenUtil.isWordPart(')')); // Right parenthesis
    assertFalse(SqlTokenUtil.isWordPart(';')); // Semicolon
  }

  @Test
  public void testIsWordPart_whitespaceCharacters() {
    // Test that whitespace characters are NOT word parts
    assertFalse(SqlTokenUtil.isWordPart('\u0009')); // TAB
    assertFalse(SqlTokenUtil.isWordPart('\u000B')); // VT
    assertFalse(SqlTokenUtil.isWordPart('\u000C')); // FF
    assertFalse(SqlTokenUtil.isWordPart('\u001C')); // FS
    assertFalse(SqlTokenUtil.isWordPart('\u001D')); // GS
    assertFalse(SqlTokenUtil.isWordPart('\u001E')); // RS
    assertFalse(SqlTokenUtil.isWordPart('\u001F')); // US
    assertFalse(SqlTokenUtil.isWordPart('\u0020')); // SPACE
  }

  @Test
  public void testIsWordPart_lineBreakCharacters() {
    // Test line break characters behavior
    char lineFeed = '\n';
    char carriageReturn = '\r';

    // LF and CR are NOT whitespace according to SqlTokenUtil.isWhitespace()
    assertFalse(SqlTokenUtil.isWhitespace(lineFeed));
    assertFalse(SqlTokenUtil.isWhitespace(carriageReturn));

    // However, LF and CR ARE whitespace according to Character.isWhitespace()
    // So isWordPart() returns false because it uses Character.isWhitespace()
    assertFalse(SqlTokenUtil.isWordPart(lineFeed));
    assertFalse(SqlTokenUtil.isWordPart(carriageReturn));
  }

  @Test
  public void testIsWordPart_controlCharacters() {
    // Test some control characters
    char nullChar = '\u0000';
    char soh = '\u0001';
    char stx = '\u0002';
    char bs = '\u0008';
    char lf = (char) 0x000A;
    char cr = (char) 0x000D;

    // These are not whitespace according to SqlTokenUtil.isWhitespace()
    assertFalse(SqlTokenUtil.isWhitespace(nullChar));
    assertFalse(SqlTokenUtil.isWhitespace(soh));
    assertFalse(SqlTokenUtil.isWhitespace(stx));
    assertFalse(SqlTokenUtil.isWhitespace(bs));
    assertFalse(SqlTokenUtil.isWhitespace(lf));
    assertFalse(SqlTokenUtil.isWhitespace(cr));

    // However, isWordPart() uses Character.isWhitespace(), so the behavior depends on that
    assertTrue(
        SqlTokenUtil.isWordPart(nullChar)); // NULL is not whitespace in Character.isWhitespace()
    assertTrue(SqlTokenUtil.isWordPart(soh)); // SOH is not whitespace in Character.isWhitespace()
    assertTrue(SqlTokenUtil.isWordPart(stx)); // STX is not whitespace in Character.isWhitespace()
    assertTrue(SqlTokenUtil.isWordPart(bs)); // BS is not whitespace in Character.isWhitespace()
    assertFalse(SqlTokenUtil.isWordPart(lf)); // LF is whitespace in Character.isWhitespace()
    assertFalse(SqlTokenUtil.isWordPart(cr)); // CR is whitespace in Character.isWhitespace()
  }

  @Test
  public void testIsWordPart_unicodeCharacters() {
    // Test some Unicode characters
    assertTrue(SqlTokenUtil.isWordPart('α')); // Greek alpha
    assertTrue(SqlTokenUtil.isWordPart('β')); // Greek beta
    assertTrue(SqlTokenUtil.isWordPart('あ')); // Hiragana A
    assertTrue(SqlTokenUtil.isWordPart('漢')); // Kanji character
    assertTrue(SqlTokenUtil.isWordPart('€')); // Euro symbol
    assertTrue(SqlTokenUtil.isWordPart('©')); // Copyright symbol
  }

  @Test
  public void testIsWordPart_edgeCases() {
    // Test edge cases with maximum character values
    assertTrue(SqlTokenUtil.isWordPart(Character.MAX_VALUE));
    assertTrue(SqlTokenUtil.isWordPart('\uFFFF'));
  }

  @Test
  public void testConsistency_whitespaceAndWordPart() {
    // Test that no character can be both whitespace and word part
    for (int i = 0; i < 128; i++) {
      char c = (char) i;
      boolean isWhitespace = SqlTokenUtil.isWhitespace(c);
      boolean isWordPart = SqlTokenUtil.isWordPart(c);

      if (isWhitespace) {
        assertFalse(
            isWordPart,
            "Character \\u"
                + String.format("%04X", (int) c)
                + " cannot be both whitespace and word part");
      }
    }
  }

  @Test
  public void testSqlKeywordCharacters() {
    // Test characters commonly used in SQL keywords
    String sqlKeywords = "SELECT FROM WHERE ORDER BY GROUP HAVING INSERT UPDATE DELETE";
    for (char c : sqlKeywords.toCharArray()) {
      if (c == ' ') {
        assertTrue(SqlTokenUtil.isWhitespace(c));
        assertFalse(SqlTokenUtil.isWordPart(c));
      } else {
        assertFalse(SqlTokenUtil.isWhitespace(c));
        assertTrue(SqlTokenUtil.isWordPart(c));
      }
    }
  }

  @Test
  public void testSqlOperatorCharacters() {
    // Test characters commonly used as SQL operators
    String operators = "=<>+-*/,();";
    for (char c : operators.toCharArray()) {
      assertFalse(SqlTokenUtil.isWhitespace(c));
      assertFalse(SqlTokenUtil.isWordPart(c));
    }
  }

  @Test
  public void testDifferenceBetweenWhitespaceMethods() {
    // Document the key difference between SqlTokenUtil.isWhitespace() and Character.isWhitespace()
    // This is important for understanding isWordPart() behavior

    char[] commonWhitespace = {'\t', '\n', '\r', ' '};
    for (char c : commonWhitespace) {
      boolean javaIsWhitespace = Character.isWhitespace(c);
      boolean sqlTokenIsWhitespace = SqlTokenUtil.isWhitespace(c);
      boolean isWordPart = SqlTokenUtil.isWordPart(c);

      // isWordPart() should return false if Character.isWhitespace() returns true
      if (javaIsWhitespace) {
        assertFalse(
            isWordPart, "Character '" + c + "' is Java whitespace, so should not be word part");
      }

      // Document which characters differ between the two whitespace methods
      if (c == '\n' || c == '\r') {
        assertTrue(javaIsWhitespace); // Java considers these whitespace
        assertFalse(sqlTokenIsWhitespace); // SqlTokenUtil does not
        assertFalse(isWordPart); // But isWordPart() uses Java's definition
      } else if (c == '\t' || c == ' ') {
        assertTrue(javaIsWhitespace); // Java considers these whitespace
        assertTrue(sqlTokenIsWhitespace); // SqlTokenUtil also considers these whitespace
        assertFalse(isWordPart); // So isWordPart() returns false
      }
    }
  }

  @Test
  public void testPerformanceBaseline() {
    // Performance baseline test for future optimization comparison
    char[] testChars = new char[256];
    for (int i = 0; i < 256; i++) {
      testChars[i] = (char) i;
    }

    // Warm up
    for (int i = 0; i < 1000; i++) {
      for (char c : testChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
      }
    }

    // Measure performance
    long startTime = System.nanoTime();
    for (int i = 0; i < 10000; i++) {
      for (char c : testChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
      }
    }
    long endTime = System.nanoTime();

    long duration = endTime - startTime;

    // This test will always pass - it's just for performance measurement
    assertTrue(duration > 0);

    // Note: Performance results can be used to compare with optimized versions
    System.out.println(
        "SqlTokenUtil performance baseline: " + duration + " ns for 2,560,000 calls");
  }
}
