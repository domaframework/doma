package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;
import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate that optimistic locking is failed.
 */
public class OptimisticLockException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final SqlKind kind;

    protected final String rawSql;

    protected final String formattedSql;

    protected final String sqlFilePath;

    public OptimisticLockException(SqlLogType logType, Sql<?> sql) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getFormattedSql(), sql.getSqlFilePath());
    }

    public OptimisticLockException(SqlLogType logType, SqlKind kind, String rawSql,
            String formattedSql, String sqlFilePath) {
        super(Message.DOMA2003, sqlFilePath, choiceSql(logType, rawSql, formattedSql));
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
    }

    protected OptimisticLockException(MessageResource messageCode, SqlKind kind, String rawSql,
            String sqlFilePath) {
        super(messageCode, sqlFilePath, rawSql);
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = null;
        this.sqlFilePath = sqlFilePath;
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
     * Returns the formatted SQL string.
     * 
     * @return the formatted SQL string or {@code null} if it does not exist
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * Returns the SQL file path
     * 
     * @return the SQL file path or {@code null} if the SQL is auto generated
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }

}
