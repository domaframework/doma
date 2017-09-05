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
 * The SQL file.
 */
public class SqlFile {

    /** the SQL file path */
    protected final String path;

    /** the SQL string */
    protected final String sql;

    /** the SQL node */
    protected final SqlNode sqlNode;

    /**
     * Creates an instance.
     * 
     * @param path
     *            the SQL file path
     * @param sql
     *            the SQL string
     * @param sqlNode
     *            the SQL node
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
     * Returns the SQL file path.
     * 
     * @return the SQL file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the SQL string.
     * 
     * @return the SQL string
     */
    public String getSql() {
        return sql;
    }

    /**
     * Returns the SQL node.
     * <p>
     * Do not modify the SQL node in the client.
     * 
     * @return the SQL node
     */
    public SqlNode getSqlNode() {
        return sqlNode;
    }

    @Override
    public String toString() {
        return sqlNode.toString();
    }

}
