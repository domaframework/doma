/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * スクリプトファイル内のSQLの実行中に例外が発生した場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.7.0
 */
public class ScriptException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 未加工SQL */
    protected final String rawSql;

    /** スクリプトファイルのパス */
    protected final String scriptFilePath;

    /** 行番号 */
    protected final int lineNumber;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param sql
     *            SQL
     * @param lineNumber
     *            行番号
     */
    public ScriptException(Throwable cause, Sql<?> sql, int lineNumber) {
        this(cause, sql.getRawSql(), sql.getSqlFilePath(), lineNumber);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param rawSql
     *            未加工SQL
     * @param scriptFilePath
     *            SQLファイルのパス
     * @param lineNumber
     *            行番号
     */
    public ScriptException(Throwable cause, String rawSql,
            String scriptFilePath, int lineNumber) {
        super(Message.DOMA2077, cause, rawSql, scriptFilePath, lineNumber,
                cause);
        this.rawSql = rawSql;
        this.scriptFilePath = scriptFilePath;
        this.lineNumber = lineNumber;
    }

    /**
     * 未加工SQLを返します。
     * 
     * @return 未加工SQL
     */
    public String getRawSql() {
        return rawSql;
    }

    /**
     * スクリプトファイルのパスを返します。
     * 
     * @return スクリプトファイルのパス
     */
    public String getScriptFilePath() {
        return scriptFilePath;
    }

    /**
     * 行番号を返します。
     * 
     * @return 行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
