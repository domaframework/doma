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
