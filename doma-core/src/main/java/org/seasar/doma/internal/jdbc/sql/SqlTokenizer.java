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
    if (buf.hasRemaining()) {
      char c = buf.get();
      if (buf.hasRemaining()) {
        char c2 = buf.get();
        if (buf.hasRemaining()) {
          char c3 = buf.get();
          if (buf.hasRemaining()) {
            char c4 = buf.get();
            if (buf.hasRemaining()) {
              char c5 = buf.get();
              if (buf.hasRemaining()) {
                char c6 = buf.get();
                if (buf.hasRemaining()) {
                  char c7 = buf.get();
                  if (buf.hasRemaining()) {
                    char c8 = buf.get();
                    if (buf.hasRemaining()) {
                      char c9 = buf.get();
                      if (buf.hasRemaining()) {
                        char c10 = buf.get();
                        peekTenChars(c, c2, c3, c4, c5, c6, c7, c8, c9, c10);
                      } else {
                        peekNineChars(c, c2, c3, c4, c5, c6, c7, c8, c9);
                      }
                    } else {
                      peekEightChars(c, c2, c3, c4, c5, c6, c7, c8);
                    }
                  } else {
                    peekSevenChars(c, c2, c3, c4, c5, c6, c7);
                  }
                } else {
                  peekSixChars(c, c2, c3, c4, c5, c6);
                }
              } else {
                peekFiveChars(c, c2, c3, c4, c5);
              }
            } else {
              peekFourChars(c, c2, c3, c4);
            }
          } else {
            peekThreeChars(c, c2, c3);
          }
        } else {
          peekTwoChars(c, c2);
        }
      } else {
        peekOneChar(c);
      }
    } else {
      type = EOF;
    }
  }

  protected void peekTenChars(
      char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9, char c10) {
    if ((c == 'f' || c == 'F')
        && (c2 == 'o' || c2 == 'O')
        && (c3 == 'r' || c3 == 'R')
        && (isWhitespace(c4))
        && (c5 == 'u' || c5 == 'U')
        && (c6 == 'p' || c6 == 'P')
        && (c7 == 'd' || c7 == 'D')
        && (c8 == 'a' || c8 == 'A')
        && (c9 == 't' || c9 == 'T')
        && (c10 == 'e' || c10 == 'E')) {
      type = FOR_UPDATE_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekNineChars(c, c2, c3, c4, c5, c6, c7, c8, c9);
  }

  protected void peekNineChars(
      char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9) {
    if ((c == 'i' || c == 'I')
        && (c2 == 'n' || c2 == 'N')
        && (c3 == 't' || c3 == 'T')
        && ((c4 == 'e' || c4 == 'E'))
        && (c5 == 'r' || c5 == 'R')
        && (c6 == 's' || c6 == 'S')
        && (c7 == 'e' || c7 == 'E')
        && (c8 == 'c' || c8 == 'C')
        && (c9 == 't' || c9 == 'T')) {
      type = INTERSECT_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekEightChars(c, c2, c3, c4, c5, c6, c7, c8);
  }

  protected void peekEightChars(
      char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
    if ((c == 'g' || c == 'G')
        && (c2 == 'r' || c2 == 'R')
        && (c3 == 'o' || c3 == 'O')
        && (c4 == 'u' || c4 == 'U')
        && (c5 == 'p' || c5 == 'P')
        && (isWhitespace(c6))
        && (c7 == 'b' || c7 == 'B')
        && (c8 == 'y' || c8 == 'Y')) {
      type = GROUP_BY_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'o' || c == 'O')
        && (c2 == 'r' || c2 == 'R')
        && (c3 == 'd' || c3 == 'D')
        && (c4 == 'e' || c4 == 'E')
        && (c5 == 'r' || c5 == 'R')
        && (Character.isWhitespace(c6))
        && (c7 == 'b' || c7 == 'B')
        && (c8 == 'y' || c8 == 'Y')) {
      type = ORDER_BY_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'o' || c == 'O')
        && (c2 == 'p' || c2 == 'P')
        && (c3 == 't' || c3 == 'T')
        && (c4 == 'i' || c4 == 'I')
        && (c5 == 'o' || c5 == 'O')
        && (c6 == 'n' || c6 == 'N')
        && (isWhitespace(c7))
        && (c8 == '(')) {
      type = OPTION_WORD;
      buf.position(buf.position() - 2);
      return;
    } else if ((c == 'd' || c == 'D')
        && (c2 == 'i' || c2 == 'I')
        && (c3 == 's' || c3 == 'S')
        && (c4 == 't' || c4 == 'T')
        && (c5 == 'i' || c5 == 'I')
        && (c6 == 'n' || c6 == 'N')
        && (c7 == 'c' || c7 == 'C')
        && (c8 == 't' || c8 == 'T')) {
      type = DISTINCT_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekSevenChars(c, c2, c3, c4, c5, c6, c7);
  }

  protected void peekSevenChars(char c, char c2, char c3, char c4, char c5, char c6, char c7) {
    buf.position(buf.position() - 1);
    peekSixChars(c, c2, c3, c4, c5, c6);
  }

  protected void peekSixChars(char c, char c2, char c3, char c4, char c5, char c6) {
    if ((c == 's' || c == 'S')
        && (c2 == 'e' || c2 == 'E')
        && (c3 == 'l' || c3 == 'L')
        && (c4 == 'e' || c4 == 'E')
        && (c5 == 'c' || c5 == 'C')
        && (c6 == 't' || c6 == 'T')) {
      type = SELECT_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'h' || c == 'H')
        && (c2 == 'a' || c2 == 'A')
        && (c3 == 'v' || c3 == 'V')
        && (c4 == 'i' || c4 == 'I')
        && (c5 == 'n' || c5 == 'N')
        && (c6 == 'g' || c6 == 'G')) {
      type = HAVING_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'e' || c == 'E')
        && (c2 == 'x' || c2 == 'X')
        && (c3 == 'c' || c3 == 'C')
        && (c4 == 'e' || c4 == 'E')
        && (c5 == 'p' || c5 == 'P')
        && (c6 == 't' || c6 == 'T')) {
      type = EXCEPT_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'u' || c == 'U')
        && (c2 == 'p' || c2 == 'P')
        && (c3 == 'd' || c3 == 'D')
        && (c4 == 'a' || c4 == 'A')
        && (c5 == 't' || c5 == 'T')
        && (c6 == 'e' || c6 == 'E')) {
      type = UPDATE_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekFiveChars(c, c2, c3, c4, c5);
  }

  protected void peekFiveChars(char c, char c2, char c3, char c4, char c5) {
    if ((c == 'w' || c == 'W')
        && (c2 == 'h' || c2 == 'H')
        && (c3 == 'e' || c3 == 'E')
        && (c4 == 'r' || c4 == 'R')
        && (c5 == 'e' || c5 == 'E')) {
      type = WHERE_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'u' || c == 'U')
        && (c2 == 'n' || c2 == 'N')
        && (c3 == 'i' || c3 == 'I')
        && (c4 == 'o' || c4 == 'O')
        && (c5 == 'n' || c5 == 'N')) {
      type = UNION_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 'm' || c == 'M')
        && (c2 == 'i' || c2 == 'I')
        && (c3 == 'n' || c3 == 'N')
        && (c4 == 'u' || c4 == 'U')
        && (c5 == 's' || c5 == 'S')) {
      type = MINUS_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekFourChars(c, c2, c3, c4);
  }

  protected void peekFourChars(char c, char c2, char c3, char c4) {
    if ((c == 'f' || c == 'F')
        && (c2 == 'r' || c2 == 'R')
        && (c3 == 'o' || c3 == 'O')
        && (c4 == 'm' || c4 == 'M')) {
      type = FROM_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekThreeChars(c, c2, c3);
  }

  protected void peekThreeChars(char c, char c2, char c3) {
    if ((c == 'a' || c == 'A') && (c2 == 'n' || c2 == 'N') && (c3 == 'd' || c3 == 'D')) {
      type = AND_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if ((c == 's' || c == 'S') && (c2 == 'e' || c2 == 'E') && (c3 == 't' || c3 == 'T')) {
      type = SET_WORD;
      if (isWordTerminated()) {
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekTwoChars(c, c2);
  }

  protected void peekTwoChars(char c, char c2) {
    if ((c == 'o' || c == 'O') && (c2 == 'r' || c2 == 'R')) {
      type = OR_WORD;
      if (isWordTerminated()) {
        return;
      }
    } else if (c == '/' && c2 == '*') {
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
    } else if (c == '-' && c2 == '-') {
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
    } else if (c == '\r' && c2 == '\n') {
      type = EOL;
      currentLineNumber++;
      return;
    }
    buf.position(buf.position() - 1);
    peekOneChar(c);
  }

  protected void peekOneChar(char c) {
    if (isWhitespace(c)) {
      type = WHITESPACE;
    } else if (c == '(') {
      type = OPENED_PARENS;
    } else if (c == ')') {
      type = CLOSED_PARENS;
    } else if (c == ';') {
      type = DELIMITER;
    } else if (c == '\'') {
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
    } else if (isWordStart(c)) {
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
    } else if (c == '\r' || c == '\n') {
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
