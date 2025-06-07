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

  protected final CharBuffer duplicatedBuf;

  protected char[] lookahead = new char[10];

  protected SqlTokenType type;

  protected String token;

  protected int currentLineNumber;

  protected int lineNumber;

  protected int lineStartPosition;

  protected int position;

  public SqlTokenizer(String sql) {
    assertNotNull(sql);
    this.sql = sql;
    this.buf = CharBuffer.wrap(sql);
    duplicatedBuf = buf.duplicate();
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
    duplicatedBuf.limit(buf.position());
    token = duplicatedBuf.toString();
    duplicatedBuf.position(buf.position());
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

  protected void peek() {
    if (!buf.hasRemaining()) {
      type = EOF;
      return;
    }

    int charsRead = Math.min(10, buf.remaining());
    buf.get(lookahead, 0, charsRead);

    switch (charsRead) {
      case 1 -> peekOneChar();
      case 2 -> peekTwoChars();
      case 3 -> peekThreeChars();
      case 4 -> peekFourChars();
      case 5 -> peekFiveChars();
      case 6 -> peekSixChars();
      case 7 -> peekSevenChars();
      case 8 -> peekEightChars();
      case 9 -> peekNineChars();
      case 10 -> peekTenChars();
      default -> throw new RuntimeException(); // TODO
    }
  }

  protected void peekTenChars() {
    if ((lookahead[0] == 'f' || lookahead[0] == 'F')
        && (lookahead[1] == 'o' || lookahead[1] == 'O')
        && (lookahead[2] == 'r' || lookahead[2] == 'R')
        && (isWhitespace(lookahead[3]))
        && (lookahead[4] == 'u' || lookahead[4] == 'U')
        && (lookahead[5] == 'p' || lookahead[5] == 'P')
        && (lookahead[6] == 'd' || lookahead[6] == 'D')
        && (lookahead[7] == 'a' || lookahead[7] == 'A')
        && (lookahead[8] == 't' || lookahead[8] == 'T')
        && (lookahead[9] == 'e' || lookahead[9] == 'E')) {
      type = FOR_UPDATE_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekNineChars();
  }

  protected void peekNineChars() {
    if ((lookahead[0] == 'i' || lookahead[0] == 'I')
        && (lookahead[1] == 'n' || lookahead[1] == 'N')
        && (lookahead[2] == 't' || lookahead[2] == 'T')
        && ((lookahead[3] == 'e' || lookahead[3] == 'E'))
        && (lookahead[4] == 'r' || lookahead[4] == 'R')
        && (lookahead[5] == 's' || lookahead[5] == 'S')
        && (lookahead[6] == 'e' || lookahead[6] == 'E')
        && (lookahead[7] == 'c' || lookahead[7] == 'C')
        && (lookahead[8] == 't' || lookahead[8] == 'T')) {
      type = INTERSECT_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekEightChars();
  }

  protected void peekEightChars() {
    if ((lookahead[0] == 'g' || lookahead[0] == 'G')
        && (lookahead[1] == 'r' || lookahead[1] == 'R')
        && (lookahead[2] == 'o' || lookahead[2] == 'O')
        && (lookahead[3] == 'u' || lookahead[3] == 'U')
        && (lookahead[4] == 'p' || lookahead[4] == 'P')
        && (isWhitespace(lookahead[5]))
        && (lookahead[6] == 'b' || lookahead[6] == 'B')
        && (lookahead[7] == 'y' || lookahead[7] == 'Y')) {
      type = GROUP_BY_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'o' || lookahead[0] == 'O')
        && (lookahead[1] == 'r' || lookahead[1] == 'R')
        && (lookahead[2] == 'd' || lookahead[2] == 'D')
        && (lookahead[3] == 'e' || lookahead[3] == 'E')
        && (lookahead[4] == 'r' || lookahead[4] == 'R')
        && (Character.isWhitespace(lookahead[5]))
        && (lookahead[6] == 'b' || lookahead[6] == 'B')
        && (lookahead[7] == 'y' || lookahead[7] == 'Y')) {
      type = ORDER_BY_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'o' || lookahead[0] == 'O')
        && (lookahead[1] == 'p' || lookahead[1] == 'P')
        && (lookahead[2] == 't' || lookahead[2] == 'T')
        && (lookahead[3] == 'i' || lookahead[3] == 'I')
        && (lookahead[4] == 'o' || lookahead[4] == 'O')
        && (lookahead[5] == 'n' || lookahead[5] == 'N')
        && (isWhitespace(lookahead[6]))
        && (lookahead[7] == '(')) {
      type = OPTION_WORD;
      buf.position(buf.position() - 2);
      return;
    } else if ((lookahead[0] == 'd' || lookahead[0] == 'D')
        && (lookahead[1] == 'i' || lookahead[1] == 'I')
        && (lookahead[2] == 's' || lookahead[2] == 'S')
        && (lookahead[3] == 't' || lookahead[3] == 'T')
        && (lookahead[4] == 'i' || lookahead[4] == 'I')
        && (lookahead[5] == 'n' || lookahead[5] == 'N')
        && (lookahead[6] == 'c' || lookahead[6] == 'C')
        && (lookahead[7] == 't' || lookahead[7] == 'T')) {
      type = DISTINCT_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekSevenChars();
  }

  protected void peekSevenChars() {
    buf.position(buf.position() - 1);
    peekSixChars();
  }

  protected void peekSixChars() {
    if ((lookahead[0] == 's' || lookahead[0] == 'S')
        && (lookahead[1] == 'e' || lookahead[1] == 'E')
        && (lookahead[2] == 'l' || lookahead[2] == 'L')
        && (lookahead[3] == 'e' || lookahead[3] == 'E')
        && (lookahead[4] == 'c' || lookahead[4] == 'C')
        && (lookahead[5] == 't' || lookahead[5] == 'T')) {
      type = SELECT_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'h' || lookahead[0] == 'H')
        && (lookahead[1] == 'a' || lookahead[1] == 'A')
        && (lookahead[2] == 'v' || lookahead[2] == 'V')
        && (lookahead[3] == 'i' || lookahead[3] == 'I')
        && (lookahead[4] == 'n' || lookahead[4] == 'N')
        && (lookahead[5] == 'g' || lookahead[5] == 'G')) {
      type = HAVING_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'e' || lookahead[0] == 'E')
        && (lookahead[1] == 'x' || lookahead[1] == 'X')
        && (lookahead[2] == 'c' || lookahead[2] == 'C')
        && (lookahead[3] == 'e' || lookahead[3] == 'E')
        && (lookahead[4] == 'p' || lookahead[4] == 'P')
        && (lookahead[5] == 't' || lookahead[5] == 'T')) {
      type = EXCEPT_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'u' || lookahead[0] == 'U')
        && (lookahead[1] == 'p' || lookahead[1] == 'P')
        && (lookahead[2] == 'd' || lookahead[2] == 'D')
        && (lookahead[3] == 'a' || lookahead[3] == 'A')
        && (lookahead[4] == 't' || lookahead[4] == 'T')
        && (lookahead[5] == 'e' || lookahead[5] == 'E')) {
      type = UPDATE_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekFiveChars();
  }

  protected void peekFiveChars() {
    if ((lookahead[0] == 'w' || lookahead[0] == 'W')
        && (lookahead[1] == 'h' || lookahead[1] == 'H')
        && (lookahead[2] == 'e' || lookahead[2] == 'E')
        && (lookahead[3] == 'r' || lookahead[3] == 'R')
        && (lookahead[4] == 'e' || lookahead[4] == 'E')) {
      type = WHERE_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'u' || lookahead[0] == 'U')
        && (lookahead[1] == 'n' || lookahead[1] == 'N')
        && (lookahead[2] == 'i' || lookahead[2] == 'I')
        && (lookahead[3] == 'o' || lookahead[3] == 'O')
        && (lookahead[4] == 'n' || lookahead[4] == 'N')) {
      type = UNION_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'm' || lookahead[0] == 'M')
        && (lookahead[1] == 'i' || lookahead[1] == 'I')
        && (lookahead[2] == 'n' || lookahead[2] == 'N')
        && (lookahead[3] == 'u' || lookahead[3] == 'U')
        && (lookahead[4] == 's' || lookahead[4] == 'S')) {
      type = MINUS_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekFourChars();
  }

  protected void peekFourChars() {
    if ((lookahead[0] == 'f' || lookahead[0] == 'F')
        && (lookahead[1] == 'r' || lookahead[1] == 'R')
        && (lookahead[2] == 'o' || lookahead[2] == 'O')
        && (lookahead[3] == 'm' || lookahead[3] == 'M')) {
      type = FROM_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekThreeChars();
  }

  protected void peekThreeChars() {
    if ((lookahead[0] == 'a' || lookahead[0] == 'A')
        && (lookahead[1] == 'n' || lookahead[1] == 'N')
        && (lookahead[2] == 'd' || lookahead[2] == 'D')) {
      type = AND_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 's' || lookahead[0] == 'S')
        && (lookahead[1] == 'e' || lookahead[1] == 'E')
        && (lookahead[2] == 't' || lookahead[2] == 'T')) {
      type = SET_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekTwoChars();
  }

  protected void peekTwoChars() {
    if ((lookahead[0] == 'o' || lookahead[0] == 'O')
        && (lookahead[1] == 'r' || lookahead[1] == 'R')) {
      type = OR_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((lookahead[0] == 'i' || lookahead[0] == 'I')
        && (lookahead[1] == 'n' || lookahead[1] == 'N')) {
      type = IN_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if (lookahead[0] == '/' && lookahead[1] == '*') {
      type = BLOCK_COMMENT;
      if (buf.hasRemaining()) {
        char c3 = buf.get();
        if (ExpressionUtil.isExpressionIdentifierStart(c3)) {
          type = BIND_VARIABLE_BLOCK_COMMENT;
        } else if (c3 == '^') {
          type = LITERAL_VARIABLE_BLOCK_COMMENT;
        } else if (c3 == '#') {
          type = EMBEDDED_VARIABLE_BLOCK_COMMENT;
        } else if (c3 == '%') {
          if (buf.hasRemaining()) {
            char c4 = buf.get();
            if (c4 == '!') {
              type = PARSER_LEVEL_BLOCK_COMMENT;
            } else if (buf.hasRemaining()) {
              char c5 = buf.get();
              if (c4 == 'i' && c5 == 'f') {
                if (isBlockCommentDirectiveTerminated()) {
                  type = IF_BLOCK_COMMENT;
                }
              } else if (buf.hasRemaining()) {
                char c6 = buf.get();
                if (c4 == 'f' && c5 == 'o' && c6 == 'r') {
                  if (isBlockCommentDirectiveTerminated()) {
                    type = FOR_BLOCK_COMMENT;
                  }
                } else if (c4 == 'e' && c5 == 'n' && c6 == 'd') {
                  if (isBlockCommentDirectiveTerminated()) {
                    type = END_BLOCK_COMMENT;
                  }
                } else if (buf.hasRemaining()) {
                  char c7 = buf.get();
                  if (c4 == 'e' && c5 == 'l' && c6 == 's' && c7 == 'e') {
                    if (isBlockCommentDirectiveTerminated()) {
                      type = ELSE_BLOCK_COMMENT;
                    } else {
                      if (buf.hasRemaining()) {
                        char c8 = buf.get();
                        if (buf.hasRemaining()) {
                          char c9 = buf.get();
                          if (c8 == 'i' && c9 == 'f') {
                            if (isBlockCommentDirectiveTerminated()) {
                              type = ELSEIF_BLOCK_COMMENT;
                            }
                          } else {
                            buf.position(buf.position() - 6);
                          }
                        } else {
                          buf.position(buf.position() - 5);
                        }
                      }
                    }
                  } else if (buf.hasRemaining()) {
                    char c8 = buf.get();
                    if (buf.hasRemaining()) {
                      char c9 = buf.get();
                      if (c4 == 'e' && c5 == 'x' && c6 == 'p' && c7 == 'a' && c8 == 'n'
                          && c9 == 'd') {
                        if (isBlockCommentDirectiveTerminated()) {
                          type = EXPAND_BLOCK_COMMENT;
                        }
                      } else if (buf.hasRemaining()) {
                        char c10 = buf.get();
                        if (buf.hasRemaining()) {
                          char c11 = buf.get();
                          if (c4 == 'p'
                              && c5 == 'o'
                              && c6 == 'p'
                              && c7 == 'u'
                              && c8 == 'l'
                              && c9 == 'a'
                              && c10 == 't'
                              && c11 == 'e') {
                            if (isBlockCommentDirectiveTerminated()) {
                              type = POPULATE_BLOCK_COMMENT;
                            }
                          } else {
                            buf.position(buf.position() - 8);
                          }
                        } else {
                          buf.position(buf.position() - 7);
                        }
                      } else {
                        buf.position(buf.position() - 6);
                      }
                    } else {
                      buf.position(buf.position() - 5);
                    }
                  } else {
                    buf.position(buf.position() - 4);
                  }
                } else {
                  buf.position(buf.position() - 3);
                }
              } else {
                buf.position(buf.position() - 2);
              }
            } else {
              buf.position(buf.position() - 1);
            }
          }
          if (type != PARSER_LEVEL_BLOCK_COMMENT
              && type != IF_BLOCK_COMMENT
              && type != FOR_BLOCK_COMMENT
              && type != END_BLOCK_COMMENT
              && type != ELSE_BLOCK_COMMENT
              && type != ELSEIF_BLOCK_COMMENT
              && type != EXPAND_BLOCK_COMMENT
              && type != POPULATE_BLOCK_COMMENT) {
            int pos = buf.position() - lineStartPosition;
            throw new JdbcException(Message.DOMA2119, sql, lineNumber, pos);
          }
        }
        buf.position(buf.position() - 1);
      }
      while (buf.hasRemaining()) {
        char c3 = buf.get();
        if (buf.hasRemaining()) {
          buf.mark();
          char c4 = buf.get();
          if (c3 == '*' && c4 == '/') {
            return;
          }
          if ((c3 == '\r' || c3 == '\n')) {
            currentLineNumber++;
          }
          buf.reset();
        }
      }
      int pos = buf.position() - lineStartPosition;
      throw new JdbcException(Message.DOMA2102, sql, lineNumber, pos);
    } else if (lookahead[0] == '-' && lookahead[1] == '-') {
      type = LINE_COMMENT;
      while (buf.hasRemaining()) {
        buf.mark();
        char c3 = buf.get();
        if (c3 == '\r' || c3 == '\n') {
          buf.reset();
          return;
        }
      }
      return;
    } else if (lookahead[0] == '\r' && lookahead[1] == '\n') {
      type = EOL;
      currentLineNumber++;
      return;
    }
    buf.position(buf.position() - 1);
    peekOneChar();
  }

  protected void peekOneChar() {
    if (isWhitespace(lookahead[0])) {
      type = WHITESPACE;
    } else if (lookahead[0] == '(') {
      type = OPENED_PARENS;
    } else if (lookahead[0] == ')') {
      type = CLOSED_PARENS;
    } else if (lookahead[0] == ';') {
      type = DELIMITER;
    } else if (lookahead[0] == '\'') {
      type = QUOTE;
      boolean closed = false;
      while (buf.hasRemaining()) {
        char c2 = buf.get();
        if (c2 == '\'') {
          if (buf.hasRemaining()) {
            buf.mark();
            char c3 = buf.get();
            if (c3 != '\'') {
              buf.reset();
              closed = true;
              break;
            }
          } else {
            closed = true;
          }
        }
      }
      if (closed) {
        return;
      }
      int pos = buf.position() - lineStartPosition;
      throw new JdbcException(Message.DOMA2101, sql, lineNumber, pos);
    } else if (isWordStart(lookahead[0])) {
      type = WORD;
      while (buf.hasRemaining()) {
        buf.mark();
        char c2 = buf.get();
        if (c2 == '\'') {
          boolean closed = false;
          while (buf.hasRemaining()) {
            char c3 = buf.get();
            if (c3 == '\'') {
              if (buf.hasRemaining()) {
                buf.mark();
                char c4 = buf.get();
                if (c4 != '\'') {
                  buf.reset();
                  closed = true;
                  break;
                }
              } else {
                closed = true;
              }
            }
          }
          if (closed) {
            return;
          }
          int pos = buf.position() - lineStartPosition;
          throw new JdbcException(Message.DOMA2101, sql, lineNumber, pos);
        }
        if (!isWordPart(c2)) {
          buf.reset();
          return;
        }
      }
    } else if (lookahead[0] == '\r' || lookahead[0] == '\n') {
      type = EOL;
      currentLineNumber++;
    } else {
      type = OTHER;
    }
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

  protected boolean isBlockCommentDirectiveTerminated() {
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
