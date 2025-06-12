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
package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.expr.ExpressionTokenType.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.nio.CharBuffer;
import org.seasar.doma.message.Message;

public class ExpressionTokenizer {

  protected final String expression;

  protected final CharBuffer buf;

  protected ExpressionTokenType type;

  protected String token;

  protected int position;

  protected int tokenStartIndex;

  protected boolean binaryOpAvailable;

  public ExpressionTokenizer(String expression) {
    assertNotNull(expression);
    this.expression = expression;
    buf = CharBuffer.wrap(expression);
    peek();
  }

  public ExpressionTokenType next() {
    if (type == EOE) {
      token = null;
      return EOE;
    }
    ExpressionTokenType currentType = type;
    prepareToken();
    peek();
    return currentType;
  }

  protected void prepareToken() {
    position = buf.position();
    token = expression.substring(tokenStartIndex, position);
    tokenStartIndex = position;
  }

  public String getToken() {
    return token;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position, boolean binaryOpAvailable) {
    this.position = position;
    this.binaryOpAvailable = binaryOpAvailable;
    buf.position(position);
    tokenStartIndex = position;
    peek();
  }

  protected void peek() {
    // Check if we've reached the end of the expression
    if (!buf.hasRemaining()) {
      type = EOE;
      return;
    }

    // Read the first character
    char c1 = buf.get();
    if (!buf.hasRemaining()) {
      // Only one character left, process it as a single character token
      peekOneChar(c1);
      return;
    }
    // Optimization: Most identifiers don't start with 'f', 't', or 'n' (keywords false, true, null,
    // new)
    // Skip reading more characters for regular identifiers
    if (Character.isJavaIdentifierStart(c1) && c1 != 'f' && c1 != 't' && c1 != 'n') {
      handleVariable();
      return;
    }

    // Read the second character for two-character operators (&&, ||, ==, etc.)
    char c2 = buf.get();
    if (!buf.hasRemaining()) {
      // Only two characters available, check for two-character operators
      peekTwoChars(c1, c2);
      return;
    }
    // Optimization: If the first character is not an identifier start, it can be a two-character
    // operator
    if (!Character.isJavaIdentifierStart(c1)) {
      if (!Character.isJavaIdentifierStart(c1)) {
        peekTwoChars(c1, c2);
      } else {
        buf.position(buf.position() - 1);
        peekOneChar(c1);
      }
      return;
    }

    // Read the third character to check for 'new' keyword
    char c3 = buf.get();
    if (isNewOperator(c1, c2, c3)) {
      type = NEW_OPERATOR;
      return;
    }
    if (!buf.hasRemaining()) {
      // No more characters, rewind and process as regular identifier
      buf.position(buf.position() - 2);
      handleVariable();
      return;
    }

    // Read the fourth character to check for 'null' or 'true' keywords
    char c4 = buf.get();
    if (isNullLiteral(c1, c2, c3, c4)) {
      type = NULL_LITERAL;
      binaryOpAvailable = true;
      return;
    }
    if (isTrueLiteral(c1, c2, c3, c4)) {
      type = TRUE_LITERAL;
      binaryOpAvailable = true;
      return;
    }
    if (!buf.hasRemaining()) {
      // No more characters, rewind and process as regular identifier
      buf.position(buf.position() - 3);
      handleVariable();
      return;
    }

    // Read the fifth character to check for 'false' keyword
    char c5 = buf.get();
    if (isFalseLiteral(c1, c2, c3, c4, c5)) {
      type = FALSE_LITERAL;
      binaryOpAvailable = true;
      return;
    }
    // Not a keyword, rewind and process as regular identifier
    buf.position(buf.position() - 4);
    handleVariable();
  }

  private boolean isNewOperator(char c, char c2, char c3) {
    return c == 'n' && c2 == 'e' && c3 == 'w' && isWordTerminated();
  }

  private boolean isNullLiteral(char c, char c2, char c3, char c4) {
    return c == 'n' && c2 == 'u' && c3 == 'l' && c4 == 'l' && isWordTerminated();
  }

  private boolean isTrueLiteral(char c, char c2, char c3, char c4) {
    return c == 't' && c2 == 'r' && c3 == 'u' && c4 == 'e' && isWordTerminated();
  }

  private boolean isFalseLiteral(char c, char c2, char c3, char c4, char c5) {
    return c == 'f' && c2 == 'a' && c3 == 'l' && c4 == 's' && c5 == 'e' && isWordTerminated();
  }

  protected void peekTwoChars(char c, char c2) {
    if (binaryOpAvailable) {
      if (c == '&' && c2 == '&') {
        type = AND_OPERATOR;
        binaryOpAvailable = false;
        return;
      } else if (c == '|' && c2 == '|') {
        type = OR_OPERATOR;
        binaryOpAvailable = false;
        return;
      } else if (c == '=' && c2 == '=') {
        type = EQ_OPERATOR;
        binaryOpAvailable = false;
        return;
      } else if (c == '!' && c2 == '=') {
        type = NE_OPERATOR;
        binaryOpAvailable = false;
        return;
      } else if (c == '>' && c2 == '=') {
        type = GE_OPERATOR;
        binaryOpAvailable = false;
        return;
      } else if (c == '<' && c2 == '=') {
        type = LE_OPERATOR;
        binaryOpAvailable = false;
        return;
      }
    }
    buf.position(buf.position() - 1);
    peekOneChar(c);
  }

  protected void peekOneChar(char c) {
    // Handle binary operators first when available
    if (binaryOpAvailable) {
      switch (c) {
        case '>':
          type = GT_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '<':
          type = LT_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '+':
          type = ADD_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '-':
          type = SUBTRACT_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '*':
          type = MULTIPLY_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '/':
          type = DIVIDE_OPERATOR;
          binaryOpAvailable = false;
          return;
        case '%':
          type = MOD_OPERATOR;
          binaryOpAvailable = false;
          return;
      }
    }

    // Handle single character tokens with switch for efficiency
    switch (c) {
      case ' ':
      case '\t':
      case '\n':
      case '\r':
      case '\u000B': // Vertical TAB
      case '\u000C': // Form Feed
      case '\u001C': // File Separator
      case '\u001D': // Group Separator
      case '\u001E': // Record Separator
      case '\u001F': // Unit Separator
        type = WHITESPACE;
        return;
      case ',':
        type = COMMA_OPERATOR;
        return;
      case '(':
        type = OPENED_PARENS;
        return;
      case ')':
        type = CLOSED_PARENS;
        binaryOpAvailable = true;
        return;
      case '!':
        type = NOT_OPERATOR;
        return;
      case '\'':
        handleCharLiteral();
        return;
      case '"':
        handleStringLiteral();
        return;
      case '+':
      case '-':
        handleSignedNumber();
        return;
      case '.':
        handleFieldOrMethodOperator();
        return;
      case '@':
        handleFunctionOperator();
        return;
      default:
        // Check remaining character types
        if (Character.isWhitespace(c)) {
          type = WHITESPACE;
        } else if (Character.isDigit(c)) {
          peekNumber();
        } else if (Character.isJavaIdentifierStart(c)) {
          handleVariable();
        } else {
          type = OTHER;
        }
    }
  }

  private void handleCharLiteral() {
    type = CHAR_LITERAL;
    if (buf.hasRemaining()) {
      buf.get();
      if (buf.hasRemaining()) {
        char c3 = buf.get();
        if (c3 == '\'') {
          binaryOpAvailable = true;
          return;
        }
      }
    }
    throw new ExpressionException(Message.DOMA3016, expression, buf.position());
  }

  private void handleStringLiteral() {
    type = STRING_LITERAL;
    boolean closed = false;
    while (buf.hasRemaining()) {
      char c2 = buf.get();
      if (c2 == '"') {
        if (buf.hasRemaining()) {
          buf.mark();
          char c3 = buf.get();
          if (c3 != '"') {
            buf.reset();
            closed = true;
            break;
          }
        } else {
          closed = true;
        }
      }
    }
    if (!closed) {
      throw new ExpressionException(Message.DOMA3004, expression, buf.position());
    }
    binaryOpAvailable = true;
  }

  private void handleSignedNumber() {
    buf.mark();
    if (buf.hasRemaining()) {
      char c2 = buf.get();
      if (Character.isDigit(c2)) {
        peekNumber();
        return;
      }
      buf.reset();
    }
    type = ILLEGAL_NUMBER_LITERAL;
  }

  private void handleVariable() {
    type = VARIABLE;
    binaryOpAvailable = true;
    while (buf.hasRemaining()) {
      buf.mark();
      char c2 = buf.get();
      if (!Character.isJavaIdentifierPart(c2)) {
        buf.reset();
        break;
      }
    }
  }

  private void handleFieldOrMethodOperator() {
    type = FIELD_OPERATOR;
    binaryOpAvailable = true;
    if (!buf.hasRemaining()) {
      throw new ExpressionException(Message.DOMA3021, expression, buf.position());
    }
    buf.mark();
    char c2 = buf.get();
    if (Character.isJavaIdentifierStart(c2)) {
      while (buf.hasRemaining()) {
        buf.mark();
        char c3 = buf.get();
        if (!Character.isJavaIdentifierPart(c3)) {
          if (c3 == '(') {
            type = METHOD_OPERATOR;
            binaryOpAvailable = false;
          }
          buf.reset();
          return;
        }
      }
    } else {
      throw new ExpressionException(Message.DOMA3022, expression, buf.position(), c2);
    }
  }

  private void handleFunctionOperator() {
    if (!buf.hasRemaining()) {
      throw new ExpressionException(Message.DOMA3023, expression, buf.position());
    }
    buf.mark();
    char c2 = buf.get();
    if (Character.isJavaIdentifierStart(c2)) {
      while (buf.hasRemaining()) {
        buf.mark();
        char c3 = buf.get();
        if (!Character.isJavaIdentifierPart(c3)) {
          if (c3 == '(') {
            type = FUNCTION_OPERATOR;
            binaryOpAvailable = false;
            buf.reset();
            return;
          } else if (c3 == '@') {
            peekStaticMember();
            return;
          } else if (c3 == '.') {
            while (buf.hasRemaining()) {
              buf.mark();
              char c4 = buf.get();
              if (!Character.isJavaIdentifierPart(c4)) {
                if (c4 == '.') {
                  continue;
                } else if (c4 == '@') {
                  peekStaticMember();
                  return;
                }
                throw new ExpressionException(Message.DOMA3031, expression, buf.position(), c4);
              }
            }
            throw new ExpressionException(Message.DOMA3032, expression, buf.position());
          }
          throw new ExpressionException(Message.DOMA3025, expression, buf.position());
        }
      }
    } else {
      throw new ExpressionException(Message.DOMA3024, expression, buf.position(), c2);
    }
  }

  protected void peekStaticMember() {
    type = STATIC_FIELD_OPERATOR;
    binaryOpAvailable = true;
    if (!buf.hasRemaining()) {
      throw new ExpressionException(Message.DOMA3029, expression, buf.position());
    }
    buf.mark();
    char c = buf.get();
    if (Character.isJavaIdentifierStart(c)) {
      while (buf.hasRemaining()) {
        buf.mark();
        char c2 = buf.get();
        if (!Character.isJavaIdentifierPart(c2)) {
          if (c2 == '(') {
            type = STATIC_METHOD_OPERATOR;
            binaryOpAvailable = false;
          }
          buf.reset();
          return;
        }
      }
    } else {
      throw new ExpressionException(Message.DOMA3030, expression, buf.position(), c);
    }
  }

  protected void peekNumber() {
    type = INT_LITERAL;
    boolean decimal = false;
    while (buf.hasRemaining()) {
      buf.mark();
      char c2 = buf.get();
      if (Character.isDigit(c2)) {
        continue;
      }
      if (c2 == '.') {
        if (decimal) {
          type = ILLEGAL_NUMBER_LITERAL;
          return;
        }
        decimal = true;
        if (buf.hasRemaining()) {
          char c3 = buf.get();
          if (!Character.isDigit(c3)) {
            type = ILLEGAL_NUMBER_LITERAL;
            return;
          }
        } else {
          type = ILLEGAL_NUMBER_LITERAL;
          return;
        }
      } else if (c2 == 'F') {
        type = FLOAT_LITERAL;
        break;
      } else if (c2 == 'D') {
        type = DOUBLE_LITERAL;
        break;
      } else if (c2 == 'L') {
        type = LONG_LITERAL;
        break;
      } else if (c2 == 'B') {
        type = BIGDECIMAL_LITERAL;
        break;
      } else {
        buf.reset();
        break;
      }
    }
    if (!isWordTerminated()) {
      type = ILLEGAL_NUMBER_LITERAL;
    }
    if ((type == INT_LITERAL || type == LONG_LITERAL) && decimal) {
      type = ILLEGAL_NUMBER_LITERAL;
    }
    binaryOpAvailable = true;
  }

  protected boolean isWordTerminated() {
    buf.mark();
    if (buf.hasRemaining()) {
      char c = buf.get();
      if (!Character.isJavaIdentifierPart(c)) {
        buf.reset();
        return true;
      }
    } else {
      return true;
    }
    return false;
  }
}
