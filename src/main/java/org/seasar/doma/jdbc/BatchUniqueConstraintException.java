package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that an unique constraint violation occurs in a batch
 * processing.
 * <p>
 * {@link #getFormattedSql()} returns {@code null}.
 */
public class BatchUniqueConstraintException extends UniqueConstraintException {

    private static final long serialVersionUID = 1L;

    public BatchUniqueConstraintException(SqlLogType logType, Sql<?> sql, Throwable cause) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath(), cause);
    }

    public BatchUniqueConstraintException(SqlLogType logType, SqlKind kind, String rawSql,
            String sqlFilePath, Throwable cause) {
        super(Message.DOMA2029, kind, choiceSql(logType, rawSql, rawSql), sqlFilePath, cause);
    }

}
