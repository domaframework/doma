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

import org.seasar.doma.internal.message.Message;

/**
 * 1列であることを期待する検索系SQLの結果が1列でない場合にスローされる例外です。
 * 
 * @author taedium
 * @since 1.5.0
 */
public class NonSingleColumnException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL */
    protected final String formattedSql;

    /** SQLファイルのパス */
    protected final String sqlFilePath;

    /**
     * 1列でない結果を返したSQLを指定してインスタンスを構築します。
     * 
     * @param sql
     *            SQL
     */
    public NonSingleColumnException(Sql<?> sql) {
        this(sql.getRawSql(), sql.getFormattedSql(), sql.getSqlFilePath());
    }

    /**
     * 1列でない結果を返した未加工SQLとフォーマット済みSQLを指定してインスタンスを構築します。
     * 
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public NonSingleColumnException(String rawSql, String formattedSql,
            String sqlFilePath) {
        super(Message.DOMA2006, sqlFilePath, formattedSql);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
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
     * フォーマット済みSQLを返します。
     * 
     * @return フォーマット済みSQL
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * SQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス、SQLが自動生成された場合 {@code null}
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

}
