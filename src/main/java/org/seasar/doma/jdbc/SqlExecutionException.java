package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;
import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate that the SQL execution is failed.
 */
public class SqlExecutionException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final SqlKind kind;

    protected final String rawSql;

    protected final String formattedSql;

    protected final String sqlFilePath;

    protected final Throwable rootCause;

    public SqlExecutionException(SqlLogType logType, Sql<?> sql, Throwable cause,
            Throwable rootCause) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getFormattedSql(), sql.getSqlFilePath(),
                cause, rootCause);
    }

    public SqlExecutionException(SqlLogType logType, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath, Throwable cause, Throwable rootCause) {
        super(Message.DOMA2009, cause, sqlFilePath, choiceSql(logType, rawSql, formattedSql), cause,
                rootCause);
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
        this.rootCause = rootCause;
    }

    protected SqlExecutionException(MessageResource messageCode, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath, Throwable cause, Throwable rootCause) {
        super(messageCode, cause, sqlFilePath, rawSql, cause, rootCause);
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = null;
        this.sqlFilePath = sqlFilePath;
        this.rootCause = rootCause;
    }

    /**
     * Returns the SQL kind.
     * 
     * @return the SQL kind
     */
    public SqlKind getKind() {
        return kind;
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
     * Returns the formatted SQL string
     * 
     * @return the formatted SQL or {@code null} if this exception is thrown in
     *         the batch process
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * Returns the SQL file path.
     * 
     * @return the SQL file path or {@code null} if the SQL is auto generated
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

    /**
     * Returns the root cause.
     * 
     * @return the root cause
     */
    public Throwable getRootCause() {
        return rootCause;
    }

}
