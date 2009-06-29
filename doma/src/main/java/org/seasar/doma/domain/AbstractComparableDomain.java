package org.seasar.doma.domain;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractComparableDomain<V extends Comparable<? super V>, D extends AbstractDomain<V, D> & ComparableDomain<V, D>>
        extends AbstractDomain<V, D> implements ComparableDomain<V, D> {

    public AbstractComparableDomain(V v) {
        super(v);
    }

    @Override
    public int compareTo(D other) {
        if (other == null) {
            throw new DomaIllegalArgumentException("other", other);
        }
        assertComparable(other);
        return value.compareTo(other.value);
    }

    @Override
    public boolean eq(V other) {
        if (value == null && other == null) {
            return true;
        }
        if (value == null || other == null) {
            return false;
        }
        return value.compareTo(other) == 0;
    }

    @Override
    public boolean eq(D other) {
        if (other == null) {
            throw new DomaIllegalArgumentException("other", other);
        }
        if (value == null && other.value == null) {
            return true;
        }
        if (value == null || other.value == null) {
            return false;
        }
        return compareTo(other) == 0;
    }

    @Override
    public boolean ge(V other) {
        assertComparable(other);
        return value.compareTo(other) >= 0;
    }

    @Override
    public boolean ge(D other) {
        return compareTo(other) >= 0;
    }

    @Override
    public boolean gt(V other) {
        assertComparable(other);
        return value.compareTo(other) > 0;
    }

    @Override
    public boolean gt(D other) {
        return compareTo(other) > 0;
    }

    @Override
    public boolean le(V other) {
        assertComparable(other);
        return value.compareTo(other) <= 0;
    }

    @Override
    public boolean le(D other) {
        return compareTo(other) <= 0;
    }

    @Override
    public boolean lt(V other) {
        assertComparable(other);
        return value.compareTo(other) < 0;
    }

    @Override
    public boolean lt(D other) {
        return compareTo(other) < 0;
    }

    protected void assertComparable(V other) {
        if (value == null || other == null) {
            throw new DomainIncomparableException();
        }
    }

    protected void assertComparable(D other) {
        if (value == null || other.value == null) {
            throw new DomainIncomparableException();
        }
    }
}
