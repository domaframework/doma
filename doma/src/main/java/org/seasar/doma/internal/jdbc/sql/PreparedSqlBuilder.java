package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.domain.DomainVisitor;


/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilder {

    protected final List<BindParameter> parameters = new ArrayList<BindParameter>();

    protected final StringBuilder rawSql = new StringBuilder(200);

    protected final StringBuilder formattedSql = new StringBuilder(200);

    protected final DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor;

    public PreparedSqlBuilder(
            DomainVisitor<String, Void, RuntimeException> sqlLogFormattingVisitor) {
        assertNotNull(sqlLogFormattingVisitor);
        this.sqlLogFormattingVisitor = sqlLogFormattingVisitor;
    }

    public void appendSql(String sql) {
        rawSql.append(sql);
        formattedSql.append(sql);
    }

    public void cutBackSql(int length) {
        rawSql.setLength(rawSql.length() - length);
        formattedSql.setLength(formattedSql.length() - length);
    }

    public void appendDomain(Domain<?, ?> domain) {
        rawSql.append("?");
        formattedSql.append(domain.accept(sqlLogFormattingVisitor, null));
        parameters.add(new BindParameter(domain));
    }

    public PreparedSql build() {
        return new PreparedSql(rawSql, formattedSql, parameters);
    }
}
