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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.AND_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BIND_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.CLOSED_PARENS;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.DELIMITER;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.DISTINCT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ELSEIF_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ELSE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EMBEDDED_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.END_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EOF;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EOL;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EXCEPT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EXPAND_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FOR_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FOR_UPDATE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FROM_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.GROUP_BY_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.HAVING_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.IF_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.INTERSECT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.IN_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.LINE_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.LITERAL_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.MINUS_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OPENED_PARENS;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OPTION_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ORDER_BY_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OR_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OTHER;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.PARSER_LEVEL_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.POPULATE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.QUOTE;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.SELECT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.SET_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.UNION_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.UPDATE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WHERE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WHITESPACE;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WORD;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.nio.CharBuffer;
import org.seasar.doma.internal.expr.util.ExpressionUtil;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.internal.util.SqlTokenUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * High-performance SQL tokenizer for Doma's two-way SQL processing.
 *
 * <h2>Architecture Overview</h2>
 *
 * This tokenizer processes SQL strings character by character, identifying tokens such as:
 *
 * <ul>
 *   <li>SQL keywords (SELECT, FROM, WHERE, etc.)
 *   <li>Block comments (/&#42; &#42;/), including Doma directives (/&#42;%if&#42;/,
 *       /&#42;%expand&#42;/, etc.)
 *   <li>Line comments (--)
 *   <li>Quoted strings ('text')
 *   <li>Bind variables (/&#42;param&#42;/value)
 *   <li>Parentheses, delimiters, and other SQL syntax elements
 * </ul>
 *
 * <h2>Key Design Principles</h2>
 *
 * <ul>
 *   <li><strong>Performance-Critical</strong>: Used extensively in SQL parsing pipelines
 *   <li><strong>Memory-Efficient</strong>: Uses CharBuffer for zero-copy string operations
 *   <li><strong>Lookahead Optimization</strong>: 10-character lookahead buffer minimizes buffer
 *       operations
 * </ul>
 *
 * <h2>Internal State Management</h2>
 *
 * The tokenizer maintains several critical state variables:
 *
 * <ul>
 *   <li>{@code buf}: CharBuffer positioned at current parsing location
 *   <li>{@code lookahead}: Fixed 10-char array for efficient keyword matching
 *   <li>{@code tokenStartIndex}: Start position of current token for substring extraction
 *   <li>{@code currentLineNumber}: Line tracking for error reporting
 * </ul>
 *
 * <h2>Parsing Strategy</h2>
 *
 * The core parsing logic in {@code peek()} uses a two-phase approach:
 *
 * <ol>
 *   <li><strong>Character Classification</strong>: Determines if first character starts a word
 *   <li><strong>Optimized Parsing</strong>: Uses fall-through switch statements for efficient
 *       keyword matching
 * </ol>
 *
 * <h2>Performance Optimizations</h2>
 *
 * <ul>
 *   <li><strong>Bitwise Case Folding</strong>: {@code (ch | 0x20)} for fast case-insensitive
 *       comparison
 *   <li><strong>Fall-through Switch</strong>: Processes longer keywords first, falls through to
 *       shorter ones
 *   <li><strong>Minimal Buffer Operations</strong>: Reduces CharBuffer position changes
 *   <li><strong>Specialized Handlers</strong>: Separate methods for quotes, comments, and words
 * </ul>
 *
 * <h2>Error Handling</h2>
 *
 * Provides precise error reporting with line/column information for:
 *
 * <ul>
 *   <li>Unterminated quoted strings (DOMA2101)
 *   <li>Unterminated block comments (DOMA2102)
 *   <li>Invalid directive syntax (DOMA2119)
 * </ul>
 *
 * <h2>Maintenance Guidelines</h2>
 *
 * <ul>
 *   <li><strong>Keyword Changes</strong>: Update both {@code peekWord()} and corresponding {@code
 *       isXxxWord()} methods
 *   <li><strong>Performance Testing</strong>: Run JMH benchmarks after modifications (see {@code
 *       SqlTokenizerBenchmark})
 *   <li><strong>Buffer Position</strong>: Always ensure buffer position is correctly managed in
 *       parsing methods
 *   <li><strong>Lookahead Consistency</strong>: Verify lookahead array size matches maximum keyword
 *       length
 * </ul>
 *
 * <h2>Thread Safety</h2>
 *
 * This class is <strong>NOT thread-safe</strong>. Each parsing operation requires a separate
 * instance.
 *
 * @see SqlTokenType for token type definitions
 * @see SqlTokenUtil for character classification utilities
 */
public class SqlTokenizer {

  /** Original SQL string being tokenized. Used for substring extraction in token preparation. */
  private final String sql;

  /**
   * CharBuffer wrapper around SQL string. Maintains current parsing position and provides efficient
   * character access.
   */
  private final CharBuffer buf;

  /**
   * Fixed-size lookahead buffer for efficient keyword matching. Size of 10 accommodates the longest
   * keyword ("FOR UPDATE" = 10 chars including space). This buffer minimizes CharBuffer position
   * manipulations during parsing.
   */
  private final char[] lookahead = new char[10];

  /** Current token type determined by the most recent peek() operation. */
  private SqlTokenType type;

  /** Current token string extracted during prepareToken(). Null until first next() call. */
  private String token;

  /** Line number tracking during parsing for error reporting. Incremented on EOL detection. */
  private int currentLineNumber;

  /** Line number of the current token, captured during prepareToken(). */
  private int lineNumber;

  /** Buffer position at the start of the current line. Used for calculating column positions. */
  private int lineStartPosition;

  /** Column position of the current token within its line. */
  private int position;

  /**
   * Start index of the current token in the original SQL string. Updated after each token
   * extraction.
   */
  private int tokenStartIndex;

  public SqlTokenizer(String sql) {
    assertNotNull(sql);
    this.sql = sql;
    this.buf = CharBuffer.wrap(sql);
    currentLineNumber = 1;
    lineNumber = 1;
    peek();
  }

  /**
   * Advances to the next token and returns its type.
   *
   * <p>This method coordinates the tokenization process:
   *
   * <ol>
   *   <li>Handles EOF and EOL special cases
   *   <li>Prepares the current token (extracts substring and position info)
   *   <li>Advances parsing position and determines next token type
   * </ol>
   *
   * @return the type of the current token (before advancing)
   */
  public SqlTokenType next() {
    switch (type) {
      case EOF:
        token = null;
        return EOF;
      case EOL:
        lineStartPosition = buf.position();
      default:
        SqlTokenType currentType = type;
        prepareToken();
        peek();
        return currentType;
    }
  }

  private void prepareToken() {
    lineNumber = currentLineNumber;
    position = buf.position() - lineStartPosition;
    token = sql.substring(tokenStartIndex, buf.position());
    tokenStartIndex = buf.position();
  }

  public String getToken() {
    return token;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public int getPosition() {
    return position;
  }

  /**
   * Core tokenization method that determines the type of the next token.
   *
   * <p>This method implements a two-phase parsing strategy:
   *
   * <ol>
   *   <li>Character classification: Determines if the next character starts a word
   *   <li>Specialized parsing: Delegates to peekWord() or peekNonWord() accordingly
   * </ol>
   *
   * <p>The method fills the lookahead buffer with the appropriate number of characters to minimize
   * buffer operations during keyword matching.
   */
  private void peek() {
    if (!buf.hasRemaining()) {
      type = EOF;
      return;
    }

    int offset = buf.position();
    char c = buf.get();
    boolean isWordStart = isWordStart(c);
    buf.position(offset);

    if (isWordStart) {
      // For words, read up to lookahead.length chars for keyword matching
      int charsRead = Math.min(lookahead.length, buf.remaining());
      buf.get(lookahead, 0, charsRead);
      peekWord(offset, charsRead);
    } else {
      // For non-words, 2 chars is sufficient for comment detection and other syntax
      int charsRead = Math.min(2, buf.remaining());
      buf.get(lookahead, 0, charsRead);
      peekNonWord(offset, charsRead);
    }
  }

  /**
   * Handles word-like tokens including SQL keywords, identifiers, and quoted strings.
   *
   * <p>This method uses an optimized fall-through switch strategy:
   *
   * <ul>
   *   <li>Processes longer keywords first (FOR UPDATE = 10 chars)
   *   <li>Falls through to shorter keywords to minimize comparisons
   *   <li>Uses bitwise case folding for performance (ch | 0x20)
   * </ul>
   *
   * @param offset the starting position in the buffer
   * @param charsRead number of characters read into lookahead buffer
   */
  private void peekWord(int offset, int charsRead) {
    // Handle special word-starting characters
    if (lookahead[0] == '\'') {
      buf.position(offset + 1);
      handleQuotedString();
      return;
    }
    if (lookahead[0] == '+' || lookahead[0] == '-') {
      buf.position(offset + 1);
      handleWord();
      return;
    }

    // Fall-through switch for efficient keyword matching
    // Process longer keywords first, fall through to shorter ones
    switch (charsRead) {
      case 10:
        buf.position(offset + 10);
        if (isForUpdateWord()) {
          type = FOR_UPDATE_WORD;
          return;
        }
      // fall-through
      case 9:
        buf.position(offset + 9);
        if (isIntersectWord()) {
          type = INTERSECT_WORD;
          return;
        }
      // fall-through
      case 8:
        buf.position(offset + 8);
        if (isGroupByWord()) {
          type = GROUP_BY_WORD;
          return;
        }
        if (isOrderByWord()) {
          type = ORDER_BY_WORD;
          return;
        }
        if (isOptionWord()) {
          type = OPTION_WORD;
          buf.position(buf.position() - 2);
          return;
        }
        if (isDistinctWord()) {
          type = DISTINCT_WORD;
          return;
        }
      // fall-through
      case 7:
        buf.position(offset + 7);
      // fall-through
      case 6:
        buf.position(offset + 6);
        if (isSelectWord()) {
          type = SELECT_WORD;
          return;
        }
        if (isHavingWord()) {
          type = HAVING_WORD;
          return;
        }
        if (isExceptWord()) {
          type = EXCEPT_WORD;
          return;
        }
        if (isUpdateWord()) {
          type = UPDATE_WORD;
          return;
        }
      // fall-through
      case 5:
        buf.position(offset + 5);
        if (isWhereWord()) {
          type = WHERE_WORD;
          return;
        }
        if (isUnionWord()) {
          type = UNION_WORD;
          return;
        }
        if (isMinusWord()) {
          type = MINUS_WORD;
          return;
        }
      // fall-through
      case 4:
        buf.position(offset + 4);
        if (isFromWord()) {
          type = FROM_WORD;
          return;
        }
      // fall-through
      case 3:
        buf.position(offset + 3);
        if (isAndWord()) {
          type = AND_WORD;
          return;
        }
        if (isSetWord()) {
          type = SET_WORD;
          return;
        }
      // fall-through
      case 2:
        buf.position(offset + 2);
        if (isOrWord()) {
          type = OR_WORD;
          return;
        }
        if (isInWord()) {
          type = IN_WORD;
          return;
        }
      // fall-through
      case 1:
        buf.position(offset + 1);
        if (isWordStart(lookahead[0])) {
          handleWord();
          return;
        }
        type = OTHER;
        break;
      default:
        AssertionUtil.assertUnreachable();
    }
  }

  /**
   * Handles non-word tokens including comments, operators, and punctuation.
   *
   * <p>This method processes:
   *
   * <ul>
   *   <li>Block comments (/&#42; &#42;/) and their special variants (directives, bind variables)
   *   <li>Line comments (--)
   *   <li>End-of-line sequences (\r\n)
   *   <li>Single character tokens (parentheses, semicolons, whitespace)
   * </ul>
   *
   * @param offset the starting position in the buffer
   * @param charsRead number of characters read into lookahead buffer (max 2)
   */
  private void peekNonWord(int offset, int charsRead) {
    // Fall-through switch for efficient multi-character token detection
    switch (charsRead) {
      case 2:
        buf.position(offset + 2);
        if (isBlockCommentStart()) {
          handleBlockComment();
          return;
        }
        if (isLineCommentStart()) {
          handleLineComment();
          return;
        }
        if (isEol()) {
          type = EOL;
          currentLineNumber++;
          return;
        }
      // fall-through
      case 1:
        buf.position(offset + 1);
        char c = lookahead[0];
        if (isWhitespace(c)) {
          type = WHITESPACE;
          return;
        }
        if (c == '(') {
          type = OPENED_PARENS;
          return;
        }
        if (c == ')') {
          type = CLOSED_PARENS;
          return;
        }
        if (c == ';') {
          type = DELIMITER;
          return;
        }
        if (c == '\r' || c == '\n') {
          type = EOL;
          currentLineNumber++;
          return;
        }
        type = OTHER;
        break;
      default:
        AssertionUtil.assertUnreachable();
    }
  }

  private boolean isForUpdateWord() {
    return ((lookahead[0] | 0x20) == 'f')
        && ((lookahead[1] | 0x20) == 'o')
        && ((lookahead[2] | 0x20) == 'r')
        && (isWhitespace(lookahead[3]))
        && ((lookahead[4] | 0x20) == 'u')
        && ((lookahead[5] | 0x20) == 'p')
        && ((lookahead[6] | 0x20) == 'd')
        && ((lookahead[7] | 0x20) == 'a')
        && ((lookahead[8] | 0x20) == 't')
        && ((lookahead[9] | 0x20) == 'e')
        && isWordTerminated();
  }

  private boolean isIntersectWord() {
    return ((lookahead[0] | 0x20) == 'i')
        && ((lookahead[1] | 0x20) == 'n')
        && ((lookahead[2] | 0x20) == 't')
        && ((lookahead[3] | 0x20) == 'e')
        && ((lookahead[4] | 0x20) == 'r')
        && ((lookahead[5] | 0x20) == 's')
        && ((lookahead[6] | 0x20) == 'e')
        && ((lookahead[7] | 0x20) == 'c')
        && ((lookahead[8] | 0x20) == 't')
        && isWordTerminated();
  }

  private boolean isGroupByWord() {
    return ((lookahead[0] | 0x20) == 'g')
        && ((lookahead[1] | 0x20) == 'r')
        && ((lookahead[2] | 0x20) == 'o')
        && ((lookahead[3] | 0x20) == 'u')
        && ((lookahead[4] | 0x20) == 'p')
        && (isWhitespace(lookahead[5]))
        && ((lookahead[6] | 0x20) == 'b')
        && ((lookahead[7] | 0x20) == 'y')
        && isWordTerminated();
  }

  private boolean isOrderByWord() {
    return ((lookahead[0] | 0x20) == 'o')
        && ((lookahead[1] | 0x20) == 'r')
        && ((lookahead[2] | 0x20) == 'd')
        && ((lookahead[3] | 0x20) == 'e')
        && ((lookahead[4] | 0x20) == 'r')
        && (Character.isWhitespace(lookahead[5]))
        && ((lookahead[6] | 0x20) == 'b')
        && ((lookahead[7] | 0x20) == 'y')
        && isWordTerminated();
  }

  private boolean isOptionWord() {
    return ((lookahead[0] | 0x20) == 'o')
        && ((lookahead[1] | 0x20) == 'p')
        && ((lookahead[2] | 0x20) == 't')
        && ((lookahead[3] | 0x20) == 'i')
        && ((lookahead[4] | 0x20) == 'o')
        && ((lookahead[5] | 0x20) == 'n')
        && (isWhitespace(lookahead[6]))
        && (lookahead[7] == '(');
  }

  private boolean isDistinctWord() {
    return ((lookahead[0] | 0x20) == 'd')
        && ((lookahead[1] | 0x20) == 'i')
        && ((lookahead[2] | 0x20) == 's')
        && ((lookahead[3] | 0x20) == 't')
        && ((lookahead[4] | 0x20) == 'i')
        && ((lookahead[5] | 0x20) == 'n')
        && ((lookahead[6] | 0x20) == 'c')
        && ((lookahead[7] | 0x20) == 't')
        && isWordTerminated();
  }

  private boolean isSelectWord() {
    return ((lookahead[0] | 0x20) == 's')
        && ((lookahead[1] | 0x20) == 'e')
        && ((lookahead[2] | 0x20) == 'l')
        && ((lookahead[3] | 0x20) == 'e')
        && ((lookahead[4] | 0x20) == 'c')
        && ((lookahead[5] | 0x20) == 't')
        && isWordTerminated();
  }

  private boolean isHavingWord() {
    return ((lookahead[0] | 0x20) == 'h')
        && ((lookahead[1] | 0x20) == 'a')
        && ((lookahead[2] | 0x20) == 'v')
        && ((lookahead[3] | 0x20) == 'i')
        && ((lookahead[4] | 0x20) == 'n')
        && ((lookahead[5] | 0x20) == 'g')
        && isWordTerminated();
  }

  private boolean isExceptWord() {
    return ((lookahead[0] | 0x20) == 'e')
        && ((lookahead[1] | 0x20) == 'x')
        && ((lookahead[2] | 0x20) == 'c')
        && ((lookahead[3] | 0x20) == 'e')
        && ((lookahead[4] | 0x20) == 'p')
        && ((lookahead[5] | 0x20) == 't')
        && isWordTerminated();
  }

  private boolean isUpdateWord() {
    return ((lookahead[0] | 0x20) == 'u')
        && ((lookahead[1] | 0x20) == 'p')
        && ((lookahead[2] | 0x20) == 'd')
        && ((lookahead[3] | 0x20) == 'a')
        && ((lookahead[4] | 0x20) == 't')
        && ((lookahead[5] | 0x20) == 'e')
        && isWordTerminated();
  }

  private boolean isWhereWord() {
    return ((lookahead[0] | 0x20) == 'w')
        && ((lookahead[1] | 0x20) == 'h')
        && ((lookahead[2] | 0x20) == 'e')
        && ((lookahead[3] | 0x20) == 'r')
        && ((lookahead[4] | 0x20) == 'e')
        && isWordTerminated();
  }

  private boolean isUnionWord() {
    return ((lookahead[0] | 0x20) == 'u')
        && ((lookahead[1] | 0x20) == 'n')
        && ((lookahead[2] | 0x20) == 'i')
        && ((lookahead[3] | 0x20) == 'o')
        && ((lookahead[4] | 0x20) == 'n')
        && isWordTerminated();
  }

  private boolean isMinusWord() {
    return ((lookahead[0] | 0x20) == 'm')
        && ((lookahead[1] | 0x20) == 'i')
        && ((lookahead[2] | 0x20) == 'n')
        && ((lookahead[3] | 0x20) == 'u')
        && ((lookahead[4] | 0x20) == 's')
        && isWordTerminated();
  }

  private boolean isFromWord() {
    return ((lookahead[0] | 0x20) == 'f')
        && ((lookahead[1] | 0x20) == 'r')
        && ((lookahead[2] | 0x20) == 'o')
        && ((lookahead[3] | 0x20) == 'm')
        && isWordTerminated();
  }

  private boolean isAndWord() {
    return ((lookahead[0] | 0x20) == 'a')
        && ((lookahead[1] | 0x20) == 'n')
        && ((lookahead[2] | 0x20) == 'd')
        && isWordTerminated();
  }

  private boolean isSetWord() {
    return ((lookahead[0] | 0x20) == 's')
        && ((lookahead[1] | 0x20) == 'e')
        && ((lookahead[2] | 0x20) == 't')
        && isWordTerminated();
  }

  private boolean isOrWord() {
    return ((lookahead[0] | 0x20) == 'o') && ((lookahead[1] | 0x20) == 'r') && isWordTerminated();
  }

  private boolean isInWord() {
    return ((lookahead[0] | 0x20) == 'i') && ((lookahead[1] | 0x20) == 'n') && isWordTerminated();
  }

  private boolean isBlockCommentStart() {
    return lookahead[0] == '/' && lookahead[1] == '*';
  }

  private boolean isLineCommentStart() {
    return lookahead[0] == '-' && lookahead[1] == '-';
  }

  private boolean isEol() {
    return lookahead[0] == '\r' && lookahead[1] == '\n';
  }

  /**
   * Processes block comments and determines their specific type.
   *
   * <p>Block comments in Doma SQL have several variants:
   *
   * <ul>
   *   <li>Standard comments: /&#42;&#42; comment &#42;/
   *   <li>Bind variables: /&#42;paramName&#42;/defaultValue
   *   <li>Literal variables: /&#42;^paramName&#42;/'defaultValue'
   *   <li>Embedded variables: /&#42;#paramName&#42;/
   *   <li>Directives: /&#42;%if&#42;/, /&#42;%expand&#42;/, etc.
   * </ul>
   *
   * <p>The method examines the first character after /&#42; to determine the variant.
   */
  private void handleBlockComment() {
    type = BLOCK_COMMENT;
    if (buf.hasRemaining()) {
      char c1 = buf.get();
      if (ExpressionUtil.isExpressionIdentifierStart(c1)) {
        type = BIND_VARIABLE_BLOCK_COMMENT;
      } else if (c1 == '^') {
        type = LITERAL_VARIABLE_BLOCK_COMMENT;
      } else if (c1 == '#') {
        type = EMBEDDED_VARIABLE_BLOCK_COMMENT;
      } else if (c1 == '%') {
        parsePercentageDirective();
      } else {
        buf.position(buf.position() - 1);
      }
    }
    consumeBlockCommentContent();
  }

  /**
   * Parses directive tokens that begin with /&#42;%.
   *
   * <p>Doma SQL supports several directives for conditional logic and dynamic SQL:
   *
   * <ul>
   *   <li>/&#42;%if expression&#42;/: Conditional inclusion
   *   <li>/&#42;%elseif expression&#42;/: Alternative conditional
   *   <li>/&#42;%else&#42;/: Default branch
   *   <li>/&#42;%end&#42;/: Closes conditional block
   *   <li>/&#42;%for item : items&#42;/: Iteration over collections
   *   <li>/&#42;%expand alias&#42;/: Column expansion
   *   <li>/&#42;%populate&#42;/: Batch insert value expansion
   *   <li>/&#42;%!metadata&#42;/: Parser-level metadata (internal use)
   * </ul>
   *
   * <p>This method uses a switch on the first character after % for efficient directive
   * identification, then performs exact matching for validation. Invalid directives result in
   * DOMA2119 exception.
   *
   * <p>Implementation note: The method carefully manages buffer position to enable proper
   * backtracking when partial matches fail (e.g., "e" could be "end", "else", "elseif", or
   * "expand").
   */
  private void parsePercentageDirective() {
    if (!buf.hasRemaining()) {
      throwInvalidPercentageDirectiveException();
    }

    int charsRead;
    int offset = buf.position();
    char c = buf.get();
    buf.position(offset);

    switch (c) {
      case '!':
        // Parser-level directive: /*%!
        buf.position(offset + 1);
        type = PARSER_LEVEL_BLOCK_COMMENT;
        return;
      case 'i':
        // Check for "if"
        charsRead = Math.min(2, buf.remaining());
        if (charsRead == 2) {
          buf.get(lookahead, 0, charsRead);
          if (isIfWord()) {
            type = IF_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        break;
      case 'f':
        // Check for "for"
        charsRead = Math.min(3, buf.remaining());
        if (charsRead == 3) {
          buf.get(lookahead, 0, charsRead);
          if (isForWord()) {
            type = FOR_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        break;
      case 'e':
        // Complex case: could be "end", "else", "elseif", or "expand"
        // Check shortest match first: "end" (3 chars)
        charsRead = Math.min(3, buf.remaining());
        if (charsRead == 3) {
          buf.get(lookahead, 0, charsRead);
          if (isEndWord()) {
            type = END_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        // Check "else" (4 chars)
        charsRead = Math.min(4, buf.remaining());
        if (charsRead == 4) {
          buf.get(lookahead, 0, charsRead);
          if (isElseWord()) {
            type = ELSE_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        // Check "expand" and "elseif" (6 chars)
        charsRead = Math.min(6, buf.remaining());
        if (charsRead == 6) {
          buf.get(lookahead, 0, charsRead);
          if (isExpandWord()) {
            type = EXPAND_BLOCK_COMMENT;
            return;
          }
          if (isElseifWord()) {
            type = ELSEIF_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        break;
      case 'p':
        // Check for "populate"
        charsRead = Math.min(8, buf.remaining());
        if (charsRead == 8) {
          buf.get(lookahead, 0, charsRead);
          if (isPopulateWord()) {
            type = POPULATE_BLOCK_COMMENT;
            return;
          }
          buf.position(offset);
        }
        break;
    }

    throwInvalidPercentageDirectiveException();
  }

  private void throwInvalidPercentageDirectiveException() {
    int pos = buf.position() - lineStartPosition;
    throw new JdbcException(Message.DOMA2119, sql, lineNumber, pos);
  }

  private boolean isPopulateWord() {
    return lookahead[0] == 'p'
        && lookahead[1] == 'o'
        && lookahead[2] == 'p'
        && lookahead[3] == 'u'
        && lookahead[4] == 'l'
        && lookahead[5] == 'a'
        && lookahead[6] == 't'
        && lookahead[7] == 'e'
        && isWordTerminated();
  }

  private boolean isExpandWord() {
    return lookahead[0] == 'e'
        && lookahead[1] == 'x'
        && lookahead[2] == 'p'
        && lookahead[3] == 'a'
        && lookahead[4] == 'n'
        && lookahead[5] == 'd'
        && isWordTerminated();
  }

  private boolean isElseifWord() {
    return lookahead[0] == 'e'
        && lookahead[1] == 'l'
        && lookahead[2] == 's'
        && lookahead[3] == 'e'
        && lookahead[4] == 'i'
        && lookahead[5] == 'f'
        && isWordTerminated();
  }

  private boolean isElseWord() {
    return lookahead[0] == 'e'
        && lookahead[1] == 'l'
        && lookahead[2] == 's'
        && lookahead[3] == 'e'
        && isWordTerminated();
  }

  private boolean isForWord() {
    return lookahead[0] == 'f' && lookahead[1] == 'o' && lookahead[2] == 'r' && isWordTerminated();
  }

  private boolean isEndWord() {
    return lookahead[0] == 'e' && lookahead[1] == 'n' && lookahead[2] == 'd' && isWordTerminated();
  }

  private boolean isIfWord() {
    return lookahead[0] == 'i' && lookahead[1] == 'f' && isWordTerminated();
  }

  private void consumeBlockCommentContent() {
    while (buf.hasRemaining()) {
      char c1 = buf.get();
      if (buf.hasRemaining()) {
        buf.mark();
        char c2 = buf.get();
        if (c1 == '*' && c2 == '/') {
          return;
        }
        if ((c1 == '\r' || c1 == '\n')) {
          currentLineNumber++;
        }
        buf.reset();
      }
    }
    int pos = buf.position() - lineStartPosition;
    throw new JdbcException(Message.DOMA2102, sql, lineNumber, pos);
  }

  private void handleLineComment() {
    type = LINE_COMMENT;
    while (buf.hasRemaining()) {
      buf.mark();
      char c1 = buf.get();
      if (c1 == '\r' || c1 == '\n') {
        buf.reset();
        return;
      }
    }
  }

  private void handleQuotedString() {
    type = QUOTE;
    if (!consumeQuotedContent()) {
      throwUnterminatedQuoteException();
    }
  }

  private boolean consumeQuotedContent() {
    while (buf.hasRemaining()) {
      char c1 = buf.get();
      if (c1 == '\'') {
        if (buf.hasRemaining()) {
          buf.mark();
          char c2 = buf.get();
          if (c2 != '\'') {
            buf.reset();
            return true; // Found closing quote
          }
          // Double quote encountered, continue
        } else {
          return true; // End of input with closing quote
        }
      }
    }
    return false; // Unterminated quote
  }

  private void handleWord() {
    type = WORD;
    while (buf.hasRemaining()) {
      buf.mark();
      char c2 = buf.get();

      if (c2 == '\'') {
        if (!consumeEmbeddedQuote()) {
          throwUnterminatedQuoteException();
        }
        return;
      }

      if (!isWordPart(c2)) {
        buf.reset();
        return;
      }
    }
  }

  private boolean consumeEmbeddedQuote() {
    while (buf.hasRemaining()) {
      char c1 = buf.get();
      if (c1 == '\'') {
        if (buf.hasRemaining()) {
          buf.mark();
          char c2 = buf.get();
          if (c2 != '\'') {
            buf.reset();
            return true; // Found closing quote
          }
          // Double quote encountered, continue
        } else {
          return true; // End of input with closing quote
        }
      }
    }
    return false; // Unterminated quote
  }

  private void throwUnterminatedQuoteException() {
    int pos = buf.position() - lineStartPosition;
    throw new JdbcException(Message.DOMA2101, sql, lineNumber, pos);
  }

  private boolean isWordStart(char c) {
    if (c == '+' || c == '-') {
      buf.mark();
      if (buf.hasRemaining()) {
        char c2 = buf.get();
        buf.reset();
        if (Character.isDigit(c2)) {
          return true;
        }
      }
    }
    return isWordPart(c);
  }

  private boolean isWordTerminated() {
    buf.mark();
    if (buf.hasRemaining()) {
      char c = buf.get();
      buf.reset();
      return !isWordPart(c);
    } else {
      return true;
    }
  }

  private boolean isWordPart(char c) {
    return SqlTokenUtil.isWordPart(c);
  }

  private boolean isWhitespace(char c) {
    return SqlTokenUtil.isWhitespace(c);
  }
}
