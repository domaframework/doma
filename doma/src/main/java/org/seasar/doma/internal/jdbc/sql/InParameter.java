package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class InParameter implements CallableSqlParameter {

    protected final Domain<?, ?> domain;

    public InParameter(Domain<?, ?> domain) {
        assertNotNull(domain);
        this.domain = domain;
    }

    public Domain<?, ?> getDomain() {
        return domain;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitInParameter(this, p);
    }

}
