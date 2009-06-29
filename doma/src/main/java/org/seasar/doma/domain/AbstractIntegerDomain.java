package org.seasar.doma.domain;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractIntegerDomain<D extends AbstractIntegerDomain<D>>
        extends AbstractComparableDomain<Integer, D> implements
        NumberDomain<Integer, D> {

    public AbstractIntegerDomain() {
        super(null);
    }

    public AbstractIntegerDomain(Integer value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        setInternal(v.intValue());
    }

    @Override
    public void set(NumberDomain<Integer, D> other) {
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
        if (AbstractIntegerDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractIntegerDomainVisitor<R, P, TH> v = AbstractIntegerDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractIntegerDomain(this, p);
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
        AbstractIntegerDomain<?> other = AbstractIntegerDomain.class.cast(o);
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
