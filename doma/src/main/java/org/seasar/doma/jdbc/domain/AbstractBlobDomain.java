package org.seasar.doma.jdbc.domain;

import java.sql.Blob;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractBlobDomain extends
        AbstractDomain<Blob, AbstractBlobDomain> {

    public AbstractBlobDomain() {
        super(null);
    }

    public AbstractBlobDomain(Blob v) {
        super(v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractBlobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractBlobDomainVisitor<R, P, TH> v = AbstractBlobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractBlobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
