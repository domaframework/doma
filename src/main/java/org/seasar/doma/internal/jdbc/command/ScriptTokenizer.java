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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.jdbc.command.ScriptTokenType.*;
import static org.seasar.doma.internal.util.AssertionUtil.*;

/**
 * SQLスクリプト内のトークンを認識するクラスです。
 * 
 * @author taedium
 */
public class ScriptTokenizer {

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter;

    /** SQLの行 */
    protected String line;

    /** 現在の位置 */
    protected int pos;

    /** 次の位置 */
    protected int nextPos;

    /** {@code #line}の長さ */
    protected int length;

    /** トークン */
    protected String token;

    /** トークンのタイプ */
    protected ScriptTokenType type;

    /** ブロックコメントが開始されている場合{@code true} */
    protected boolean blockCommentStarted;

    /**
     * インスタンスを構築します。
     * 
     * @param blockDelimiter
     *            スクリプトブロックの区切り文字
     */
    public ScriptTokenizer(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
        type = END_OF_LINE;
    }

    /**
     * 1行の文字列を追加します。
     * 
     * @param line
     *            1行の文字列
     */
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

    /**
     * 次のトークンを前もって調べます。
     * 
     * @param index
     *            開始インデックス
     */
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

    /**
     * 文字について次のトークンを前もって調べます。
     * 
     * @param index
     *            開始インデックス
     * @param c
     *            文字
     */
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

    /**
     * 次のトークンタイプを返します。
     * 
     * @return 次のトークンタイプ
     */
    public ScriptTokenType nextToken() {
        switch (type) {
        case END_OF_FILE:
            token = null;
            type = END_OF_FILE;
            return END_OF_FILE;
        case END_OF_LINE:
            token = "";
            type = END_OF_LINE;
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
            for (; type == WORD && pos < length; peek(nextPos)) {
            }
            token = line.substring(wordStartPos, pos);
            return WORD;
        case OTHER:
            int otherStartPos = pos;
            for (; type == OTHER && pos < length; peek(nextPos)) {
            }
            token = line.substring(otherStartPos, pos);
            return OTHER;
        default:
            assertUnreachable(type.name());
            return null;
        }
    }

    /**
     * コメントや単語以外の場合{@code true}を返します。
     * 
     * @param c
     *            文字
     * @return コメントや単語以外の場合{@code true}
     */
    protected static boolean isOther(char c) {
        return Character.isWhitespace(c) || c == '=' || c == '?' || c == '<'
                || c == '>' || c == '(' || c == ')' || c == '!' || c == '*'
                || c == '-' || c == ',';
    }

    /**
     * トークンを返します。
     * 
     * @return トークン
     */
    public String getToken() {
        return token;
    }

}
