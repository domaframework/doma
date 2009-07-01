package org.seasar.doma.jdbc.domain;

import java.sql.NClob;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractNClobDomain extends
        AbstractDomain<NClob, AbstractNClobDomain> {

    public AbstractNClobDomain() {
        super(null);
    }

    public AbstractNClobDomain(NClob v) {
        super(v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractNClobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractNClobDomainVisitor<R, P, TH> v = AbstractNClobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractNClobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
