package org.seasar.doma.domain;

import java.sql.Timestamp;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public abstract class AbstractTimestampDomain<D extends AbstractTimestampDomain<D>>
        extends AbstractComparableDomain<Timestamp, D> {

    public AbstractTimestampDomain() {
        super(null);
    }

    public AbstractTimestampDomain(Timestamp value) {
        super(value);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractTimestampDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractTimestampDomainVisitor<R, P, TH> v = AbstractTimestampDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractTimestampDomain(this, p);
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
        AbstractTimestampDomain<?> other = AbstractTimestampDomain.class
                .cast(o);
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
