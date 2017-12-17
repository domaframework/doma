package org.seasar.doma.internal.jdbc.sql.node;

/**
 * @author taedium
 * 
 */
public class SqlLocation {

    protected final String sql;

    protected final int lineNumber;

    protected final int position;

    public SqlLocation(String sql, int lineNumber, int position) {
        this.sql = sql;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    public String getSql() {
        return sql;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return sql + ":" + lineNumber + ":" + position;
    }
}
