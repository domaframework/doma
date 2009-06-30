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

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public class SqlFile {

    protected final String realPath;

    protected final String sql;

    protected final SqlNode sqlNode;

    public SqlFile(String realPath, String sql, SqlNode sqlNode) {
        if (realPath == null) {
            throw new DomaIllegalArgumentException("realPath", realPath);
        }
        if (sql == null) {
            throw new DomaIllegalArgumentException("sql", sql);
        }
        if (sqlNode == null) {
            throw new DomaIllegalArgumentException("sqlNode", sqlNode);
        }
        this.realPath = realPath;
        this.sql = sql;
        this.sqlNode = sqlNode;
    }

    public String getRealPath() {
        return realPath;
    }

    public String getSql() {
        return sql;
    }

    public SqlNode getSqlNode() {
        return sqlNode.copy();
    }

    @Override
    public String toString() {
        return realPath;
    }

}
