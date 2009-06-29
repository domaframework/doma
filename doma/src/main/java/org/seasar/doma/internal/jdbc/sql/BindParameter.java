package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class BindParameter implements PreparedSqlParameter {

    protected final Domain<?, ?> domain;

    public BindParameter(Domain<?, ?> domain) {
        assertNotNull(domain);
        this.domain = domain;
    }

    public Domain<?, ?> getDomain() {
        return domain;
    }

}
