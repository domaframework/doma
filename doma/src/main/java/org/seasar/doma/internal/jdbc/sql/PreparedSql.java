package org.seasar.doma.internal.jdbc.sql;

import java.util.Collections;
import java.util.List;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.Sql;


/**
 * 
 * @author taedium
 * 
 */
public class PreparedSql implements Sql<PreparedSqlParameter> {

    protected final String rawSql;

    protected final String formattedSql;

    protected final List<PreparedSqlParameter> parameters;

    public PreparedSql(CharSequence rawSql, CharSequence formattedSql,
            List<? extends PreparedSqlParameter> parameters) {
        if (rawSql == null) {
            throw new DomaIllegalArgumentException("rawSql", rawSql);
        }
        if (formattedSql == null) {
            throw new DomaIllegalArgumentException("formattedSql", formattedSql);
        }
        if (parameters == null) {
            throw new DomaIllegalArgumentException("parameters", parameters);
        }
        this.rawSql = rawSql.toString().trim();
        this.formattedSql = formattedSql.toString().trim();
        this.parameters = Collections.unmodifiableList(parameters);
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

    public List<PreparedSqlParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return rawSql;
    }

}
