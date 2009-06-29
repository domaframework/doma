package org.seasar.doma.domain;

import java.sql.Time;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public abstract class AbstractTimeDomain<D extends AbstractTimeDomain<D>>
        extends AbstractComparableDomain<Time, D> {

    public AbstractTimeDomain() {
        super(null);
    }

    public AbstractTimeDomain(Time value) {
        super(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractTimeDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractTimeDomainVisitor<R, P, TH> v = AbstractTimeDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractTimeDomain(this, p);
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
        AbstractTimeDomain<?> other = AbstractTimeDomain.class.cast(o);
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
