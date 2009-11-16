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

import org.seasar.doma.internal.message.DomaMessageCode;

/**
 * 1件であることを期待する検索系SQLの結果が2件以上である場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class NonUniqueResultException extends JdbcException {

    private static final long serialVersionUID = 1L;

    /** 未加工SQL */
    protected final String rawSql;

    /** フォーマット済みSQL */
    protected final String formattedSql;

    /** SQLファイルのパス */
    protected final String sqlFilePath;

    /**
     * 2件以上の結果を返したSQLを指定してインスタンスを構築します。
     * 
     * @param sql
     *            SQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public NonUniqueResultException(Sql<?> sql, String sqlFilePath) {
        this(sql.getRawSql(), sql.getFormattedSql(), sqlFilePath);
    }

    /**
     * 2件以上の結果を返した未加工SQLとフォーマット済みSQLを指定してインスタンスを構築します。
     * 
     * @param rawSql
     *            未加工SQL
     * @param formattedSql
     *            フォーマット済みSQL
     * @param sqlFilePath
     *            SQLファイルのパス
     */
    public NonUniqueResultException(String rawSql, String formattedSql,
            String sqlFilePath) {
        super(DomaMessageCode.DOMA2001, sqlFilePath, formattedSql);
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
