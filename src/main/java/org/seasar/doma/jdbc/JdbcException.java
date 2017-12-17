package org.seasar.doma.jdbc;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageResource;

/**
 * Thrown to indicate a JDBC related error.
 */
public class JdbcException extends DomaException {

    private static final long serialVersionUID = 1L;

    public JdbcException(MessageResource messageCode, Object... args) {
        super(messageCode, args);
    }

    public JdbcException(MessageResource messageCode, Throwable cause, Object... args) {
        super(messageCode, cause, args);
    }

    /**
     * Chooses the SQL string for logging depending on the log type
     * 
     * @param logType
     *            the log type
     * @param rawSql
     *            the raw SQL string
     * @param formattedSql
     *            the formatted SQL string
     * @return the SQL string
     */
    protected static String choiceSql(SqlLogType logType, String rawSql, String formattedSql) {
        switch (logType) {
        case RAW:
            return rawSql;
        case FORMATTED:
            return formattedSql;
        case NONE:
            return "";
        default:
            throw new AssertionError("unreachable");
        }
    }
}
