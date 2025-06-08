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
import org.seasar.doma.internal.util.SqlTokenUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

public class SqlTokenizer {

  protected final String sql;

  protected final CharBuffer buf;

  protected char[] lookahead = new char[10];

  protected SqlTokenType type;

  protected String token;

  protected int currentLineNumber;

  protected int lineNumber;

  protected int lineStartPosition;

  protected int position;

  private int tokenStartIndex;

  public SqlTokenizer(String sql) {
    assertNotNull(sql);
    this.sql = sql;
    this.buf = CharBuffer.wrap(sql);
    currentLineNumber = 1;
    lineNumber = 1;
    peek();
  }

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

  protected void prepareToken() {
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

  private void decrementPosition() {
    buf.position(buf.position() - 1);
  }

  protected void peek() {
    int charsRead = Math.min(lookahead.length, buf.remaining());
    if (charsRead == 0) {
      type = EOF;
      return;
    }

    buf.get(lookahead, 0, charsRead);

    boolean isWordStart = isWordStart(lookahead[0]);
    if (!isWordStart && charsRead > 2) {
      buf.position(buf.position() - (charsRead - 2));
      charsRead = 2;
    }

    // This switch statement takes advantage of fall-through behavior.
    switch (charsRead) {
      case 10:
        if (isForUpdateWord()) {
          type = FOR_UPDATE_WORD;
          return;
        }
        decrementPosition();
      case 9:
        if (isIntersectWord()) {
          type = INTERSECT_WORD;
          return;
        }
        decrementPosition();
      case 8:
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
        decrementPosition();
      case 7:
        decrementPosition();
      case 6:
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
        decrementPosition();
      case 5:
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
        decrementPosition();
      case 4:
        if (isFromWord()) {
          type = FROM_WORD;
          return;
        }
        decrementPosition();
      case 3:
        if (isAndWord()) {
          type = AND_WORD;
          return;
        }
        if (isSetWord()) {
          type = SET_WORD;
          return;
        }
        decrementPosition();
      case 2:
        if (isWordStart && isOrWord()) {
          type = OR_WORD;
          return;
        }
        if (isWordStart && isInWord()) {
          type = IN_WORD;
          return;
        }
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
        decrementPosition();
      case 1:
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
        if (c == '\'') {
          handleQuotedString();
          return;
        }
        if (isWordStart(c)) {
          handleWord();
          return;
        }
        if (c == '\r' || c == '\n') {
          type = EOL;
          currentLineNumber++;
          return;
        }
        type = OTHER;
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
        decrementPosition();
      }
    }
    consumeBlockCommentContent();
  }

  private void parsePercentageDirective() {
    int charsRead = Math.min(8, buf.remaining());
    if (charsRead == 0) {
      throwInvalidPercentageDirectiveException();
    }

    int preservedPosition = buf.position();
    buf.get(lookahead, 0, charsRead);

    switch (lookahead[0]) {
      case '!':
        if (charsRead >= 1) {
          buf.position(preservedPosition + 1);
          type = PARSER_LEVEL_BLOCK_COMMENT;
          return;
        }
        break;
      case 'i':
        if (charsRead >= 2) {
          buf.position(preservedPosition + 2);
          if (isIfWord()) {
            type = IF_BLOCK_COMMENT;
            return;
          }
        }
        break;
      case 'f':
        if (charsRead >= 3) {
          buf.position(preservedPosition + 3);
          if (isForWord()) {
            type = FOR_BLOCK_COMMENT;
            return;
          }
        }
        break;
      case 'e':
        if (charsRead >= 6) {
          buf.position(preservedPosition + 6);
          if (isExpandWord()) {
            type = EXPAND_BLOCK_COMMENT;
            return;
          }
          if (isElseifWord()) {
            type = ELSEIF_BLOCK_COMMENT;
            return;
          }
        }
        if (charsRead >= 4) {
          buf.position(preservedPosition + 4);
          if (isElseWord()) {
            type = ELSE_BLOCK_COMMENT;
            return;
          }
        }
        if (charsRead >= 3) {
          buf.position(preservedPosition + 3);
          if (isEndWord()) {
            type = END_BLOCK_COMMENT;
            return;
          }
        }
        break;
      case 'p':
        if (charsRead == 8) {
          if (isPopulateWord()) {
            type = POPULATE_BLOCK_COMMENT;
            return;
          }
        }
        break;
    }

    buf.position(preservedPosition);
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

  protected boolean isWordStart(char c) {
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

  protected boolean isWordTerminated() {
    buf.mark();
    if (buf.hasRemaining()) {
      char c = buf.get();
      buf.reset();
      return !isWordPart(c);
    } else {
      return true;
    }
  }

  protected boolean isWordPart(char c) {
    return SqlTokenUtil.isWordPart(c);
  }

  protected boolean isWhitespace(char c) {
    return SqlTokenUtil.isWhitespace(c);
  }
}
