package org.seasar.doma.jdbc;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class NonUniqueResultException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    public NonUniqueResultException(Sql<?> sql) {
        this(sql.getRawSql(), sql.getFormattedSql());
    }

    public NonUniqueResultException(String rawSql, String formattedSql) {
        super(MessageCode.DOMA2001, rawSql, formattedSql);
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
