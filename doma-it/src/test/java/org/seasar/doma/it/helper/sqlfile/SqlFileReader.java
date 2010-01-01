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
package org.seasar.doma.it.helper.sqlfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.it.helper.dialect.ToolDialect;
import org.seasar.doma.it.helper.dialect.ToolDialect.SqlBlockContext;
import org.seasar.doma.it.helper.sqlfile.SqlFileTokenizer.TokenType;

/**
 * SQLファイルのリーダです。
 * 
 * @author taedium
 */
public class SqlFileReader {

    /** SQLファイル */
    protected File sqlFile;

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding;

    /** トークナイザ */
    protected SqlFileTokenizer tokenizer;

    /** 方言 */
    protected ToolDialect dialect;

    /** リーダ */
    protected BufferedReader reader;

    /** 行番号のカウント */
    protected int lineCount;

    /** 処理対象のSQLの先頭の行番号 */
    protected int lineNumber;

    /** ファイルの終端に達した場合{@code true} */
    protected boolean endOfFile;

    /** 行の終端に達した場合{@code true} */
    protected boolean endOfLine = true;

    /**
     * インスタンスを構築します。
     * 
     * @param sqlFile
     *            SQLファイル
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     * @param tokenizer
     *            トークナイザ
     * @param dialect
     *            方言
     */
    public SqlFileReader(File sqlFile, String sqlFileEncoding,
            SqlFileTokenizer tokenizer, ToolDialect dialect) {
        if (sqlFile == null) {
            throw new NullPointerException("sqlFile");
        }
        if (sqlFileEncoding == null) {
            throw new NullPointerException("sqlFileEncoding");
        }
        if (tokenizer == null) {
            throw new NullPointerException("tokenizer");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.sqlFile = sqlFile;
        this.sqlFileEncoding = sqlFileEncoding;
        this.tokenizer = tokenizer;
        this.dialect = dialect;
    }

    /**
     * SQLステートメントもしくはSQLブロックを読み取ります。
     * 
     * @return ファイルの終端に達していなければSQL、ファイルの終端に達していれば{@code null}
     */
    public String readSql() {
        if (endOfFile) {
            return null;
        }
        try {
            if (reader == null) {
                reader = createBufferedReader();
            }
            SqlBuilder builder = new SqlBuilder();
            readLineLoop: for (;;) {
                if (endOfLine) {
                    lineCount++;
                    tokenizer.addLine(reader.readLine());
                    builder.notifyLineChanged();
                }
                for (;;) {
                    builder.build(tokenizer.nextToken(), tokenizer.getToken());
                    if (builder.isTokenRequired()) {
                        continue;
                    } else if (builder.isLineRequired()) {
                        continue readLineLoop;
                    } else if (builder.isCompleted()) {
                        return builder.getSql();
                    }
                    throw new IllegalStateException("builder");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 処理対象のSQLの先頭の行番号を返します。
     * <p>
     * 行番号は1から始まります。
     * </p>
     * 
     * @return 行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * クローズします。
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ignore) {
            // logger.log(ignore);
        }
    }

    /**
     * {@link #sqlFile}に対する{@link BufferedReader}を作成します。
     * 
     * @return {@link BufferedReader}
     * @throws IOException
     *             IOに関する例外が発生した場合
     */
    protected BufferedReader createBufferedReader() throws IOException {
        InputStream is = new FileInputStream(sqlFile);
        return new BufferedReader(new InputStreamReader(is, sqlFileEncoding));
    }

    /**
     * SQLのビルダです。
     * 
     * @author taedium
     */
    protected class SqlBuilder {

        /** 次のトークンが必要な場合{@code true} */
        protected boolean tokenRequired;

        /** 次の行が必要な場合{@code true} */
        protected boolean lineRequired;

        /** SQLの組み立てが完了した場合{@code true} */
        protected boolean completed;

        /** SQLの文字列を保持するバッファ */
        protected StringBuilder buf = new StringBuilder(300);

        /** SQLのキーワードを管理するリスト */
        protected List<String> wordList = new ArrayList<String>();

        /** SQLブロックのコンテキスト */
        protected SqlBlockContext sqlBlockContext;

        /** 行が変更された場合{@code true} */
        protected boolean lineChanged;

        /**
         * インスタンスを構築します
         */
        protected SqlBuilder() {
            sqlBlockContext = dialect.createSqlBlockContext();
        }

        /**
         * SQL文を組み立てます。
         * 
         * @param tokenType
         *            トークンのタイプ
         * @param token
         *            トークン
         */
        protected void build(TokenType tokenType, String token) {
            reset();
            if (buf.length() == 0) {
                lineNumber = lineCount;
            }
            switch (tokenType) {
            case WORD:
                appendWord(token);
            case QUOTE:
            case OTHER:
                appendToken(token);
                requireToken();
                break;
            case END_OF_LINE:
                endOfLine = true;
                requireLine();
                break;
            case STATEMENT_DELIMITER:
                if (isInSqlBlock()) {
                    appendToken(token);
                    requireToken();
                } else {
                    complete();
                }
                break;
            case BLOCK_DELIMITER:
                if (isSqlEmpty()) {
                    requireToken();
                } else {
                    complete();
                }
                break;
            case END_OF_FILE:
                endOfFile = true;
                complete();
                break;
            default:
                requireToken();
                break;
            }
        }

        /**
         * リセットします。
         */
        protected void reset() {
            endOfLine = false;
            requireToken();
        }

        /**
         * 次のトークンが必要な場合{@code true}を返します。
         * 
         * @return 次のトークンが必要な場合{@code true}
         */
        protected boolean isTokenRequired() {
            return tokenRequired;
        }

        /**
         * 次のトークンを要求します。
         * 
         */
        protected void requireToken() {
            tokenRequired = true;
            lineRequired = false;
            completed = false;
        }

        /**
         * 次の行が必要な場合{@code true}を返します。
         * 
         * @return 次の行が必要な場合{@code true}
         */
        protected boolean isLineRequired() {
            return lineRequired;
        }

        /**
         * 次の行を要求します。
         * 
         */
        protected void requireLine() {
            lineRequired = true;
            tokenRequired = false;
            completed = false;
        }

        /**
         * SQLの組み立てが完了した場合{@code true}を返します。
         * 
         * @return SQLの組み立てが完了した場合{@code true}
         */
        protected boolean isCompleted() {
            return completed;
        }

        /**
         * SQLの組み立てを完了します。
         * 
         */
        protected void complete() {
            completed = true;
            tokenRequired = false;
            lineRequired = false;
        }

        /**
         * 単語を追加します。
         * 
         * @param word
         *            単語
         */
        protected void appendWord(String word) {
            sqlBlockContext.addKeyword(word);
        }

        /**
         * トークンを追加します。
         * 
         * @param token
         *            トークン
         */
        protected void appendToken(String token) {
            appendWhitespaceIfNecessary();
            buf.append(token);
        }

        /**
         * 必要ならば空白を追加します。
         */
        protected void appendWhitespaceIfNecessary() {
            if (!lineChanged) {
                return;
            }
            if (buf.length() > 0) {
                char lastChar = buf.charAt(buf.length() - 1);
                if (!Character.isWhitespace(lastChar)) {
                    buf.append(' ');
                }
            }
            lineChanged = false;
        }

        /**
         * 行が変更されたことを通知します。
         */
        protected void notifyLineChanged() {
            lineChanged = true;
        }

        /**
         * SQLブロックの内側を組み立てている場合{@code true}を返します。
         * 
         * @return SQLブロックの内側を組み立てている場合{@code true}
         */
        protected boolean isInSqlBlock() {
            return sqlBlockContext.isInSqlBlock();
        }

        /**
         * SQLが空の場合{@code true}を返します。
         * 
         * @return SQLが空の場合{@code true}
         */
        protected boolean isSqlEmpty() {
            return buf.toString().trim().length() == 0;
        }

        /**
         * SQLを返します。
         * 
         * @return SQL
         */
        protected String getSql() {
            if (!completed) {
                throw new IllegalStateException("completed");
            }

            String sql = buf.toString().trim();
            return endOfFile && sql.length() == 0 ? null : sql;
        }
    }

}
