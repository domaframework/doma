/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.nio.CharBuffer;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;


/**
 * @author taedium
 * 
 */
public class SqlTokenizer {

    protected final String sql;

    protected final CharBuffer buf;

    protected CharBuffer duplicatedBuf;

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
            type = EOF;
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
        duplicatedBuf = buf.duplicate();
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
                                            } else
                                                peekNineChars(c, c2, c3, c4, c5, c6, c7, c8, c9);
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

    protected void peekTenChars(char c, char c2, char c3, char c4, char c5,
            char c6, char c7, char c8, char c9, char c10) {
        if ((c == 'f' || c == 'F') && (c2 == 'o' || c2 == 'O')
                && (c3 == 'r' || c3 == 'R')
                && (Character.isWhitespace(c4) && (c4 != '\r' && c4 != '\n'))
                && (c5 == 'u' || c5 == 'U') && (c6 == 'p' || c6 == 'P')
                && (c7 == 'd' || c7 == 'D') && (c8 == 'a' || c8 == 'A')
                && (c9 == 't' || c9 == 'T') && (c10 == 'e' || c10 == 'E')) {
            type = FOR_UPDATE_WORD;
            if (isWordTerminated()) {
                return;
            }
        }
        buf.position(buf.position() - 1);
        peekNineChars(c, c2, c3, c4, c5, c6, c7, c8, c9);
    }

    protected void peekNineChars(char c, char c2, char c3, char c4, char c5,
            char c6, char c7, char c8, char c9) {
        buf.position(buf.position() - 1);
        peekEightChars(c, c2, c3, c4, c5, c6, c7, c8);
    }

    protected void peekEightChars(char c, char c2, char c3, char c4, char c5,
            char c6, char c7, char c8) {
        if ((c == '-') && (c2 == '-') && (c3 == 'e') && (c4 == 'l')
                && (c5 == 's') && (c6 == 'e') && c7 == 'i' && c8 == 'f') {
            type = ELSEIF_LINE_COMMENT;
            if (isWordTerminated()) {
                while (buf.hasRemaining()) {
                    char c9 = buf.get();
                    if (c9 == '-') {
                        if (buf.hasRemaining()) {
                            char c10 = buf.get();
                            if (c10 == '-') {
                                return;
                            }
                        }
                    }
                }
                int pos = buf.position() - lineStartPosition;
                throw new JdbcException(DomaMessageCode.DOMA2103, sql, lineNumber,
                        pos);
            }
        } else if ((c == 'g' || c == 'G') && (c2 == 'r' || c2 == 'R')
                && (c3 == 'o' || c3 == 'O') && (c4 == 'u' || c4 == 'U')
                && (c5 == 'p' || c5 == 'P')
                && (Character.isWhitespace(c6) && (c6 != '\r' && c6 != '\n'))
                && (c7 == 'b' || c7 == 'B') && (c8 == 'y' || c8 == 'Y')) {
            type = GROUP_BY_WORD;
            if (isWordTerminated()) {
                return;
            }
        } else if ((c == 'o' || c == 'O') && (c2 == 'r' || c2 == 'R')
                && (c3 == 'd' || c3 == 'D') && (c4 == 'e' || c4 == 'E')
                && (c5 == 'r' || c5 == 'R')
                && (Character.isWhitespace(c6) && (c6 != '\r' && c6 != '\n'))
                && (c7 == 'b' || c7 == 'B') && (c8 == 'y' || c8 == 'Y')) {
            type = ORDER_BY_WORD;
            if (isWordTerminated()) {
                return;
            }
        }
        buf.position(buf.position() - 1);
        peekSevenChars(c, c2, c3, c4, c5, c6, c7);
    }

    protected void peekSevenChars(char c, char c2, char c3, char c4, char c5,
            char c6, char c7) {
        buf.position(buf.position() - 1);
        peekSixChars(c, c2, c3, c4, c5, c6);
    }

    protected void peekSixChars(char c, char c2, char c3, char c4, char c5,
            char c6) {
        if ((c == 's' || c == 'S') && (c2 == 'e' || c2 == 'E')
                && (c3 == 'l' || c3 == 'L') && (c4 == 'e' || c4 == 'E')
                && (c5 == 'c' || c5 == 'C') && (c6 == 't' || c6 == 'T')) {
            type = SELECT_WORD;
            if (isWordTerminated()) {
                return;
            }
        } else if ((c == 'h' || c == 'H') && (c2 == 'a' || c2 == 'A')
                && (c3 == 'v' || c3 == 'V') && (c4 == 'i' || c4 == 'I')
                && (c5 == 'n' || c5 == 'N') && (c6 == 'g' || c6 == 'G')) {
            type = HAVING_WORD;
            if (isWordTerminated()) {
                return;
            }
        } else if ((c == '-') && (c2 == '-') && (c3 == 'e') && (c4 == 'l')
                && (c5 == 's') && (c6 == 'e')) {
            type = ELSE_LINE_COMMENT;
            if (isWordTerminated()) {
                return;
            }
        }
        buf.position(buf.position() - 1);
        peekFiveChars(c, c2, c3, c4, c5);
    }

    protected void peekFiveChars(char c, char c2, char c3, char c4, char c5) {
        if ((c == 'w' || c == 'W') && (c2 == 'h' || c2 == 'H')
                && (c3 == 'e' || c3 == 'E') && (c4 == 'r' || c4 == 'R')
                && (c5 == 'e' || c5 == 'E')) {
            type = WHERE_WORD;
            if (isWordTerminated()) {
                return;
            }
        }
        buf.position(buf.position() - 1);
        peekFourChars(c, c2, c3, c4);
    }

    protected void peekFourChars(char c, char c2, char c3, char c4) {
        if ((c == 'f' || c == 'F') && (c2 == 'r' || c2 == 'R')
                && (c3 == 'o' || c3 == 'O') && (c4 == 'm' || c4 == 'M')) {
            type = FROM_WORD;
            if (isWordTerminated()) {
                return;
            }
        }
        buf.position(buf.position() - 1);
        peekThreeChars(c, c2, c3);
    }

    protected void peekThreeChars(char c, char c2, char c3) {
        if ((c == 'a' || c == 'A') && (c2 == 'n' || c2 == 'N')
                && (c3 == 'd' || c3 == 'D')) {
            type = AND_WORD;
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
                if (Character.isJavaIdentifierStart(c3)) {
                    type = BIND_BLOCK_COMMENT;
                } else if (buf.hasRemaining()) {
                    char c4 = buf.get();
                    if (buf.hasRemaining()) {
                        char c5 = buf.get();
                        if (c3 == '%' && c4 == 'i' && c5 == 'f') {
                            if (isBlockCommentDirectiveTerminated()) {
                                type = IF_BLOCK_COMMENT;
                            }
                        } else if (buf.hasRemaining()) {
                            char c6 = buf.get();
                            if (c3 == '%' && c4 == 'e' && c5 == 'n'
                                    && c6 == 'd') {
                                if (isBlockCommentDirectiveTerminated()) {
                                    type = END_BLOCK_COMMENT;
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
            while (buf.hasRemaining()) {
                char c3 = buf.get();
                if (c3 == '*') {
                    if (buf.hasRemaining()) {
                        char c4 = buf.get();
                        if (c4 == '/') {
                            return;
                        }
                    }
                }
            }
            int pos = buf.position() - lineStartPosition;
            throw new JdbcException(DomaMessageCode.DOMA2102, sql, lineNumber, pos);
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
        if (c == '(') {
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
            throw new JdbcException(DomaMessageCode.DOMA2101, sql, lineNumber, pos);
        } else if (!isOther(c)) {
            type = WORD;
            while (buf.hasRemaining()) {
                if (isWordTerminated()) {
                    return;
                }
                buf.get();
            }
        } else if (c == '\r' || c == '\n') {
            type = EOL;
            currentLineNumber++;
            return;
        } else {
            type = OTHER;
        }
    }

    protected boolean isWordTerminated() {
        buf.mark();
        if (buf.hasRemaining()) {
            char c = buf.get();
            buf.reset();
            if (isOther(c)) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    protected boolean isBlockCommentDirectiveTerminated() {
        buf.mark();
        if (buf.hasRemaining()) {
            char c = buf.get();
            buf.reset();
            if (isOther(c) || c == '*') {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    protected boolean isOther(char c) {
        return Character.isWhitespace(c) || c == '=' || c == '<' || c == '>'
                || c == '-' || c == ',' || c == '/' || c == '+' || c == '('
                || c == ')' || c == ';';
    }
}
