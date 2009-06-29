package org.seasar.doma.domain;

import java.math.BigDecimal;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public abstract class AbstractBigDecimalDomain<D extends AbstractBigDecimalDomain<D>>
        extends AbstractComparableDomain<BigDecimal, D> implements
        NumberDomain<BigDecimal, D> {

    public AbstractBigDecimalDomain() {
        super(null);
    }

    public AbstractBigDecimalDomain(BigDecimal value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        setInternal(new BigDecimal(v.doubleValue()));
    }

    @Override
    public void set(NumberDomain<BigDecimal, D> other) {
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
        if (AbstractDateDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractBigDecimalDomainVisitor<R, P, TH> v = AbstractBigDecimalDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractBigDecimalDomain(this, p);
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
        AbstractBigDecimalDomain<?> other = AbstractBigDecimalDomain.class
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
        return value != null ? value.toPlainString() : null;
    }

}
