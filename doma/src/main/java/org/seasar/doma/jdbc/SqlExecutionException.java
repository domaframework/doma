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
package org.seasar.doma.jdbc;

import org.seasar.doma.MessageCode;
import org.seasar.doma.message.DomaMessageCode;

/**
 * SQLの実行に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class SqlExecutionException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL、バッチ処理時にスローされた場合 {@code null} */
    protected final String formattedSql;

    /** 根本原因 */
    protected final Throwable rootCause;

    /**
     * SQLを指定してインスタンスを構築します。
     * 
     * @param sql
     *            SQL
     * @param cause
     *            原因
     * @param rootCause
     *            根本原因
     */
    public SqlExecutionException(Sql<?> sql, Throwable cause,
            Throwable rootCause) {
        this(sql.getRawSql(), sql.getFormattedSql(), cause, rootCause);
    }

    /**
     * 未加工SQLとフォーマット済みSQLを指定してインスタンスを構築します。
     * 
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param cause
     *            原因
     * @param rootCause
     *            根本原因
     */
    public SqlExecutionException(String rawSql, String formattedSql,
            Throwable cause, Throwable rootCause) {
        super(DomaMessageCode.DOMA2009, cause, formattedSql, rawSql, cause,
                rootCause);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.rootCause = rootCause;
    }

    /**
     * メッセージコード、未加工SQL、フォーマット済みSQLを指定してインスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param cause
     *            原因
     * @param rootCause
     *            根本原因
     */
    protected SqlExecutionException(MessageCode messageCode, String rawSql,
            String formattedSql, Throwable cause, Throwable rootCause) {
        super(messageCode, cause, rawSql, cause, rootCause);
        this.rawSql = rawSql;
        this.formattedSql = null;
        this.rootCause = rootCause;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

    public Throwable getRootCause() {
        return rootCause;
    }

}
