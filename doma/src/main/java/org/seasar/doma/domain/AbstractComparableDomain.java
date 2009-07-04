/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.domain;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractComparableDomain<V extends Comparable<? super V>, D extends AbstractDomain<V, D> & ComparableDomain<V, D>>
        extends AbstractDomain<V, D> implements ComparableDomain<V, D> {

    public AbstractComparableDomain() {
    }

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
