package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.jdbc.command.ScriptTokenType.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

public class ScriptTokenizer {

  protected final String blockDelimiter;

  protected String line;

  protected int pos;

  protected int nextPos;

  protected int length;

  protected String token;

  protected ScriptTokenType type;

  protected boolean blockCommentStarted;

  public ScriptTokenizer(String blockDelimiter) {
    this.blockDelimiter = blockDelimiter;
    type = END_OF_LINE;
  }

  public void addLine(String line) {
    if (line == null) {
      type = END_OF_FILE;
      return;
    }
    this.line = line;
    length = line.length();
    pos = 0;
    nextPos = 0;

    if (blockCommentStarted) {
      type = BLOCK_COMMENT;
    } else if (line.trim().equalsIgnoreCase(blockDelimiter)) {
      type = BLOCK_DELIMITER;
      nextPos = length;
    } else {
      peek(pos);
    }
  }

  protected void peek(int index) {
    if (index < length) {
      char c = line.charAt(index);
      if (c == '\'') {
        type = QUOTE;
        pos = index;
        nextPos = index + 1;
      } else {
        int nextIndex = index + 1;
        if (nextIndex < length) {
          char c2 = line.charAt(nextIndex);
          if (c == '-' && c2 == '-') {
            type = LINE_COMMENT;
            pos = index;
            nextPos = index + 2;
          } else if (c == '/' && c2 == '*') {
            type = START_OF_BLOCK_COMMENT;
            pos = index;
            nextPos = index + 2;
          } else {
            peekChar(index, c);
          }
        } else {
          peekChar(index, c);
        }
      }
    } else {
      type = END_OF_LINE;
      pos = length;
      nextPos = length;
    }
  }

  protected void peekChar(int index, char c) {
    if (c == ';') {
      type = STATEMENT_DELIMITER;
    } else if (isOther(c)) {
      type = OTHER;
    } else {
      type = WORD;
    }
    pos = index;
    nextPos = index + 1;
  }

  public ScriptTokenType nextToken() {
    switch (type) {
      case END_OF_FILE:
        token = null;
        return END_OF_FILE;
      case END_OF_LINE:
        token = "";
        return END_OF_LINE;
      case BLOCK_DELIMITER:
        token = line;
        type = END_OF_LINE;
        return BLOCK_DELIMITER;
      case STATEMENT_DELIMITER:
        token = line.substring(pos, nextPos);
        peek(nextPos);
        return STATEMENT_DELIMITER;
      case LINE_COMMENT:
        token = line.substring(pos, length);
        type = END_OF_LINE;
        return LINE_COMMENT;
      case START_OF_BLOCK_COMMENT:
        token = line.substring(pos, nextPos);
        type = BLOCK_COMMENT;
        pos = pos + 2;
        nextPos = pos + 2;
        return START_OF_BLOCK_COMMENT;
      case BLOCK_COMMENT:
        for (int i = nextPos; i < length; i++) {
          char c = line.charAt(i);
          int nextIndex = i + 1;
          if (nextIndex < length) {
            char c2 = line.charAt(nextIndex);
            if (c == '*' && c2 == '/') {
              blockCommentStarted = false;
              token = line.substring(pos, i);
              type = END_OF_BLOCK_COMMENT;
              pos = i;
              nextPos = i + 2;
              return BLOCK_COMMENT;
            }
          }
        }
        blockCommentStarted = true;
        token = line.substring(pos, length);
        type = END_OF_LINE;
        return BLOCK_COMMENT;
      case END_OF_BLOCK_COMMENT:
        token = line.substring(pos, nextPos);
        peek(nextPos);
        return END_OF_BLOCK_COMMENT;
      case QUOTE:
        for (int i = nextPos; i < length; i++) {
          char c = line.charAt(i);
          if (c == '\'') {
            i++;
            if (i >= length) {
              token = line.substring(pos, i);
              type = END_OF_LINE;
              return QUOTE;
            } else if (line.charAt(i) != '\'') {
              token = line.substring(pos, i);
              peek(i);
              return QUOTE;
            }
          }
        }
        token = line.substring(pos, length);
        type = END_OF_LINE;
        return QUOTE;
      case WORD:
        int wordStartPos = pos;
        while (type == WORD && pos < length) {
          peek(nextPos);
        }
        token = line.substring(wordStartPos, pos);
        return WORD;
      case OTHER:
        int otherStartPos = pos;
        while (type == OTHER && pos < length) {
          peek(nextPos);
        }
        token = line.substring(otherStartPos, pos);
        return OTHER;
      default:
        assertUnreachable(type.name());
        return null;
    }
  }

  protected static boolean isOther(char c) {
    return Character.isWhitespace(c)
        || c == '='
        || c == '?'
        || c == '<'
        || c == '>'
        || c == '('
        || c == ')'
        || c == '!'
        || c == '*'
        || c == '-'
        || c == ',';
  }

  public String getToken() {
    return token;
  }
}
