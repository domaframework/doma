package org.seasar.doma.domain;

import java.sql.Date;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public abstract class AbstractDateDomain<D extends AbstractDateDomain<D>>
        extends AbstractComparableDomain<Date, D> {

    public AbstractDateDomain() {
        super(null);
    }

    public AbstractDateDomain(Date value) {
        super(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractDateDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractDateDomainVisitor<R, P, TH> v = AbstractDateDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractDateDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractDateDomain<?> other = AbstractDateDomain.class.cast(o);
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

}
