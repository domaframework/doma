package org.seasar.doma.jdbc;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class UniqueConstraintException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    public UniqueConstraintException(Sql<?> sql, Throwable cause) {
        this(sql.getRawSql(), sql.getFormattedSql(), cause);
    }

    public UniqueConstraintException(String rawSql, String formattedSql,
            Throwable cause) {
        super(MessageCode.DOMA2004, cause, rawSql, formattedSql, cause);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

}
