package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.List;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class DomainListParameter implements ListParameter<Domain<?, ?>> {

    protected final Class<Domain<?, ?>> domainClass;

    protected final List<Domain<?, ?>> domains;

    public DomainListParameter(Class<Domain<?, ?>> domainClass,
            List<Domain<?, ?>> domains) {
        assertNotNull(domainClass, domains);
        this.domainClass = domainClass;
        this.domains = domains;
    }

    public Domain<?, ?> add() {
        Domain<?, ?> domain = createDomain();
        domains.add(domain);
        return domain;
    }

    protected Domain<?, ?> createDomain() {
        try {
            return Classes.newInstance(domainClass);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2006, cause, domainClass
                    .getName(), cause);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainListParameter(this, p);
    }

}
