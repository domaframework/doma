package org.seasar.doma.domain;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractLongDomain<D extends AbstractLongDomain<D>>
        extends AbstractComparableDomain<Long, D> implements
        NumberDomain<Long, D> {

    public AbstractLongDomain() {
        super(null);
    }

    public AbstractLongDomain(Long value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        setInternal(v.longValue());
    }

    @Override
    public void set(NumberDomain<Long, D> other) {
        if (other == null) {
            throw new DomaIllegalArgumentException("other", other);
        }
        setInternal(other.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractLongDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractLongDomainVisitor<R, P, TH> v = AbstractLongDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractLongDomain(this, p);
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
        AbstractLongDomain<?> other = AbstractLongDomain.class.cast(o);
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
