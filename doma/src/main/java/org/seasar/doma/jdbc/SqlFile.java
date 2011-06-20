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

import org.seasar.doma.DomaNullPointerException;

/**
 * SQLファイルです。
 * <p>
 * SQLファイルのパス、SQLの文字列、SQLの文字列を解析した結果をカプセル化します。
 * 
 * @author taedium
 * 
 */
public class SqlFile {

    /** SQLファイルのパス */
    protected final String path;

    /** SQLの文字列 */
    protected final String sql;

    /** SQLの解析結果 */
    protected final SqlNode sqlNode;

    /**
     * 
     * @param path
     *            SQLファイルのパス
     * @param sql
     *            SQLの文字列
     * @param sqlNode
     *            SQLの解析結果
     */
    public SqlFile(String path, String sql, SqlNode sqlNode) {
        if (path == null) {
            throw new DomaNullPointerException("path");
        }
        if (sql == null) {
            throw new DomaNullPointerException("sql");
        }
        if (sqlNode == null) {
            throw new DomaNullPointerException("sqlNode");
        }
        this.path = path;
        this.sql = sql;
        this.sqlNode = sqlNode;
    }

    /**
     * SQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス
     */
    public String getPath() {
        return path;
    }

    /**
     * SQLの文字列を返します。
     * 
     * @return SQLの文字列
     */
    public String getSql() {
        return sql;
    }

    /**
     * SQLの解析結果を返します。
     * <p>
     * 呼び出し側でSQLの解析結果を変更してはいけません。
     * 
     * @return SQLの解析結果
     */
    public SqlNode getSqlNode() {
        return sqlNode;
    }

    @Override
    public String toString() {
        return sqlNode.toString();
    }

}
