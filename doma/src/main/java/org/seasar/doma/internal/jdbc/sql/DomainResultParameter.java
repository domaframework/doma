package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.Classes;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomainResultParameter<D extends Domain<?, ?>> implements
        ResultParameter<D> {

    protected static final int INDEX = 1;

    protected final Class<D> domainClass;

    protected final D domain;

    public DomainResultParameter(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
        this.domain = createDomain();
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

    public D getDomain() {
        return domain;
    }

    public int getIndex() {
        return INDEX;
    }

    @Override
    public D getResult() {
        return domain;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainResultParameter(this, p);
    }

}
