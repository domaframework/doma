package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that the one of SQL statements in a script file throws an
 * exception.
 */
public class ScriptException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String scriptFilePath;

    protected final int lineNumber;

    public ScriptException(Throwable cause, Sql<?> sql, int lineNumber) {
        this(cause, sql.getRawSql(), sql.getSqlFilePath(), lineNumber);
    }

    public ScriptException(Throwable cause, String rawSql, String scriptFilePath, int lineNumber) {
        super(Message.DOMA2077, cause, rawSql, scriptFilePath, lineNumber, cause);
        this.rawSql = rawSql;
        this.scriptFilePath = scriptFilePath;
        this.lineNumber = lineNumber;
    }

    /**
     * Returns the raw SQL string.
     * 
     * @return the raw SQL string
     */
    public String getRawSql() {
        return rawSql;
    }

    /**
     * Returns the script file path.
     * 
     * @return the script file path
     */
    public String getScriptFilePath() {
        return scriptFilePath;
    }

    /**
     * Returns the line number of the script file.
     * 
     * @return the line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
