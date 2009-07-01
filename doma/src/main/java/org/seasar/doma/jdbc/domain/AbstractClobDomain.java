package org.seasar.doma.jdbc.domain;

import java.sql.Clob;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractClobDomain extends
        AbstractDomain<Clob, AbstractClobDomain> {

    public AbstractClobDomain() {
        super(null);
    }

    public AbstractClobDomain(Clob v) {
        super(v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractClobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractClobDomainVisitor<R, P, TH> v = AbstractClobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractClobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
