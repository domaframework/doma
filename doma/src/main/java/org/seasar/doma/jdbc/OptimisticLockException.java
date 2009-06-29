package org.seasar.doma.jdbc;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class OptimisticLockException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    public OptimisticLockException(Sql<?> sql) {
        this(sql.getRawSql(), sql.getFormattedSql());
    }

    public OptimisticLockException(String rawSql, String formattedSql) {
        super(MessageCode.DOMA2003, rawSql, formattedSql);
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
