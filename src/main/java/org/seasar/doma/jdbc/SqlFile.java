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
