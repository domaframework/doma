package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import java.util.ArrayList;
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
public class DomainListResultParameter<D extends Domain<?, ?>> implements
        ResultParameter<List<D>>, ListParameter<D> {

    protected final Class<D> domainClass;

    protected final List<D> domains = new ArrayList<D>();

    public DomainListResultParameter(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
    }

    public D add() {
        D domain = createDomain();
        domains.add(domain);
        return domain;
    }

    protected D createDomain() {
        try {
            return Classes.newInstance(domainClass);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2006, cause, domainClass
                    .getName(), cause);
        }
    }

    @Override
    public List<D> getResult() {
        return domains;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainListResultParameter(this, p);
    }
}
