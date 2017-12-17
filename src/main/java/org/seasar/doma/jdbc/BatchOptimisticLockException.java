package org.seasar.doma.jdbc;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that optimistic locking is failed in a batch processing.
 * <p>
 * {@link #getFormattedSql()} returns {@code null}.
 */
public class BatchOptimisticLockException extends OptimisticLockException {

    private static final long serialVersionUID = 1L;

    public BatchOptimisticLockException(SqlLogType logType, Sql<?> sql) {
        this(logType, sql.getKind(), sql.getRawSql(), sql.getSqlFilePath());
    }

    public BatchOptimisticLockException(SqlLogType logType, SqlKind kind, String rawSql,
            String sqlFilePath) {
        super(Message.DOMA2028, kind, choiceSql(logType, rawSql, rawSql), sqlFilePath);
    }

}
